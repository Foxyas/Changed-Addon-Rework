package net.foxyas.changedaddon.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
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
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
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

public class CatalyzerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, RecipeHolder, IRecipeRewarder, StackedContentsCompatible {

    protected final LazyOptional<? extends IItemHandler>[] itemHandler = SidedInvWrapper.create(this, Direction.values());
    public boolean startRecipe = true;
    public double nitrogenPower = 0;
    public double recipeProgress = 0;
    public int tickCount = 0;
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    protected boolean recipeOn = true;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public CatalyzerBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonBlockEntities.CATALYZER.get(), position, state);
    }

    public CatalyzerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos position, BlockState state) {
        super(blockEntityType, position, state);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!(blockEntity instanceof CatalyzerBlockEntity catalyzer)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        boolean shouldTick = false;
        if (catalyzer.tickCount >= 5) {
            shouldTick = true;
            catalyzer.tickCount = 0;
        }

        if (!shouldTick) {
            catalyzer.tickCount++;
            update(serverLevel, pos, state, catalyzer);
            return;
        }

        if (catalyzer.nitrogenPower < 200) {
            catalyzer.nitrogenPower += 1;
            update(serverLevel, pos, state, catalyzer);
        }

        IItemHandlerModifiable handler = (IItemHandlerModifiable)
                blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElse(null);
        if (handler == null) return;

        if (handler.getStackInSlot(0).isEmpty()) {
            catalyzer.recipeOn = false;
            catalyzer.recipeProgress = Math.max(0, catalyzer.recipeProgress - 5);
            update(serverLevel, pos, state, catalyzer);
            return;
        }

        boolean isFull = handler.getStackInSlot(1).getCount() >= handler.getStackInSlot(1).getMaxStackSize();
        if (isFull) {
            update(serverLevel, pos, state, catalyzer);
            return;
        }

        if (!catalyzer.startRecipe) {
            update(serverLevel, pos, state, catalyzer);
            return;
        }

        ItemStack input = handler.getStackInSlot(0).copy();
        CatalyzerRecipe recipe = findRecipe(serverLevel, input);
        catalyzer.recipeOn = recipe != null;

        if (recipe != null) {
            if (catalyzer.recipeProgress < 100) {
                double speed = recipe.getProgressSpeed() * catalyzer.getSpeedMultiplier();
                catalyzer.recipeProgress += speed;
            }

            if (catalyzer.recipeProgress >= 100) {
                ItemStack output = recipe.getResultItem(level.registryAccess());

                if (handler.insertItem(1, output.copy(), true).isEmpty()) {
                    NonNullList<ItemStack> remainingItems = recipe.getRemainingItems(catalyzer.getContainer());
                    handler.extractItem(0, 1, false);

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
                                Vec3 vecPos = Vec3.upFromBottomCenterOf(catalyzer.getBlockPos(), 0.5);
                                Containers.dropItemStack(serverLevel, vecPos.x, vecPos.y, vecPos.z, remaining);
                            }

                        }
                    }

                    handler.insertItem(1, output.copy(), false);
                    catalyzer.nitrogenPower -= recipe.getNitrogenUsage();
                    catalyzer.recipeProgress = 0;
                    catalyzer.setRecipeUsed(recipe);
                    serverLevel.playSound(null,
                            pos,
                            SoundEvents.BREWING_STAND_BREW,
                            SoundSource.BLOCKS,
                            1,
                            1);
                }
            }
        } else {
            catalyzer.recipeProgress = 0;
        }

        update(serverLevel, pos, state, catalyzer);
    }

    public static @Nullable CatalyzerRecipe findRecipe(ServerLevel level, ItemStack input) {
        RecipeManager recipeManager = level.getRecipeManager();

        // Obtém todas as receitas do tipo JeiCatalyzerRecipe
        List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

        // Verifica cada receita para ver se ela corresponde ao input fornecido
        for (CatalyzerRecipe recipe : catalyzerRecipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            if (!ingredients.get(0).test(input))
                continue;
            return recipe;
        }

        return null;
    }

    private static void update(ServerLevel level, BlockPos pos, BlockState state, CatalyzerBlockEntity be) {
        be.setChanged();
        level.sendBlockUpdated(pos, state, state, 3);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (!this.tryLoadLootTable(tag))
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);

        nitrogenPower = tag.getDouble("nitrogenPower");
        recipeProgress = tag.getDouble("recipeProgress");
        recipeOn = tag.getBoolean("recipeOn");
        startRecipe = tag.getBoolean("startRecipe");
        CompoundTag compoundtag = tag.getCompound("RecipesUsed");

        for (String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.parse(s), compoundtag.getInt(s));
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.stacks);
        }

        tag.putDouble("nitrogenPower", nitrogenPower);
        tag.putDouble("recipeProgress", recipeProgress);
        tag.putBoolean("recipeOn", recipeOn);
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
        return saveWithoutMetadata();
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
        return Component.literal("catalyzer");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        return new CatalyzerGuiMenu(id, inventory, getBlockPos());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Catalyzer");
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
        if (side == Direction.DOWN) {
            return new int[]{1};
        }
        if (side == Direction.UP) {
            return new int[]{0};
        }

        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction) {
//        return true;
        return canPlaceItem(index, stack) && direction != Direction.DOWN && index <= 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return true;
//        return direction == Direction.DOWN && index >= 1;
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

    public float getSpeedMultiplier() {
        return 1f;
    }

    public float getExperienceMultiplier() {
        return 1f;
    }


    public SimpleContainer getContainer() {
        return new SimpleContainer(this.stacks.toArray(new ItemStack[0]));
    }

    @Override
    public boolean canTakeItem(@NotNull Container pTarget, int pIndex, @NotNull ItemStack pStack) {
        if (pTarget instanceof HopperBlockEntity hopperBlockEntity && pIndex != 1) {
            return false;
        }

        return super.canTakeItem(pTarget, pIndex, pStack);
    }

    @Override
    public boolean canPlaceItem(int pIndex, @NotNull ItemStack pStack) {
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
    public void fillStackedContents(@NotNull StackedContents stackedContents) {
        for (ItemStack itemstack : this.stacks) {
            stackedContents.accountStack(itemstack);
        }

    }

    @Override
    public void awardUsedRecipes(ServerPlayer player, ItemStack pStack) {
        awardUsedRecipesAndPopExperience(player);
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(pPlayer.serverLevel(), pPlayer.position());
        pPlayer.awardRecipes(list);

        for (Recipe<?> recipe : list) {
            if (recipe != null) {
                pPlayer.triggerRecipeCrafted(recipe, this.stacks);
            }
        }

        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pPopVec) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(pLevel, pPopVec, entry.getIntValue(), ((CatalyzerRecipe) recipe).getExperience() * this.getExperienceMultiplier());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float) pRecipeIndex * pExperience);
        float f = Mth.frac((float) pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, pPopVec, i);
    }
}
