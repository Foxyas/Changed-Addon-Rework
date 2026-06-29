package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.ICoatLikeEntity;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityWithTameFunction;
import net.foxyas.changedaddon.entity.defaults.AbstractExp2SnepChangedEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractTamableLatexEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractUnfuseableChangedEntity;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.AbilityColors;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static net.foxyas.changedaddon.ability.UnfuseAbility.Instance;

public class UnfuseAbility extends AbstractAbility<Instance> {

    public UnfuseAbility() {
        super(Instance::new);
    }

    public static Optional<Integer> getColor(AbstractAbilityInstance instance, int layer) {
        if (!(instance instanceof Instance abilityInstance)) {
            return Optional.empty();
        }
        AbstractRadialScreen.ColorScheme scheme = AbilityColors.getAbilityColors(abilityInstance);
        float chargePercent = abilityInstance.getController().chargePercent();
        if (chargePercent < 0.25f && layer == 0) {
            return Optional.of(scheme.foreground().toInt());
        } else if (chargePercent >= 0.25f && chargePercent < 0.50F && layer == 1) {
            return Optional.of(scheme.foreground().toInt());
        } else if (chargePercent >= 0.50F && chargePercent < 0.85f && layer == 2) {
            return Optional.of(scheme.foreground().toInt());
        } else if (chargePercent >= 0.85F && layer == 3) {
            return Optional.of(scheme.foreground().toInt());
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

    public static class Instance extends AbstractAbilityInstance {

        protected boolean entitySpawned = false;
        //protected Container entityInventory = null;
        //protected ChangedEntity unfusedEntity = null;

        public Instance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
            super(ability, entity);
        }

        @Override
        public boolean canUse() {
            return ability.canUse(entity);
        }

        @Override
        public boolean canKeepUsing() {
            return ability.canKeepUsing(entity);
        }

        @Override
        public void startUsing() {
            Entity entityUnfused;
            TransfurVariantInstance<?> transfurVariantInstance = entity.getTransfurVariantInstance();
            if (transfurVariantInstance == null) {
                return;
            }
            if (!(entity.getLevel() instanceof ServerLevel serverLevel)) {
                return;
            }


            ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();
            if (changedEntity instanceof ICoatLikeEntity iCoatLikeEntity) {
                Player host = transfurVariantInstance.getHost();
                entityUnfused = changedEntity.getType().create(host.level());

                if (!(entityUnfused instanceof ChangedEntity changedEntityUnfused) || !(changedEntityUnfused instanceof ICoatLikeEntity coatLikeEntity)) {
                    return;
                }

                if (changedEntityUnfused instanceof AbstractTamableLatexEntity abstractTamableLatexEntity) {
                    abstractTamableLatexEntity.tame(host);
                    coatLikeEntity.setIsUnfusedFromHost(true);
                } else if (changedEntityUnfused instanceof AbstractExp2SnepChangedEntity abstractExp2SnepChangedEntity) {
                    abstractExp2SnepChangedEntity.tame(host);
                    abstractExp2SnepChangedEntity.setIsUnfusedFromHost(true);
                } else if (changedEntityUnfused instanceof AbstractUnfuseableChangedEntity unfuseableChangedEntity) {
                    unfuseableChangedEntity.tame(host);
                    unfuseableChangedEntity.setIsUnfusedFromHost(true);
                } else if (changedEntityUnfused instanceof TamableLatexEntityWithTameFunction tamableLatexEntityWithTameFunction) {
                    tamableLatexEntityWithTameFunction.tameEntityForPlayer(host);
                    coatLikeEntity.setIsUnfusedFromHost(true);
                } else {
                    coatLikeEntity.setIsUnfusedFromHost(true);
                }

                changedEntityUnfused.setPos(host.position());
                LivingEntity target = host.getLastHurtByMob();

                if (target != null && target.distanceTo(host) < 5 && FoxyasUtil.canEntitySeeOther(changedEntityUnfused, target)) {
                    if (changedEntityUnfused.canAttack(target)) {
                        changedEntityUnfused.setTarget(target);
                    }
                }

                changedEntityUnfused.setXRot(host.getViewXRot(0));
                changedEntityUnfused.setYRot(host.getViewXRot(0));
                changedEntityUnfused.setYBodyRot(host.yBodyRotO);
                changedEntityUnfused.setYHeadRot(host.getYHeadRot());

                ForgeEventFactory.onFinalizeSpawn(changedEntityUnfused, serverLevel, serverLevel.getCurrentDifficultyAt(changedEntityUnfused.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);

                if (changedEntity instanceof IAlphaAbleEntity original && entityUnfused instanceof IAlphaAbleEntity alphaAble) {
                    alphaAble.setAlpha(original.isAlpha());
                }
                changedEntityUnfused.copyTraitsFrom(IAbstractChangedEntity.forEither(changedEntity));


//                if (changedEntity instanceof TamableLatexEntityFavors fusedEntity) {
//                    this.entityInventory = fusedEntity.getInventory();
//                }
//                unfusedEntity = changedEntityUnfused;
                entitySpawned = serverLevel.addFreshEntity(changedEntityUnfused);
            }
        }

        @Override
        public void tick() {

        }

        @Override
        public void stopUsing() {
            LivingEntity livingEntity = entity.getEntity();
//            if (unfusedEntity instanceof TamableLatexEntityFavors tamableLatexEntityFavors) {
//                if (entityInventory instanceof LatexInventory latexInventory) {
//                    tamableLatexEntityFavors.setInventory(latexInventory);
//                }
//            }
            if (livingEntity instanceof Player player && entitySpawned) {
                PlayerUtil.unTransfurPlayerAndPlaySound(player, true);
            }
        }
    }
}
