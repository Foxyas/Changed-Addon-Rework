package net.foxyas.changedaddon.process;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.CorrectorType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class DEBUG {
    public static float HeadPosX,HeadPosY,HeadPosZ,HeadPosT,HeadPosV,HeadPosB,HeadPosK,HeadPosL,HeadPosJ = 0;
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
        if (event.getMessage().startsWith("Show info")){
            event.getPlayer().displayClientMessage(new TextComponent("X = " + HeadPosX + "\n" + "Y = " + HeadPosY + "\n" + "Z = " + HeadPosZ + "\n" + "T = " + HeadPosT + "\n" + "V = " + HeadPosV + "\n" + "B = " + HeadPosB + "\n" + "K = " + HeadPosK + "\n" + "L = " + HeadPosL + "\n" + "J = " + HeadPosJ),false);
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