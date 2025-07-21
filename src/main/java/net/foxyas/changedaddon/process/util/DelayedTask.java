package net.foxyas.changedaddon.process.util;


import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DelayedTask {

    private static final Int2ObjectOpenHashMap<DelayedTask> activeTasks = new Int2ObjectOpenHashMap<>();
    private static int nextId = 0;

    private final int id;
    private final int delayTicks;
    private final Runnable task;
    private int currentTick = 0;
    private boolean paused = false;
    private boolean cancelled = false;

    static {
        MinecraftForge.EVENT_BUS.register(DelayedTask.class);
    }

    // Event to update this instance only
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            activeTasks.values().forEach(DelayedTask::tick);
        }
    }

    public DelayedTask(int delayTicks, Runnable task){
        this.delayTicks = delayTicks;
        this.task = task;

        this.id = nextId++;
        activeTasks.put(id, this);
    }

    public void tick() {
        if (paused) return;
        if(isCancelled()) {// Ensure the instance is removed correctly
            destroy();
            return;
        }

        currentTick++;
        if (currentTick < delayTicks) return;

        try {
            task.run();
        } catch (Exception e) {
            System.err.println("Error during the execution of the DelayedTask with ID: " + id + "\n " + e.getMessage());
            //e.printStackTrace();
        } finally {
            cancel();// Automatically cancel after execution
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
    }

    /**
    * This is to pause the execution of the task
    */
    public void pause() {
        this.paused = true;
        ChangedAddonMod.LOGGER.info("DelayedTask with ID: {} was paused by an external code", id);
    }

    /**
     * This is to resume the execution of the task
     */
    public void resume() {
        this.paused = false;
        ChangedAddonMod.LOGGER.info("DelayedTask with ID: {} was resumed by an external code", id);
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
