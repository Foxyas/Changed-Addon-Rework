package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;

public class WindPassiveAbility extends SimpleAbility {
    public boolean isActive = false;

    public WindPassiveAbility() {
        super();
    }

    public static void spawnAirParticles(LivingEntity livingEntity) {
        if (livingEntity.getRandom().nextFloat() >= 0.75f) {
            ParticlesUtil.sendParticlesWithMotion(livingEntity,
                    ParticleTypes.POOF,
                    new Vec3(0.3, 0.1, 0.3),
                    new Vec3(0, 0.25f, 0),
                    2, 0.25f);
        }
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        if (tag.contains("isActive")) {
            this.isActive = tag.getBoolean("isActive");
        }
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.wind_control_passive");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> list = new ArrayList<>(super.getAbilityDescription(entity));
        list.add(Component.translatable("ability.changed_addon.wind_control_passive.desc"));
        return list;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.MENU;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 5;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getTransfurVariantInstance() != null;
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        return entity.getTransfurVariantInstance() != null;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (!entity.getLevel().isClientSide()) {
            TurnOnPassive(entity);
            this.setDirty(entity);
        }
    }

    public void TurnOnPassive(IAbstractChangedEntity entity) {
        this.isActive = !this.isActive;
        entity.displayClientMessage(this.isActive ?
                        Component.translatable("ability.changed_addon.passive.toggle.on") :
                        Component.translatable("ability.changed_addon.passive.toggle.off")
                , true);
    }
}
