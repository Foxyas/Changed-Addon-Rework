package net.foxyas.changedaddon.process.features;

public class ClientPatState {
    public static boolean patting = false;
    public static float patSpeed = 1.0f;

    // Persistent animation timeline counter
    public static float animTicks = 0.0f;

    // Call this inside your ClientTickEvent (TickEvent.Phase.END)
    public static void clientTick() {
        if (patting) {
            // Increment by current speed every tick (1.0 = normal 20 tps speed)
            animTicks += Math.max(0.01f, patSpeed);
        } else {
            animTicks = 0.0f; // Reset when not patting
        }
    }
}
