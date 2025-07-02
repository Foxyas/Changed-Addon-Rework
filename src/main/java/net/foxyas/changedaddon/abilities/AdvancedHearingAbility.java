package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.mixins.MobAccessor;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.Collection;
import java.util.List;

public class AdvancedHearingAbility extends SimpleAbility {

    private SimpleAbilityInstance abilityInstance;

    public AdvancedHearingAbility() {
        super();
    }

    @Override
    public SimpleAbilityInstance makeInstance(IAbstractChangedEntity entity) {
        abilityInstance = super.makeInstance(entity);
        return abilityInstance;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    @Override
    public boolean canKeepUsing(IAbstractChangedEntity entity) {
        return super.canKeepUsing(entity);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.advanced_hearing");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/advanced_hearing.png");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        return super.getAbilityDescription(entity);
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 30;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 30;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        var User = entity.getEntity();
        if (!User.getLevel().isClientSide()) {
            User.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 3), User);
        }
        List<PathfinderMob> livingEntityList = User.getLevel().getEntities(EntityTypeTest.forClass(PathfinderMob.class), User.getBoundingBox().inflate(30), (e) -> !e.isShiftKeyDown() && e != User && e instanceof Enemy);
        if (!livingEntityList.isEmpty()) {
            for (PathfinderMob living : livingEntityList) {
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 10), User);
                if (living instanceof MobAccessor mobAccessor) {
                    if (User instanceof Player player) {
                    	if (mobAccessor.callGetAmbientSound() != null) {
							living.getLevel().playSound(player, living, mobAccessor.callGetAmbientSound(), SoundSource.AMBIENT, 2f, 1f);
                    	}
                    }
                }
            }
        }
    }
}
