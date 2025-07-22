package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.entity.defaults.AbstractExp2SnepChangedEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractTamableLatexEntity;
import net.foxyas.changedaddon.entity.interfaces.ICoatLikeEntity;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnfuseAbility extends SimpleAbility {

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/claw.png");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        List<Component> descriptions = new ArrayList<>(super.getAbilityDescription(entity));
        descriptions.add(new TranslatableComponent("changed_addon.ability.unfuse.description.line1"));
        descriptions.add(new TranslatableComponent("changed_addon.ability.unfuse.description.line2"));
        return descriptions;
    }


    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.unfuse");
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
        if (transfurVariantInstance != null) {
            ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();
            if (changedEntity instanceof ICoatLikeEntity iCoatLikeEntity) {
                Player host = transfurVariantInstance.getHost();
                entityUnfused = changedEntity.getType().create(host.getLevel());
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
                    changedEntityUnfused.setTarget(target);
                }
                if (host.getLevel() instanceof ServerLevel serverLevel) {
                    changedEntityUnfused.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(changedEntityUnfused.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                    serverLevel.addFreshEntity(changedEntityUnfused);
                    serverLevel.playSound(null, host, ChangedSounds.POISON, SoundSource.PLAYERS, 1, 1);
                    PlayerUtil.UnTransfurPlayer(host);
                }
            }
        }
    }
}
