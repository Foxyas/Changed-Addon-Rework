package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.ICoatLikeEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractExp2SnepChangedEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractTamableLatexEntity;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UnfuseAbility extends SimpleAbility {

    public static Optional<Integer> getColor(AbstractAbilityInstance abilityInstance, int layer) {
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);
        if (abilityInstance instanceof SimpleAbilityInstance Instance) {
            float chargePercent = Instance.getController().chargePercent();
            if (chargePercent < 0.25f && layer == 0) {
                return Optional.of(scheme.foreground().toInt());
            } else if (chargePercent >= 0.25f && chargePercent < 0.50F && layer == 1) {
                return Optional.of(scheme.foreground().toInt());
            } else if (chargePercent >= 0.50F && chargePercent < 0.85f && layer == 2) {
                return Optional.of(scheme.foreground().toInt());
            } else if (chargePercent >= 0.85F && layer == 3) {
                return Optional.of(scheme.foreground().toInt());
            }
        }
        return Optional.empty();
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/paw_with_claws.png");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        List<Component> descriptions = new ArrayList<>(super.getAbilityDescription(entity));
        descriptions.add(Component.translatable("ability.changed_addon.unfuse.description.line1"));
        descriptions.add(Component.translatable("ability.changed_addon.unfuse.description.line2"));
        return descriptions;
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.unfuse");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getTransfurVariantInstance() != null && entity.getTransfurVariantInstance().getChangedEntity() instanceof ICoatLikeEntity;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 60;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 360;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        Entity entityUnfused;
        TransfurVariantInstance<?> transfurVariantInstance = entity.getTransfurVariantInstance();
        if (transfurVariantInstance == null) {
            return;
        }


        ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();
        if (changedEntity  instanceof ICoatLikeEntity iCoatLikeEntity) {
            Player host = transfurVariantInstance.getHost();
            entityUnfused = changedEntity.getType().create(host.level());

            if (!(entityUnfused instanceof ChangedEntity changedEntityUnfused)) {
                return;
            }

            if (changedEntityUnfused instanceof AbstractTamableLatexEntity abstractTamableLatexEntity) {
                abstractTamableLatexEntity.tame(host);
                iCoatLikeEntity.setIsUnfusedFromHost(true);
            } else if (changedEntityUnfused instanceof AbstractExp2SnepChangedEntity abstractExp2SnepChangedEntity) {
                abstractExp2SnepChangedEntity.tame(host);
                abstractExp2SnepChangedEntity.setIsUnfusedFromHost(true);
            }

            iCoatLikeEntity.setIsUnfusedFromHost(true);

            changedEntityUnfused.setPos(host.position());
            LivingEntity target = host.getLastHurtByMob();

            if (target != null && target.distanceTo(host) < 5 && FoxyasUtils.canEntitySeeOther(changedEntityUnfused, target)) {
                if (changedEntityUnfused.canAttack(target)) {
                    changedEntityUnfused.setTarget(target);
                }
            }

            changedEntityUnfused.setXRot(host.getViewXRot(0));
            changedEntityUnfused.setYRot(host.getViewXRot(0));
            changedEntityUnfused.setYBodyRot(host.yBodyRotO);
            changedEntityUnfused.setYHeadRot(host.getYHeadRot());

            if (host.level() instanceof ServerLevel serverLevel) {
                ForgeEventFactory.onFinalizeSpawn(changedEntityUnfused, serverLevel, serverLevel.getCurrentDifficultyAt(changedEntityUnfused.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);

                if (changedEntity instanceof IAlphaAbleEntity original && entityUnfused instanceof IAlphaAbleEntity alphaAble) {
                    alphaAble.setAlpha(original.isAlpha());
                }

                serverLevel.addFreshEntity(changedEntityUnfused);
                serverLevel.playSound(null, host, ChangedSounds.TRANSFUR_BY_LATEX.get(), SoundSource.PLAYERS, 1, 1);
                PlayerUtil.UnTransfurPlayer(host);
            }
        }
    }
}
