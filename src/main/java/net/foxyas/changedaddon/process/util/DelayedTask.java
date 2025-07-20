package net.foxyas.changedaddon.process.util;


import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DelayedTask {

    private static final Map<Integer, DelayedTask> activeTasks = new HashMap<>();
    private static int nextId = 0;

    private final int id;
    private final int delayTicks;
    private final LivingEntity entity;
    private final Consumer<LivingEntity> action;
    private int currentTick;
    private boolean paused;
    private boolean cancelled;

    public DelayedTask(int delayTicks, LivingEntity entity, Consumer<LivingEntity> action) {
        this.id = nextId++;
        this.delayTicks = delayTicks;
        this.entity = entity;
        this.action = action;
        this.currentTick = 0;
        this.paused = false;
        this.cancelled = false;

        activeTasks.put(id, this);
        MinecraftForge.EVENT_BUS.register(this); // Automatic registration of the instance with ForgeEventBus
    }

    // Event to update this instance only
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick();
        }
    }

    public void tick() {
        if (paused) return;

        try {
            currentTick++;
            if (currentTick >= delayTicks) {
                action.accept(this.entity); // The Void Will return they entity as a dependence
                cancel(); // Automatically cancel after execution
            }
        } catch (Exception e) {
            System.err.println("Erro in the execution  of the DelayedTask with ID: " + id + "\n " + e.getMessage());
            //e.printStackTrace();
        } finally {
            if (isCancelled()) {// <-- in case of exception cancel() might not get called
                destroy(); // Ensure the instance is removed correctly
            }
        }
    }

    /**
     * This is To Cancel The Action of the DelayedTask, Keep in mind that this will not destroy it from memory instantly
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * This is To Remove the DelayedTask from memory, Keep in Mind that this action can't be reversed
     */
    private void destroy() {
        activeTasks.remove(id);
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    /**
    * This is to pause the execution of the task
    */
    public void pause() {
        this.paused = true;
        ChangedAddonMod.LOGGER.info("DelayedTask with ID: " + id + " was paused by an external code");
    }

    /**
     * This is to resume the execution of the task
     */
    public void resume() {
        this.paused = false;
        ChangedAddonMod.LOGGER.info("DelayedTask with ID: " + id + " was resumed by an external code");
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public int getId() {
        return id;
    }

    // Métodos estáticos para acessar as tarefas
    public static DelayedTask getTask(int id) {
        return activeTasks.get(id);
    }

    public static void cancelTask(int id) {
        DelayedTask task = activeTasks.get(id);
        if (task != null) {
            task.cancel();
        }
    }

    public static void pauseTask(int id) {
        DelayedTask task = activeTasks.get(id);
        if (task != null) {
            task.pause();
        }
    }

    public static void resumeTask(int id) {
        DelayedTask task = activeTasks.get(id);
        if (task != null) {
            task.resume();
        }
    }

    public static boolean isTaskPaused(int id) {
        DelayedTask task = activeTasks.get(id);
        return task != null && task.isPaused();
    }

}
