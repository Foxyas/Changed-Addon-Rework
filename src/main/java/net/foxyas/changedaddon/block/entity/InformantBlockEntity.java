package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.foxyas.changedaddon.world.inventory.InformantGuiMenu;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InformantBlockEntity extends RandomizableContainerBlockEntity implements Container {

    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);
    private final LazyOptional<IItemHandler> generic = LazyOptional.of(() -> new InvWrapper(this));
    private final LazyOptional<IItemHandler> in = LazyOptional.of(() -> new InvWrapper(this){
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final LazyOptional<IItemHandler> out = LazyOptional.of(() -> new InvWrapper(this){
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }
    });
    private String text = "";
    private TransfurVariant<?> selectedTf;
    private TransfurVariant<?> itemTF;

    public InformantBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonBlockEntities.INFORMANT_BLOCK.get(), position, state);
    }

    public @NotNull String getText() {
        return text;
    }

    @ApiStatus.Internal
    public void updateInternal(@NotNull String text, @Nullable TransfurVariant<?> selectedTf){
        boolean changed = false;

        if(!this.text.equals(text)){
            this.text = text;
            changed = true;
        }

        if(this.selectedTf != selectedTf){
            this.selectedTf = selectedTf;
            changed = true;
        }

        if(changed) setChanged();
    }

    public @Nullable TransfurVariant<?> getDisplayTf(){
        return itemTF != null ? itemTF : selectedTf;
    }

    private void updateItemTF(ItemStack stack){
        if(stack.isEmpty()) {
            itemTF = null;
            return;
        }

        String tf = stack.getOrCreateTag().getString("form");
        ResourceLocation loc = ResourceLocation.tryParse(tf);
        itemTF = loc == null ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
    }

    @Override
    public void setItem(int pIndex, @NotNull ItemStack pStack) {
        super.setItem(pIndex, pStack);

        if(pIndex == 0) updateItemTF(pStack);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        BlockState state = getBlockState();
        if(level != null) level.sendBlockUpdated(getBlockPos(), state, state, 3);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if (!tryLoadLootTable(compound))
            setItem(0, ItemStack.of(compound.getCompound("Item")));

        ResourceLocation loc = ResourceLocation.tryParse(compound.getString("SelectedTf"));
        updateInternal(compound.getString("Text"), loc != null ? ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc) : null);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        if (!trySaveLootTable(compound)) {
            compound.put("Item", stacks.get(0).save(new CompoundTag()));
        }

        compound.putString("Text", text);
        if(selectedTf != null) compound.putString("SelectedTf", selectedTf.toString());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        return stacks.get(0).isEmpty();
    }

    @Override
    public @NotNull Component getDefaultName() {
        return new TextComponent("Informant Block");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        return new InformantGuiMenu(id, inventory, this);
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
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if(capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return super.getCapability(capability, facing);

        if(facing == null) return generic.cast();

        if(facing == Direction.DOWN) return out.cast();

        return in.cast();
    }

    private AABB aabb;
    @Override
    public AABB getRenderBoundingBox() {
        if(aabb == null) {
            BlockPos above = getBlockPos().above();
            aabb = new AABB(above.getX(), above.getY(), above.getZ(), above.getX() + 1, above.getY() + 1.5, above.getZ() + 1);
        }
        return aabb;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        generic.invalidate();
        in.invalidate();
        out.invalidate();
    }
}
