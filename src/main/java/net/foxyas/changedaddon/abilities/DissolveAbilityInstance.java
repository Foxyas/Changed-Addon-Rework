package net.foxyas.changedaddon.abilities;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DissolveAbilityInstance extends AbstractAbilityInstance {
    private double LocationX = 0;
    private double LocationY = 0;
    private double LocationZ = 0;
    private boolean isSet = false;

    public DissolveAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    static double Distance(Vec3 pos1, Vec3 pos2) {
        return pos1.distanceTo(pos2);
    }

    public boolean isSet() {
        if (this.LocationX == 0 && this.LocationY == 0 && this.LocationZ == 0 && this.isSet) {
            return true;
        }
        return !(this.LocationX == 0 && this.LocationY == 0 && this.LocationZ == 0) && this.isSet;
    }

    public double getLocationX() {
        return LocationX;
    }

    public double getLocationY() {
        return LocationY;
    }

    public double getLocationZ() {
        return LocationZ;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {
        if (entity.getEntity() instanceof Player player) {
            Tp(player);
        }
    }

    @Override
    public void tick() {
    }

    @Override
    public void stopUsing() {
    }

    @Override
    public void onRemove() {
        super.onRemove();
        UnSet();
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putDouble("LocationX", this.LocationX);
        tag.putDouble("LocationY", this.LocationY);
        tag.putDouble("LocationZ", this.LocationZ);
        tag.putBoolean("isSet", isSet);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        if (tag.contains("LocationX")) {
            this.LocationX = tag.getInt("LocationX");
        }
        if (tag.contains("LocationY")) {
            this.LocationY = tag.getInt("LocationY");
        }
        if (tag.contains("LocationZ")) {
            this.LocationZ = tag.getInt("LocationZ");
        }
        if (tag.contains("isSet")) {
            this.isSet = tag.getBoolean("isSet");
        }
    }

    private void UnSet() {
        this.LocationX = 0;
        this.LocationY = 0;
        this.LocationZ = 0;
        this.isSet = false;
    }

    private void SetTp(Player entity) {
        this.LocationX = entity.getX();
        this.LocationY = entity.getY();
        this.LocationZ = entity.getZ();
        this.isSet = true;
    }

    private void Tp(Player player) {
        TransfurVariantInstance<?> Instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (Instance == null) {
            return;
        }
        if (!isSet() && !player.isShiftKeyDown()) {
            SetTp(player);
            return;
        }
        if (isSet() && player.isShiftKeyDown()) {
            UnSet();
            return;
        }
        if (isSet() && !player.isShiftKeyDown() && Distance(player.position(), new Vec3(this.LocationX, this.LocationY, this.LocationZ)) <= 1000) {
            player.teleportTo(this.LocationX, this.LocationY, this.LocationZ);
            player.hurt(DamageSource.MAGIC.bypassArmor(), 4f);
            ChangedSounds.broadcastSound(player, ChangedSounds.POISON, 2.5f, 1);
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getFirst()), player.getX(), player.getY() + 1, player.getZ(), 5, 0.2, 0.3, 0.2, 0);
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getSecond()), player.getX(), player.getY() + 1, player.getZ(), 5, 0.2, 0.3, 0.2, 0);

                //TP POS
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getFirst()), getLocationX(), getLocationY() + 1, getLocationZ(), 5, 0.2, 0.3, 0.2, 0);
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getSecond()), getLocationX(), getLocationY() + 1, getLocationZ(), 5, 0.2, 0.3, 0.2, 0);
            }
        } else if (Distance(player.position(), new Vec3(this.LocationX, this.LocationY, this.LocationZ)) > 1000) {
            player.displayClientMessage(Component.translatable("changed_addon.ability.dissolve.warn.too_far"), true);
        }
    }
}
