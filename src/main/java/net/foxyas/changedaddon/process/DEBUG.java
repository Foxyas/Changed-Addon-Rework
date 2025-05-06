package net.foxyas.changedaddon.process;

import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.foxyas.changedaddon.process.util.ModelUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.foxyas.changedaddon.procedures.PlayerUtilProcedure.GlobalEntityUtil.getEntityByUUID;

@Mod.EventBusSubscriber
public class DEBUG {
    public static float HeadPosT, HeadPosV, HeadPosB, HeadPosK, HeadPosL, HeadPosJ = 0;
    public static float HeadPosX, HeadPosY, HeadPosZ = 0;

    public static boolean PARTICLETEST = false;
    public static float DeltaX, DeltaY, DeltaZ = 0;
    public static String ENTITYUUID = "";

    @SubscribeEvent
    public static void DEBUG(ServerChatEvent event) {
        if (event.getMessage().startsWith("testDeltas")) {
            PARTICLETEST = !PARTICLETEST;
        } else if (event.getMessage().startsWith("setDeltaPos")) {
            String a = event.getMessage().replace("setDeltaPos", "");
            if (a.startsWith("x")) {
                DeltaX = (float) convert(a.replace("x", ""));
            } else if (a.startsWith("y")) {
                DeltaY = (float) convert(a.replace("y", ""));
            } else if (a.startsWith("z")) {
                DeltaZ = (float) convert(a.replace("z", ""));
            }
        }
        if (event.getMessage().startsWith("setHeadPos")) {
            String a = event.getMessage().replace("setHeadPos", "");
            if (a.startsWith("T")) {
                HeadPosT = (float) convert(a.replace("T", ""));
            } else if (a.startsWith("V")) {
                HeadPosV = (float) convert(a.replace("V", ""));
            } else if (a.startsWith("B")) {
                HeadPosB = (float) convert(a.replace("B", ""));
            }
            if (a.startsWith("k")) {
                HeadPosK = (float) convert(a.replace("k", ""));
            } else if (a.startsWith("l")) {
                HeadPosL = (float) convert(a.replace("l", ""));
            } else if (a.startsWith("j")) {
                HeadPosJ = (float) convert(a.replace("j", ""));
            }
            if (a.startsWith("x")) {
                HeadPosX = (float) convert(a.replace("x", ""));
            } else if (a.startsWith("y")) {
                HeadPosY = (float) convert(a.replace("y", ""));
            } else if (a.startsWith("z")) {
                HeadPosZ = (float) convert(a.replace("z", ""));
            }
        }
        if (event.getMessage().startsWith("setEntity:")) {
            ENTITYUUID = event.getMessage().replace("setEntity:", "");
        }
        if (event.getMessage().startsWith("Show info")) {
            event.getPlayer().displayClientMessage(new TextComponent("X = " + HeadPosX + "\n" + "Y = " + HeadPosY + "\n" + "Z = " + HeadPosZ + "\n" + "T = " + HeadPosT + "\n" + "V = " + HeadPosV + "\n" + "B = " + HeadPosB + "\n" + "K = " + HeadPosK + "\n" + "L = " + HeadPosL + "\n" + "J = " + HeadPosJ), false);
        }
        if (event.getMessage().startsWith("Show1")) {
            event.getPlayer().displayClientMessage(new TextComponent("X = " + DeltaX + "\n" + "Y = " + DeltaY + "\n" + "Z = " + DeltaZ), false);
        }
        if (event.getMessage().startsWith("Show Info")) {
            new DelayedTask(40, event.getPlayer(), (livingEntity) -> {
                if (livingEntity instanceof Player player) {
                    player.displayClientMessage(new TextComponent("X = " + StructureHandle.isStructureNearby(event.getPlayer().getLevel(), event.getPlayer().getOnPos(), "changed_addon:dazed_latex_meteor", 3)), false);
                }
            });
        }
    }

    public static void addOffset(int keyCode, Player player) {
        boolean shift = player.isShiftKeyDown();

        switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> {
                HeadPosZ += shift ? 0.5f : 0;
                HeadPosY += shift ? 0 : 0.5f;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                HeadPosZ += shift ? -0.5f : 0;
                HeadPosY += shift ? 0 : -0.5f;
            }
            case GLFW.GLFW_KEY_LEFT -> HeadPosX += 0.5f;
            case GLFW.GLFW_KEY_RIGHT -> HeadPosX += -0.5f;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.CLIENT) {
            Player player = event.player;
            Entity entity = getEntityByUUID(player.level, ENTITYUUID);
            var model = ModelUtils.getModelOf(entity);
            if (!(model instanceof HumanoidModel<?> humanoidModel)) {
                return;
            }
            Vec3 pos;
            pos = ModelUtils.getWorldPositionFromModelPart(player.getMainArm() == HumanoidArm.RIGHT ? humanoidModel.rightArm : humanoidModel.leftArm, new Vector3f(HeadPosX, HeadPosY, HeadPosZ), player, new Vec3(HeadPosT, HeadPosV, HeadPosB), new Vec3(HeadPosK, HeadPosL, HeadPosJ), false);
            player.displayClientMessage(new TextComponent(pos.toString()), true);

            if (player.level.random.nextFloat() >= 0) {
                player.level.addParticle(ParticleTypes.ELECTRIC_SPARK,
                        pos.x, pos.y, pos.z,
                        DeltaX, DeltaY, DeltaZ);
            }

        }
    }


    private static double convert(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
        }
        return 0;
    }
}