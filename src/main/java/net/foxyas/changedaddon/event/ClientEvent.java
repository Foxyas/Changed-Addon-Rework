package net.foxyas.changedaddon.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.ChangedAdditionsModConflictWarningScreen;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.command.ChangedAddonClientCommands;
import net.foxyas.changedaddon.init.ChangedAddonKeyMappings;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.process.features.ClientPatState;
import net.foxyas.changedaddon.process.sounds.BossMusicHandler;
import net.foxyas.changedaddon.util.TransfurVariantUtils;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.item.LatexTippedArrowItem;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.item.VariantHoldingBase;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

import static com.mojang.math.Axis.*;
import static com.mojang.math.Axis.YP;
import static net.foxyas.changedaddon.event.ClientMod.changedAdditionsLoaded;
import static net.foxyas.changedaddon.event.ClientMod.changedAdditionsWarningScreenShowed;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onSetScreen(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof TitleScreen) {
            if (changedAdditionsLoaded && !changedAdditionsWarningScreenShowed) {
                event.setNewScreen(new ChangedAdditionsModConflictWarningScreen());
            }
        }
    }

    @SubscribeEvent
    public static void onNameFormat(RenderNameTagEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (variantInstance instanceof TransfurVariantInstanceExtensor instanceExtensor) {
                if (!instanceExtensor.hasControlOverBody()) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void animateHandForPatting(RenderHandEvent event) {
        if (ModList.get().isLoaded("changed_synergy")) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.level() == null) return;

        KeyMapping patKey = ChangedAddonKeyMappings.PAT_KEY;
        boolean patting = ClientPatState.patting;

        if (patting && event.getHand() == InteractionHand.MAIN_HAND) {
            event.setCanceled(true);
            PoseStack stack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            int light = event.getPackedLight();
            float partialTicks = event.getPartialTick();
            float equipProgress = event.getEquipProgress();

            manuallyRenderFirstPersonHand(player, stack, equipProgress, partialTicks, buffer, light);
        }
    }

    private static void manuallyRenderFirstPersonHand(LocalPlayer player, PoseStack stack, float equipProgress, float partialTicks, MultiBufferSource buffer, int light) {
        EntityRenderer<? super LivingEntity> entRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (entRenderer instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
            if (livingEntityRenderer instanceof PlayerRenderer playerRenderer) {
                stack.pushPose();
                boolean rightHand = player.getMainArm() == HumanoidArm.RIGHT;

                float f = rightHand ? 1.0F : -1.0F;
                float pSwingProgress = 0; // event.getSwingProgress();
                float f1 = Mth.sqrt(pSwingProgress);
                float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
                float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
                float f4 = -0.4F * Mth.sin(pSwingProgress * (float) Math.PI);

                stack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equipProgress * -0.6F, f4 + -0.71999997F);
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

                applyPatHandTransformation(stack, partialTicks);

                if (rightHand) {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                        playerRenderer.renderRightHand(stack, buffer, light, player);
                    }
                } else {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                        playerRenderer.renderLeftHand(stack, buffer, light, player);
                    }
                }
                stack.popPose();
            }
        }
    }

    private static void applyPatHandTransformation(PoseStack poseStack, float partialTick) {
        float patSpeed = Math.max(0.01F, ClientPatState.patSpeed);

        // Base cycle duration (12 ticks at standard 1.0 speed)
        float baseCycleTicks = 12.0F;

        // Smooth phase calculation using the accumulator + partial tick interpolation
        float elapsed = ClientPatState.animTicks + (partialTick * patSpeed);
        float phase = (elapsed % baseCycleTicks) / baseCycleTicks;

        // Waveform calculations
        float sweep = Mth.sin(phase * (float) Math.PI * 2.0F);
        float lift = 0.5F - 0.5F * Mth.cos(phase * (float) Math.PI * 2.0F);

        // Matrix transformations
        poseStack.translate(0.036F * sweep, -0.012F * lift, -0.04F * lift);
        poseStack.mulPose(Axis.XP.rotationDegrees(-3.0F * lift));
        poseStack.mulPose(Axis.YP.rotationDegrees(2.2F * sweep));
        poseStack.mulPose(Axis.ZP.rotationDegrees(6.0F * sweep));
    }

//    private static final List<String> FULLBRIGHTS = Util.make(new ArrayList<>(), list -> {
//      // Add The list of models ids here;
//      // You can just leave the # in the end to tell "any layer".
//      // list.add(ChangedAddonMod.layerLocation("example", "main").toString());
//      // list.add("changed_addon:example#main");
//      // list.add("changed_addon:example#");
//    });
//
//    @SubscribeEvent
//    public static void bakeModels(ModifyBakingResult e) {
//        long time = System.currentTimeMillis();
//        for (ResourceLocation id : e.getModels().keySet()) {
//            if (FULLBRIGHTS.stream().anyMatch(str -> id.toString().startsWith(str)) || id.toString().contains("_light_emission")) {
//                e.getModels().put(id, new BakedModelShadeLayerFullbright(e.getModels().get(id)));
//            } else if (BlocksLightEmissionRegistry.getLightEmission(id) > 0) {
//                e.getModels().put(id, new BakedModelShadeLayerDynamicBright(e.getModels().get(id), BlocksLightEmissionRegistry.getLightEmission(id)));
//            }
//        }
//        if (!FULLBRIGHTS.isEmpty()) {
//            ChangedAddonMod.LOGGER.info("Loaded emissive block models in {} ms", System.currentTimeMillis() - time);
//        }
//    }

    @SubscribeEvent
    public void onRenderEntityPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity livingEntity = EntityUtil.maybeGetUnderlying(event.getEntity());
        if (livingEntity == null) {
            return;
        }

        DamageSource lastDamageSource = livingEntity.getLastDamageSource();
        if (livingEntity.isDeadOrDying() && lastDamageSource != null) {
            if (lastDamageSource.is(ChangedAddonTags.DamageTypes.HIDE_ON_DEATH)) {
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void registerClientSideCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();

        ChangedAddonClientCommands.registerClientCommand(event);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.phase == TickEvent.Phase.END && minecraft.level != null) {
            BossMusicHandler.tick(minecraft.level);
            SonarOutlineLayer.SonarClientState.tick();
            ClientPatState.clientTick();
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        showExtraTransfurInfo(event.getEntity(), stack, tooltip);

        if (stack.getItem() instanceof VariantHoldingBase) {
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("safe")) {
                Component comp = Component.translatable("tooltip.changed_addon.latex_syringe.purified").withStyle(Style.EMPTY.applyFormats(ChatFormatting.ITALIC, ChatFormatting.YELLOW));

//                boolean itAdded = false;
//                for (int i = 0; i < tooltip.size(); i++) {
//                    Component component = tooltip.get(i);
//                    ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
//                    if (registryName == null) {
//                        break;
//                    }
//
//                    boolean contains = component.toString().contains(registryName.toString());
//                    if (contains) {
//                        // BEFORE ID
//                        tooltip.add(i, comp);
//                        itAdded = true;
//                        break;
//                    }
//                }

                int index;
                if (tooltip.size() < 3) {
                    index = 1;
                } else if (stack.getItem() instanceof LatexTippedArrowItem) {
                    index = 2;
                } else {
                    index = tooltip.size() > 3 ? 3 : tooltip.size() - 1;
                }

                ResourceLocation loc = ResourceLocation.tryParse(stack.getOrCreateTag().getString("form"));
                TransfurVariant<?> tf;
                try {
                    tf = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
                } catch (Exception e) {
                    tf = null;
                }

                if (loc == null || tf == null) {
                    index -= 1;
                }

                tooltip.add(index, comp);
            }
        }
    }

    public static void showExtraTransfurInfo(@Nullable Player player, ItemStack itemstack, List<Component> tooltip) {
        if (player == null || itemstack == null || tooltip == null || !Minecraft.getInstance().isSameThread()) return;
        if (!(itemstack.is(ChangedItems.LATEX_SYRINGE.get()) || itemstack.is(ChangedItems.LATEX_FLASK.get())
                || itemstack.is(ChangedItems.LATEX_TIPPED_ARROW.get()))) return;

        ResourceLocation loc = ResourceLocation.tryParse(itemstack.getOrCreateTag().getString("form"));
        if (loc == null) return;

        TransfurVariant<?> tf = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
        if (tf == null) return;

        try {
            //boolean hasInformantBlock = player.getInventory().contains(new ItemStack(ChangedAddonModBlocks.INFORMANTBLOCK.get()));

//        if (hasInformantBlock || isCreative) {
//            if (hasInformantBlock && !Screen.hasShiftDown()) {
//                String variantName = Component.translatable(Syringe.getVariantDescriptionId(itemstack)).getString();
//                tooltip.add(Component.literal("Hold ").append(Component.literal("<Shift>").withStyle(style -> style.withColor(0xFFD700)))
//                        .append(" to show the stats of the " + variantName + " Transfur"));
//            }

            if (player.isCreative()) {
                if (!Screen.hasShiftDown()) {
                    String variantName = Component.translatable(Syringe.getVariantDescriptionId(itemstack)).getString();
                    tooltip.add(Component.translatable("item.changed_addon.latex_syringe.tooltip", variantName));
                } else {
                    int index = Math.min(tooltip.size(), 3);
                    TransfurVariantInstance<?> instance = TransfurVariantInstance.variantFor(tf, player);

                    float extraHp = TransfurVariantUtils.getExtraHpOfVariantBasedOnPlayer(tf, player) / 2f;
                    MutableComponent displayExtraHp = extraHp == 0
                            ? Component.literal("§7None§r")
                            : Component.literal((extraHp > 0 ? "§a+" : "§c") + extraHp + "§r");
                    tooltip.add(index, Component.translatable("text.changed_addon.additionalHealth", displayExtraHp).append(Component.translatable("text.changed_addon.additionalHealth.Hearts")));

                    index++;
                    String miningStrengthOfVariant = TransfurVariantUtils.getMiningStrengthOfVariant(tf, player);
                    tooltip.add(index, Component.translatable("text.changed_addon.miningStrength", miningStrengthOfVariant));

                    index++;
                    float landSpeed = TransfurVariantUtils.getLandSpeedOfVariantBasedOnPlayer(tf, player);
                    float landSpeedPct = landSpeed == 0 ? 0 : (landSpeed - 1) * 100;
                    MutableComponent displayLandSpeedPct = landSpeedPct == 0
                            ? Component.literal("§7None§r")
                            : Component.literal((landSpeedPct > 0 ? "§a+" : "§c") + (int) landSpeedPct + "%");
                    tooltip.add(index, Component.translatable("text.changed_addon.land_speed", displayLandSpeedPct));

                    index++;
                    float swimSpeed = TransfurVariantUtils.getSwimSpeedOfVariantBasedOnPlayer(tf, player);
                    float swimSpeedPct = swimSpeed == 0 ? 0 : (swimSpeed - 1) * 100;
                    MutableComponent displaySwimSpeedPct = swimSpeedPct == 0
                            ? Component.literal("§7None§r")
                            : Component.literal((swimSpeedPct > 0 ? "§a+" : "§c") + (int) swimSpeedPct + "%");
                    tooltip.add(index, Component.translatable("text.changed_addon.swim_speed", displaySwimSpeedPct));

                    index++;
                    float jumpStrength = TransfurVariantUtils.GetJumpStrength(tf, player);
                    float jumpStrengthPct = jumpStrength == 0 ? 0 : (jumpStrength - 1) * 100;
                    MutableComponent displayJumpStrengthPct = jumpStrengthPct == 0
                            ? Component.literal("§7None§r")
                            : Component.literal((jumpStrengthPct > 0 ? "§a+" : "§c") + (int) jumpStrengthPct + "%");
                    tooltip.add(index, Component.translatable("text.changed_addon.jumpStrength", displayJumpStrengthPct));

                    index++;
                    MutableComponent displayCanGlide = TransfurVariantUtils.canVariantGlide(instance)
                            ? Component.literal("§aTrue§r")
                            : Component.literal("§cFalse§r");
                    tooltip.add(index, Component.translatable("text.changed_addon.canElytraGlide", displayCanGlide));

                    index++;
                    MutableComponent displayCanCreativeFly = TransfurVariantUtils.canVariantFly(instance)
                            ? Component.literal("§aTrue§r")
                            : Component.literal("§cFalse§r");
                    tooltip.add(index, Component.translatable("text.changed_addon.canCreativeFly", displayCanCreativeFly));
                }

                if (ChangedAddonTransfurVariants.isVariantOC(loc, player.level())) {
                    List<Component> ocVariantComponents = ChangedAddonTransfurVariants.getVariantComponentIfAny(tf, player.level());
                    MutableComponent append = Component.literal("§8OC Transfur");
                    tooltip.add(append);
                    if (!ocVariantComponents.isEmpty()) {
                        tooltip.addAll(ocVariantComponents);
                    }
                }
            }

            if (ChangedAddonTransfurVariants.isBossVariant(tf)) {
                tooltip.add(Component.literal("§8Boss Version"));
            }
        } catch (Exception ignored) {

        }
    }
}
