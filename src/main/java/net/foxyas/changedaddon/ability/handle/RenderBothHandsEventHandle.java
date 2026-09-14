package net.foxyas.changedaddon.ability.handle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ability.CarryAbilityInstance;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mojang.math.Axis.*;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderBothHandsEventHandle {


    /**
     * 1.01 = Value Selected To DISABLE The handle
     * 1 = hidden hand;
     * 0 = visible hand;
     */

    public static final float DISABLE_HAND_SHOW_PROGRESS = 1.01f;
    public static final float VISIBLE_HAND_PROGRESS = 0f;
    private static boolean lock;

    private static float extraHandShowProgress = 1.01f;

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (lock) return;
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isScoping() || player.isInvisible()) return;

        boolean shouldRenderHand = ProcessTransfur.ifPlayerTransfurred(player, variant -> shouldRenderBothHands(player, variant), () -> {
            extraHandShowProgress = DISABLE_HAND_SHOW_PROGRESS;
            return false;
        });

        if (!shouldRenderHand) {
            extraHandShowProgress = Mth.clamp(extraHandShowProgress + 0.005f, 0, DISABLE_HAND_SHOW_PROGRESS);
            if (extraHandShowProgress >= DISABLE_HAND_SHOW_PROGRESS) {
                return;
            }
        } else {
            extraHandShowProgress = Mth.clamp(extraHandShowProgress - 0.005f, 0, DISABLE_HAND_SHOW_PROGRESS);
        }

        if (shouldShow()) {
            PoseStack stack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int light = event.getPackedLight();
            float partialTicks = event.getPartialTick(); //Useless for now
            float equipProgress = extraHandShowProgress <= VISIBLE_HAND_PROGRESS ? event.getEquipProgress() : extraHandShowProgress; // event.getEquipProgress();

            EntityRenderer<? super LivingEntity> entRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
            if (entRenderer instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                    lock = true;
                    if (event.getHand() == InteractionHand.MAIN_HAND) {
                        stack.pushPose();
                        boolean rightHand = player.getMainArm() == HumanoidArm.RIGHT;

                        float f = rightHand ? -1.0F : 1.0F;
                        float pSwingProgress = event.getSwingProgress();
                        float f1 = Mth.sqrt(pSwingProgress);
                        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
                        float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
                        float f4 = -0.4F * Mth.sin(pSwingProgress * (float) Math.PI);

                        stack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equipProgress * -0.6F, f4 + -0.71999997F);// 0 here is an inaccessible variable from ItemInHandRenderer
                        stack.mulPose(YP.rotationDegrees(f * 45.0F));
                        float f5 = Mth.sin(pSwingProgress * pSwingProgress * (float) Math.PI);
                        float f6 = Mth.sin(f1 * (float) Math.PI);
                        stack.mulPose(YP.rotationDegrees(f * f6 * 70.0F));
                        stack.mulPose(ZP.rotationDegrees(f * f5 * -20.0F));
                        stack.translate(f * -1.0F, 3.6F, 3.5D);
                        stack.mulPose(ZP.rotationDegrees(f * 120.0F));
                        stack.mulPose(XP.rotationDegrees(200.0F));
                        stack.mulPose(YP.rotationDegrees(f * -135.0F));
                        stack.translate(f * 5.6F, 0.0D, 0.0D);
                        if (rightHand) {
                            if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                                playerRenderer.renderLeftHand(stack, buffer, light, player);
                            }
                        } else {
                            if (player.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                                playerRenderer.renderRightHand(stack, buffer, light, player);
                            }
                        }
                        stack.popPose();
                    }
                    lock = false;
                }
            }
        }
    }

    private static boolean shouldShow() {
        return extraHandShowProgress < DISABLE_HAND_SHOW_PROGRESS;
    }

    public static boolean shouldRenderBothHands(Player player, TransfurVariantInstance<?> variantInstance) {
        if (variantInstance == null) {
            return false;
        }

        if (!ChangedAddonClientConfiguration.SHOW_EXTRA_HAND.get()) {
            extraHandShowProgress = DISABLE_HAND_SHOW_PROGRESS;
            return false;
        }

        // Check if the player is gliding with a variant that can glide and has sufficient speed
        if (variantInstance.canElytraGlide() && player.isFallFlying()) {
            double speed = player.getDeltaMovement().length(); // Velocidade do jogador
            if (speed > 1.5 || player.getFallFlyingTicks() >= 5) {
                return true;
            }
        }

        // Check if the player has the Carry ability and is carrying a valid entity
        if (variantInstance.hasAbility(ChangedAddonAbilities.CARRY.get())
                && variantInstance.selectedAbility == ChangedAddonAbilities.CARRY.get()) {
            Entity carryTarget = CarryAbilityInstance.carryTarget(player);

            if (carryTarget instanceof LivingEntity) {
                boolean isHumanoidOrTagValid = carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) ||
                        carryTarget.getType().is(ChangedAddonTags.EntityTypes.CAN_CARRY);

                return isHumanoidOrTagValid && (player.getFirstPassenger() == null || player.getFirstPassenger() != carryTarget);
            }
        }

        return false;
    }
}