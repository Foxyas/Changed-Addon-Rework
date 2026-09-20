package net.foxyas.changedaddon.process;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Stores the full serialized NBT of the grabber entity that was holding this player
 * when they logged out. The grabber is fully discarded from the world at logout and
 * reconstructed from this data at login — so there's no dependency on the grabber's
 * chunk being loaded, and no falling/AI-with-no-target window.
 */
public interface GrabberAttachment {

    CompoundTag getStoredGrabberData();
    void setStoredGrabberData(CompoundTag tag);

    void save(CompoundTag nbt);
    void load(CompoundTag nbt);

    default boolean hasStoredGrabber() {
        return getStoredGrabberData() != null;
    }

    class Default implements GrabberAttachment, INBTSerializable<CompoundTag> {
        private CompoundTag storedGrabberData;

        @Override
        public CompoundTag getStoredGrabberData() {
            return storedGrabberData;
        }

        @Override
        public void setStoredGrabberData(CompoundTag tag) {
            this.storedGrabberData = tag;
        }

        @Override
        public void save(CompoundTag nbt) {
            nbt.put("GrabberAttachment", serializeNBT());
        }

        @Override
        public void load(CompoundTag nbt) {
            if (nbt.contains("GrabberAttachment")) {
                deserializeNBT(nbt.getCompound("GrabberAttachment"));
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            if (storedGrabberData != null) {
                tag.put("StoredGrabber", storedGrabberData);
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            storedGrabberData = tag.contains("StoredGrabber")
                    ? tag.getCompound("StoredGrabber")
                    : null;
        }
    }
}
