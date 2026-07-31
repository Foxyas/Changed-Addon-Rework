package net.foxyas.changedaddon.event;

import com.mojang.brigadier.CommandDispatcher;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.ChangedAdditionsModConflictWarningScreen;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.command.ChangedAddonClientCommands;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.process.sounds.BossMusicHandler;
import net.foxyas.changedaddon.util.TransfurVariantUtils;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

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
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        showExtraTransfurInfo(event.getEntity(), stack, tooltip);

        if (stack.is(ChangedItems.LATEX_SYRINGE.get())) {
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("safe")) {
                tooltip.set(3, Component.translatable("tooltip.changed_addon.latex_syringe.purified").withStyle(Style.EMPTY.applyFormats(ChatFormatting.ITALIC, ChatFormatting.YELLOW)));
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
