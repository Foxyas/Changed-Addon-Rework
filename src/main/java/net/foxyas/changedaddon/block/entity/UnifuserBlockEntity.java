package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

public class UnifuserBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    protected final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());
    public boolean startRecipe = true;
    public double recipeProgress = 0;
    public int tickCount;
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(4, ItemStack.EMPTY);
    protected boolean recipeProgressOn = true;

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

        recipeProgress = tag.getDouble("recipe_progress");
        recipeProgressOn = tag.getBoolean("recipe_on");
        startRecipe = tag.getBoolean("start_recipe");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.stacks);
        }

        tag.putDouble("recipe_progress", recipeProgress);
        tag.putBoolean("recipe_on", recipeProgressOn);
        tag.putBoolean("start_recipe", startRecipe);
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
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        if (index == 0)
            return false;
        if (index == 1)
            return false;
        return index != 2;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
            return handlers[facing.ordinal()].cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlers)
            handler.invalidate();
    }

    public SimpleContainer getContainer() {
        return new SimpleContainer(this.stacks.toArray(new ItemStack[0]));
    }
}
