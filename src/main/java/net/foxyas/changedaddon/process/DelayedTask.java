package net.foxyas.changedaddon.process;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class DelayedTask {
    private int ticksRemaining;
    private final LivingEntity entity;
    private final Consumer<LivingEntity> action;

    public DelayedTask(int ticks, LivingEntity entity, Consumer<LivingEntity> action) {
        this.ticksRemaining = ticks;
        this.entity = entity;
        this.action = action;
        MinecraftForge.EVENT_BUS.register(this); // Registra a instância
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticksRemaining--;

            if (ticksRemaining <= 0) {
                try {
                    action.accept(entity); // Executa a ação
                } catch (Exception e) {
                    System.err.println("Erro ao executar DelayedTask: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    MinecraftForge.EVENT_BUS.unregister(this); // Garante que a task seja removida
                }
            }
        }
    }
}
