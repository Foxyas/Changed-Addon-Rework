package net.foxyas.changedaddon.process;

import net.foxyas.changedaddon.process.util.DelayedTask;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DEBUG {
    public static float HeadPosT,HeadPosV,HeadPosB,HeadPosK,HeadPosL,HeadPosJ = 10;
    public static float HeadPosX,HeadPosY,HeadPosZ = 1;
    public static String COLORSTRING = "#ffffff";

    @SubscribeEvent
    public static void DEBUG(ServerChatEvent event){
        if (event.getMessage().startsWith("setHeadPos")){
            String a = event.getMessage().replace("setHeadPos","");
            if (a.startsWith("T")){
                HeadPosT =  (float) convert(a.replace("T",""));
            } else if (a.startsWith("V")){
                HeadPosV =  (float) convert(a.replace("V",""));
            } else if (a.startsWith("B")){
                HeadPosB =  (float) convert(a.replace("B",""));
            }
            if (a.startsWith("k")){
                HeadPosK =  (float) convert(a.replace("k",""));
            } else if (a.startsWith("l")){
                HeadPosL =  (float) convert(a.replace("l",""));
            } else if (a.startsWith("j")){
                HeadPosL =  (float) convert(a.replace("j",""));
            }
            if (a.startsWith("x")){
                HeadPosX =  (float) convert(a.replace("x",""));
            } else if (a.startsWith("y")){
                HeadPosY =  (float) convert(a.replace("y",""));
            } else if (a.startsWith("z")){
                HeadPosZ =  (float) convert(a.replace("z",""));
            }
        }
        if (event.getMessage().startsWith("setColor:")){
            COLORSTRING = event.getMessage().replace("setColor:","");
        }
        if (event.getMessage().startsWith("Show info")){
            event.getPlayer().displayClientMessage(new TextComponent("X = " + HeadPosX + "\n" + "Y = " + HeadPosY + "\n" + "Z = " + HeadPosZ + "\n" + "T = " + HeadPosT + "\n" + "V = " + HeadPosV + "\n" + "B = " + HeadPosB + "\n" + "K = " + HeadPosK + "\n" + "L = " + HeadPosL + "\n" + "J = " + HeadPosJ),false);
        }
        if (event.getMessage().startsWith("Show Info")){
            new DelayedTask(40, event.getPlayer(), (livingEntity) -> {
                if (livingEntity instanceof Player player){
                    player.displayClientMessage(new TextComponent("X = " + StructureHandle.isStructureNearby(event.getPlayer().getLevel(),event.getPlayer().getOnPos(),"changed_addon:dazed_latex_meteor", 3)), false);
                }
            });
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