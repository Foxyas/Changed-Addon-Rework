package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class SoftenAbilityInstance extends AbstractAbilityInstance {

    public SoftenAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return entity.getSelfVariant() != null && ChangedEntityUtil.isGoo(entity.getEntity());
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {
    }

    public boolean isActivate() {
        return this.getController().getHoldTicks() > 0;
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
    }

    @Override
    public AbstractAbility.UseType getUseType() {
        return super.getUseType();
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
    }


    public boolean isEntityInValidState() {
        boolean isWearingArmor = Arrays.stream(EquipmentSlot.values()).filter(EquipmentSlot::isArmor).anyMatch(e -> {
            ItemStack itemBySlot = this.entity.getEntity().getItemBySlot(e);
            return !itemBySlot.isEmpty();
        });

        boolean isGrabbingNotGoo = entity.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).map(grabEntityAbilityInstance -> {
            LivingEntity currentEntity = grabEntityAbilityInstance.grabbedEntity;

            // Traverse down the grab chain
            while (currentEntity != null) {
                // If we find ANY entity in the chain that is NOT goo, return true
                if (!ChangedEntityUtil.isGoo(currentEntity)) {
                    return true;
                }

                // Check if the currently grabbed entity is also grabbing someone else
                IAbstractChangedEntity iAbstractChanged = IAbstractChangedEntity.forEither(currentEntity);
                if (iAbstractChanged != null) {
                    var subGrabInstance = iAbstractChanged.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
                    if (subGrabInstance.isPresent() && subGrabInstance.get().grabbedEntity != null) {
                        currentEntity = subGrabInstance.get().grabbedEntity;
                        continue;
                    }
                }

                break;
            }

            return false;
        }).orElse(false);

        return !isWearingArmor && !isGrabbingNotGoo;
    }

}
