package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
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

public class CatalyzerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    protected final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());
    public boolean startRecipe = true;
    public double nitrogenPower = 0;
    public double recipeProgress = 0;
    public int tickCount = 0;
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);
    protected boolean recipeOn = true;

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

    static @Nullable CatalyzerRecipe findRecipe(ServerLevel level, ItemStack input) {
        RecipeManager recipeManager = level.getRecipeManager();

        // Obtém todas as receitas do tipo JeiCatalyzerRecipe
        List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);

        // Cria um contêiner simples com o input fornecido
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, input);

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

        nitrogenPower = tag.getDouble("nitrogen_power");
        recipeProgress = tag.getDouble("recipe_progress");
        recipeOn = tag.getBoolean("recipe_on");
        startRecipe = tag.getBoolean("start_recipe");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.stacks);
        }

        tag.putDouble("nitrogen_power", nitrogenPower);
        tag.putDouble("recipe_progress", recipeProgress);
        tag.putBoolean("recipe_on", recipeOn);
        tag.putBoolean("start_recipe", startRecipe);
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
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return index != 0;
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

    public float getSpeedMultiplier() {
        return 1f;
    }

    public SimpleContainer getContainer() {
        return new SimpleContainer(this.stacks.toArray(new ItemStack[0]));
    }
}
