package net.foxyas.changedaddon.process.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DEBUG {

    public static boolean test = false;

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        String msg = event.getMessage().trim().toLowerCase();
        Player player = event.getPlayer();

        if (!msg.equals("test")) return;
        player.displayClientMessage(new TextComponent("Executando teste de partículas em círculo..."), false);
        test = !test;

    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (test) {
            shootDebugParticles(player);
        }
    }

    private static void shootDebugParticles(LivingEntity target) {

        // Define o offset para o centro do círculo acima da cabeça do alvo
        Vec3 offset = new Vec3(0, 0, 0);
        double radius = 2.0;
        int count = 24;

        spawnParticlesInCircle(target.level, target.getEyePosition().add(0, 0.5,0), target, radius, count);
    }

    private static void spawnParticlesInCircle(Level level, Vec3 center, LivingEntity target, double radius, int count) {
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).zRot((float) angle).yRot((float) Math.toRadians(-target.getYRot())).normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            if (!level.isClientSide && level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.GLOW, spawnPos.x, spawnPos.y, spawnPos.z,
                        5, 0.05, 0.05, 0.05, 0.0); // 5 partículas com leve variação
            }
        }
    }
}
