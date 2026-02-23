package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VariantEvents {

    @SubscribeEvent
    public static void entityJumpWithVariant(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.onGround()
                || !player.level.isClientSide || !(player.level instanceof ClientLevel clientLevel)) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return;

        if (!instance.hasAbility(ChangedAddonAbilities.WIND_CONTROL.get()) && !instance.hasAbility(ChangedAddonAbilities.WIND_PASSIVE.get()))
            return;

        RandomSource random = player.getRandom();
        Vec3 motion = player.getDeltaMovement();
        double x, y, z;
        for (int i = 0; i < 4; i++) {
            x = 2 * random.nextDouble() - 1;
            y = 2 * random.nextDouble() - 1;
            z = 2 * random.nextDouble() - 1;

            clientLevel.addParticle(ParticleTypes.POOF,
                    player.position().x + x,
                    player.position().y + y,
                    player.position().z + z,
                    motion.x(),
                    motion.y(),
                    motion.z()
            );
        }
    }
}
