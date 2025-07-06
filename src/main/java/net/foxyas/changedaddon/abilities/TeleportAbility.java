package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.Random;

public class TeleportAbility extends SimpleAbility {
    public TeleportAbility() {
        super();
    }

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.teleport");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
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
            BlockHitResult blockHitResult = player.getLevel().clip(new ClipContext(player.getEyePosition(), eyeLocation, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            Vec3 location = blockHitResult.getLocation();
            Color startColor = new Color(0xffeeee);
            Color endColor = new Color(0xFFCECE);
            PlayerUtilProcedure.ParticlesUtil.sendColorTransitionParticles(player.getLevel(), player, startColor, endColor, 1, 0.25f, 0.25f, 0.25f, 10, 0.25f);
            player.teleportToWithTicket(location.x, location.y, location.z);
            Random random = entity.getLevel().getRandom();
            float pitch = random.nextFloat() + 1;
			float volume = 0.5f;
            player.getLevel().playSound(null, player, SoundEvents.FOX_TELEPORT, SoundSource.MASTER, volume, pitch);
        }
    }

    @Override
    public void onRemove(IAbstractChangedEntity entity) {
        super.onRemove(entity);
    }

    public static boolean Spectator(Entity entity) {
        return entity instanceof Player player && player.isSpectator();
    }
}
