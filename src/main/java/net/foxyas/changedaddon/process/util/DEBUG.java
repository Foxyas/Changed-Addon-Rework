package net.foxyas.changedaddon.process.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DEBUG {

    // Cabeça
    public static float HeadPosX = 0, HeadPosY = 0, HeadPosZ = 0;
    public static float HeadRotX = 0, HeadRotY = 0, HeadRotZ = 0;

    // Tronco
    public static float TorsoPosX = 0, TorsoPosY = 0, TorsoPosZ = 0;
    public static float TorsoRotX = 0, TorsoRotY = 0, TorsoRotZ = 0;

    // Braços
    public static float LeftArmPosX = 0, LeftArmPosY = 0, LeftArmPosZ = 0;
    public static float LeftArmRotX = 0, LeftArmRotY = 0, LeftArmRotZ = 0;

    public static float RightArmPosX = 0, RightArmPosY = 0, RightArmPosZ = 0;
    public static float RightArmRotX = 0, RightArmRotY = 0, RightArmRotZ = 0;

    // Pernas
    public static float LeftLegPosX = 0, LeftLegPosY = 0, LeftLegPosZ = 0;
    public static float LeftLegRotX = 0, LeftLegRotY = 0, LeftLegRotZ = 0;

    public static float RightLegPosX = 0, RightLegPosY = 0, RightLegPosZ = 0;
    public static float RightLegRotX = 0, RightLegRotY = 0, RightLegRotZ = 0;

    // Delta para debug de movimento
    public static float DeltaX = 0, DeltaY = 0, DeltaZ = 0;

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        String msg = event.getMessage().trim();

        if (msg.startsWith("set")) {
            String[] parts = msg.split(" ");
            if (parts.length != 2) return;

            String key = parts[0].replace("set", "");
            float value = (float) convert(parts[1]);

            applyValue(key, value);
            event.getPlayer().sendMessage(new TextComponent("§a[DEBUG] " + key + " = " + value), event.getPlayer().getUUID());
            event.setCanceled(true);
        }

        if (msg.equalsIgnoreCase("Show info")) {
            Player p = event.getPlayer();
            p.sendMessage(new TextComponent("§6[Head] Pos(" + HeadPosX + "," + HeadPosY + "," + HeadPosZ + ") Rot(" + HeadRotX + "," + HeadRotY + "," + HeadRotZ + ")"), p.getUUID());
            p.sendMessage(new TextComponent("§6[Torso] Pos(" + TorsoPosX + "," + TorsoPosY + "," + TorsoPosZ + ") Rot(" + TorsoRotX + "," + TorsoRotY + "," + TorsoRotZ + ")"), p.getUUID());
            p.sendMessage(new TextComponent("§6[LeftArm] Pos(" + LeftArmPosX + "," + LeftArmPosY + "," + LeftArmPosZ + ") Rot(" + LeftArmRotX + "," + LeftArmRotY + "," + LeftArmRotZ + ")"), p.getUUID());
            p.sendMessage(new TextComponent("§6[RightArm] Pos(" + RightArmPosX + "," + RightArmPosY + "," + RightArmPosZ + ") Rot(" + RightArmRotX + "," + RightArmRotY + "," + RightArmRotZ + ")"), p.getUUID());
            p.sendMessage(new TextComponent("§6[LeftLeg] Pos(" + LeftLegPosX + "," + LeftLegPosY + "," + LeftLegPosZ + ") Rot(" + LeftLegRotX + "," + LeftLegRotY + "," + LeftLegRotZ + ")"), p.getUUID());
            p.sendMessage(new TextComponent("§6[RightLeg] Pos(" + RightLegPosX + "," + RightLegPosY + "," + RightLegPosZ + ") Rot(" + RightLegRotX + "," + RightLegRotY + "," + RightLegRotZ + ")"), p.getUUID());
        }

        if (msg.equalsIgnoreCase("Show1")) {
            Player p = event.getPlayer();
            p.sendMessage(new TextComponent("§b[Delta] X=" + DeltaX + " Y=" + DeltaY + " Z=" + DeltaZ), p.getUUID());
        }
    }

    private static double convert(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception ignored) {
            return 0;
        }
    }

    private static void applyValue(String key, float value) {
        switch (key) {
            case "HeadPosX" -> HeadPosX = value;
            case "HeadPosY" -> HeadPosY = value;
            case "HeadPosZ" -> HeadPosZ = value;
            case "HeadRotX" -> HeadRotX = value;
            case "HeadRotY" -> HeadRotY = value;
            case "HeadRotZ" -> HeadRotZ = value;

            case "TorsoPosX" -> TorsoPosX = value;
            case "TorsoPosY" -> TorsoPosY = value;
            case "TorsoPosZ" -> TorsoPosZ = value;
            case "TorsoRotX" -> TorsoRotX = value;
            case "TorsoRotY" -> TorsoRotY = value;
            case "TorsoRotZ" -> TorsoRotZ = value;

            case "LeftArmPosX" -> LeftArmPosX = value;
            case "LeftArmPosY" -> LeftArmPosY = value;
            case "LeftArmPosZ" -> LeftArmPosZ = value;
            case "LeftArmRotX" -> LeftArmRotX = value;
            case "LeftArmRotY" -> LeftArmRotY = value;
            case "LeftArmRotZ" -> LeftArmRotZ = value;

            case "RightArmPosX" -> RightArmPosX = value;
            case "RightArmPosY" -> RightArmPosY = value;
            case "RightArmPosZ" -> RightArmPosZ = value;
            case "RightArmRotX" -> RightArmRotX = value;
            case "RightArmRotY" -> RightArmRotY = value;
            case "RightArmRotZ" -> RightArmRotZ = value;

            case "LeftLegPosX" -> LeftLegPosX = value;
            case "LeftLegPosY" -> LeftLegPosY = value;
            case "LeftLegPosZ" -> LeftLegPosZ = value;
            case "LeftLegRotX" -> LeftLegRotX = value;
            case "LeftLegRotY" -> LeftLegRotY = value;
            case "LeftLegRotZ" -> LeftLegRotZ = value;

            case "RightLegPosX" -> RightLegPosX = value;
            case "RightLegPosY" -> RightLegPosY = value;
            case "RightLegPosZ" -> RightLegPosZ = value;
            case "RightLegRotX" -> RightLegRotX = value;
            case "RightLegRotY" -> RightLegRotY = value;
            case "RightLegRotZ" -> RightLegRotZ = value;

            case "DeltaX" -> DeltaX = value;
            case "DeltaY" -> DeltaY = value;
            case "DeltaZ" -> DeltaZ = value;
        }
    }
}
