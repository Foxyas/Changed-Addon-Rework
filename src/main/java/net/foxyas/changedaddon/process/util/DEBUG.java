package net.foxyas.changedaddon.process.util;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packets.RequestMovementCheckPacket;
import net.foxyas.changedaddon.process.StructureHandle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;


@Mod.EventBusSubscriber
@Deprecated(forRemoval = true)
public class DEBUG {
    public static float HeadPosT, HeadPosV, HeadPosB, HeadPosK, HeadPosL, HeadPosJ = 10;
    public static float HeadPosX, HeadPosY, HeadPosZ = 1;

    public static boolean PARTICLETEST = false;
    public static int MOTIONTEST = 0;

    public static float DeltaX, DeltaY, DeltaZ = 0;
    public static String COLORSTRING = "#ffffff";

    @SubscribeEvent
    public static void debug(ServerChatEvent event) {
        if (event.getMessage().startsWith("testMotion")) {
            if (MOTIONTEST == 0) {
                MOTIONTEST = 1;
            } else if (MOTIONTEST == 1) {
                MOTIONTEST = 2;
            } else {
                MOTIONTEST = 0;
            }
        }

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
        if (event.getMessage().startsWith("setColor:")) {
            COLORSTRING = event.getMessage().replace("setColor:", "");
        }
        if (event.getMessage().startsWith("Show info")) {
            event.getPlayer().displayClientMessage(new TextComponent("X = " + HeadPosX + "\n" + "Y = " + HeadPosY + "\n" + "Z = " + HeadPosZ + "\n" + "T = " + HeadPosT + "\n" + "V = " + HeadPosV + "\n" + "B = " + HeadPosB + "\n" + "K = " + HeadPosK + "\n" + "L = " + HeadPosL + "\n" + "J = " + HeadPosJ), false);
        }
        if (event.getMessage().startsWith("Show1")) {
            event.getPlayer().displayClientMessage(new TextComponent("X = " + DeltaX + "\n" + "Y = " + DeltaY + "\n" + "Z = " + DeltaZ), false);
        }
        if (event.getMessage().startsWith("Show Info")) {
            new DelayedTask(40, () -> event.getPlayer().displayClientMessage(new TextComponent("X = " + StructureHandle.isStructureNearby(event.getPlayer().getLevel(), event.getPlayer().getOnPos(), "changed_addon:dazed_latex_meteor", 3)), false));
        }

    }

    @SubscribeEvent
    public static void PARTICLETEST(TickEvent.PlayerTickEvent event) {
        if (PARTICLETEST && event.player.isShiftKeyDown()) {
            PlayerUtil.ParticlesUtil.sendParticles(event.player.getLevel(), ParticleTypes.GLOW, event.player.getEyePosition().add(FoxyasUtils.getRelativePosition(event.player, DeltaX, DeltaY, DeltaZ, true)), 0f, 0f, 0f, 4, 0);
        }

        if (MOTIONTEST != 0) {
            Player player = event.player;
            if (MOTIONTEST == 1) {
                if (player.getLevel().isClientSide()) {
                    Vec3 motion = player.getDeltaMovement();
                    double speed = motion.length();
                    ChangedAddonMod.LOGGER.info("Client Player Speed is:{}", speed);
                }
            } else if (MOTIONTEST == 2) {
                if (!player.getLevel().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    /*Vec3 oldPos = new Vec3(player.xOld, player.yOld, player.zOld);
                    Vec3 playerPosition = player.position();
                    Vec3 posRelative = playerPosition.subtract(oldPos);
                    double fakeSpeed = posRelative.length();

                    ChangedAddonMod.LOGGER.info("Player Fake Speed is:{}", fakeSpeed);
                    ChangedAddonMod.LOGGER.info("Player Fake Vec Speed is:{}", posRelative);*/
                    ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new RequestMovementCheckPacket());
                }
            }
        }
        //Player player = event.player;
        //player.displayClientMessage(new TextComponent("Dot = " + DotValueOfViewProcedure.execute(player,player.getMainHandItem())), false);
    }


    private static double convert(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception ignored) {
        }
        return 0;
    }
}