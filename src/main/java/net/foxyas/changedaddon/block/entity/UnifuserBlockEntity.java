package net.foxyas.changedaddon.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

public class UnifuserBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, RecipeHolder, IRecipeRewarder {

    protected final LazyOptional<? extends IItemHandler>[] itemHandler = SidedInvWrapper.create(this, Direction.values());
    public boolean startRecipe = true;
    public double recipeProgress = 0;
    public int tickCount;
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(4, ItemStack.EMPTY);
    protected boolean recipeProgressOn = true;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public UnifuserBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonBlockEntities.UNIFUSER.get(), position, state);
    }

    public UnifuserBlockEntity(BlockEntityType<?> blockEntityType, BlockPos position, BlockState state) {
        super(blockEntityType, position, state);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.isClientSide) return;
        if (!(blockEntity instanceof UnifuserBlockEntity unifuser)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        boolean shouldTick = false;
        if (unifuser.tickCount >= 5) {
            shouldTick = true;
            unifuser.tickCount = 0;
        }

        if (!shouldTick) {
            unifuser.tickCount++;
            level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            return;
        }

        IItemHandlerModifiable handler = (IItemHandlerModifiable)
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElse(null);
        if (handler == null) return;

        // --- Checar inputs e output ---
        ItemStack input0 = handler.getStackInSlot(0);
        ItemStack input1 = handler.getStackInSlot(1);
        ItemStack input2 = handler.getStackInSlot(2);
        ItemStack output = handler.getStackInSlot(3);

        // Nenhum input = parar receita
        if (input0.isEmpty() && input1.isEmpty() && input2.isEmpty()) {
            unifuser.recipeProgressOn = false;

            if (unifuser.recipeProgress > 0) {
                unifuser.recipeProgress = Math.max(0, unifuser.recipeProgress - 5);
            }

            unifuser.setChanged();
            return;
        }

        // Output cheio = travar processo
        boolean outputFull = !output.isEmpty() && output.getCount() >= output.getMaxStackSize();
        if (outputFull) {
            unifuser.setChanged();
            return;
        }

        // Sem receita iniciada
        if (!unifuser.startRecipe) {
            unifuser.recipeProgress = 0;
            unifuser.setChanged();
            level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            return;
        }

        // Encontrar receita válida
        UnifuserRecipe recipe = findRecipe(serverLevel, input0, input1, input2);
        boolean hasRecipe = recipe != null;
        unifuser.recipeProgressOn = hasRecipe;

        // Progresso da receita
        if (hasRecipe) {
            if (unifuser.recipeProgress < 100) {
                unifuser.recipeProgress += recipe.getProgressSpeed() * unifuser.getSpeedMultiplier();
            }
        } else {
            unifuser.recipeProgress = 0;
        }

        // Concluir receita
        if (hasRecipe && unifuser.recipeProgress >= 100) {
            ItemStack result = recipe.getResultItem(level.registryAccess());

            boolean canOutput = handler.insertItem(3, result.copy(), true).isEmpty();

            if (canOutput) {
                NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(unifuser.getContainer());

                // Consumir inputs
                handler.extractItem(0, 1, false);
                handler.extractItem(1, 1, false);
                handler.extractItem(2, 1, false);


                // Recolocar os itens remanescentes de volta nos slots corretos
                for (int i = 0; i < remainingItems.size() - 1; i++) {
                    ItemStack remaining = remainingItems.get(i);
                    if (!remaining.isEmpty()) {
                        // Se o slot estiver vazio, apenas coloca o item remanescente lá
                        ItemStack current = handler.getStackInSlot(i);
                        boolean inserted = false;

                        if (current.isEmpty()) {
                            handler.setStackInSlot(i, remaining);
                            inserted = true;
                        } else if (ItemStack.isSameItemSameTags(current, remaining)) {
                            current.grow(remaining.getCount());
                            handler.setStackInSlot(i, current);
                            inserted = true;
                        } else {
                            ItemStack leftover = handler.insertItem(i, remaining, false);
                            if (leftover.isEmpty()) {
                                inserted = true;
                            } else {
                                remaining = leftover; // ainda sobrou algo
                            }
                        }

                        // Se não conseguiu colocar no slot, dropar no mundo
                        if (!inserted && !remaining.isEmpty()) {
                            Vec3 vecPos = Vec3.upFromBottomCenterOf(unifuser.getBlockPos(), 0.5f);
                            Containers.dropItemStack(level, vecPos.x, vecPos.y, vecPos.z, remaining);
                        }
                    }
                }

                // Adicionar output
                handler.insertItem(3, result.copy(), false);

                // Resetar progresso e consumir energia
                unifuser.recipeProgress = 0;
                serverLevel.playSound(null,
                        blockPos,
                        SoundEvents.BREWING_STAND_BREW,
                        SoundSource.BLOCKS,
                        1,
                        1);
            }
        }

        unifuser.setChanged();
        level.sendBlockUpdated(blockPos, blockState, blockState, 3);
    }

    public static @Nullable UnifuserRecipe findRecipe(ServerLevel level, ItemStack input1, ItemStack input2, ItemStack input3) {
        RecipeManager recipeManager = level.getRecipeManager();

        // Obtém todas as receitas do tipo JeiCatalyzerRecipe
        List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);

        // Cria um contêiner simples com o input fornecido
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, input1);

        // Verifica cada receita para ver se ela corresponde ao input fornecido
        for (UnifuserRecipe recipe : unifuserRecipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            if (!ingredients.get(0).test(input1))
                continue;
            if (ingredients.size() >= 2 && !ingredients.get(1).test(input2))
                continue;
            if (ingredients.size() == 3 && !ingredients.get(2).test(input3))
                continue;
            return recipe;
        }

        return null;
    }

    public float getSpeedMultiplier() {
        return 1f;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (!this.tryLoadLootTable(tag))
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);

        recipeProgress = tag.getDouble("recipeProgress");
        recipeProgressOn = tag.getBoolean("recipeOn");
        startRecipe = tag.getBoolean("startRecipe");
        CompoundTag compoundtag = tag.getCompound("RecipesUsed");

        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.parse(s), compoundtag.getInt(s));
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.stacks);
        }

        tag.putDouble("recipeProgress", recipeProgress);
        tag.putBoolean("recipeOn", recipeProgressOn);
        tag.putBoolean("startRecipe", startRecipe);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> {
            compoundtag.putInt(p_187449_.toString(), p_187450_);
        });
        tag.put("RecipesUsed", compoundtag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    public boolean isSlotFull(int index) {
        return getItem(index).getCount() >= getItem(index).getMaxStackSize();
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.literal("unifuser");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        return new UnifuserGuiMenu(id, inventory, worldPosition);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Unifuser");
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack) && index <= 2;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return true;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
            return itemHandler[facing.ordinal()].cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : itemHandler)
            handler.invalidate();
    }

    public SimpleContainer getContainer() {
        return new SimpleContainer(this.stacks.toArray(new ItemStack[0]));
    }

    @Override
    public boolean canTakeItem(@NotNull Container pTarget, int pIndex, @NotNull ItemStack pStack) {
        if (pTarget instanceof HopperBlockEntity hopperBlockEntity && pIndex != 3) {
            return false;
        }

        return super.canTakeItem(pTarget, pIndex, pStack);
    }

    @Override
    public boolean canPlaceItem(int pIndex, @NotNull ItemStack pStack) {
        if (pIndex == 3) {
            return false;
        }
        return super.canPlaceItem(pIndex, pStack);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Override
    public @Nullable Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(ServerPlayer player, ItemStack pStack) {
        awardUsedRecipesAndPopExperience(player);
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(pPlayer.serverLevel(), pPlayer.position());
        pPlayer.awardRecipes(list);

        for(Recipe<?> recipe : list) {
            if (recipe != null) {
                pPlayer.triggerRecipeCrafted(recipe, this.stacks);
            }
        }

        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                list.add(p_155023_);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((AbstractCookingRecipe)p_155023_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, pPopVec, i);
    }
}
