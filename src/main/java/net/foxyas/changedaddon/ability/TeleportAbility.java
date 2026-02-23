package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.AbstractKitsuneEntity;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class TeleportAbility extends SimpleAbility {
    public TeleportAbility() {
        super();
    }

    public static boolean Spectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.teleport");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/dodge_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 30;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        return 15;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return !Spectator(entity.getEntity());
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);

        LivingEntity living = entity.getEntity();
        if (living instanceof Player player) {
            Vec3 eyeLocation = FoxyasUtils.getRelativePositionEyes(player, 0f, 0f, 16f);
            BlockHitResult blockHitResult = player.level().clip(new ClipContext(player.getEyePosition(), eyeLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            Vec3 location = blockHitResult.getLocation();
            player.teleportToWithTicket(location.x, location.y, location.z);
            player.causeFoodExhaustion(4f);

            if (entity.getChangedEntity() instanceof AbstractKitsuneEntity) {
                applyKitsuneTeleportEffects(entity, player);
            } else {
                Level levelAccessor = player.level();
                levelAccessor.playSound(null, player.blockPosition(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (levelAccessor instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            player.getX(), player.getY() + 0.5, player.getZ(),
                            20, 0.5, 1.0, 0.5, 0.1);
                }
            }
        }
    }

    private void applyKitsuneTeleportEffects(IAbstractChangedEntity entity, Player player) {
        Color startColor = new Color(0xffeeee);
        Color endColor = new Color(0xFFCECE);
        ParticlesUtil.sendColorTransitionParticles(player.level(), player, startColor, endColor, 1, 0.25f, 0.25f, 0.25f, 10, 0.25f);
        RandomSource random = entity.getLevel().getRandom();
        float pitch = random.nextFloat() + 1;
        float volume = 0.5f;
        player.level().playSound(null, player, SoundEvents.FOX_TELEPORT, SoundSource.MASTER, volume, pitch);
    }

    @Override
    public void onRemove(IAbstractChangedEntity entity) {
        super.onRemove(entity);
    }
}
