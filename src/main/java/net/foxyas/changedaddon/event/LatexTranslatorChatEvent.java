package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.item.TranslatorItem;
import net.foxyas.changedaddon.process.features.LatexLanguageTranslator;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class LatexTranslatorChatEvent {

    /*@SubscribeEvent
    public static void onClientChat(ClientChatReceivedEvent event) {
        if (!ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.get()) return;

        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        Player localPlayer = mc.player;

        if (level == null || localPlayer == null) return;

        UUID senderUUID = event.getSender();
        if (senderUUID == null) return;

        Entity senderEntity = PlayerUtil.GlobalEntityUtil.getEntityByUUID(level, senderUUID);
        if (!(senderEntity instanceof Player sender)) return;

        if (!isLatex(sender) || hasTranslator(sender)) return;

        if (canUnderstandLatex(localPlayer)) return;

        // Traduz apenas para ESTE client
        Component original = event.getMessage();
        String namePart = "<" + sender.getName().getString() + ">";
        String message = original.getString().replaceFirst(namePart, "");


        String translated = LatexLanguageTranslator.translateText(
                message,
                LatexLanguageTranslator.TranslationType.TO_LATEX_LANGUAGE
        );

        MutableComponent finalMessage = Component.literal(namePart).append(Component.literal(translated)).withStyle(original.getStyle());

        event.setMessage(finalMessage);
    }*/

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {

        if (!ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.get())
            return;

        ServerPlayer sender = event.getPlayer();
        String message = event.getMessage().getString();

        if (!isLatex(sender) || hasTranslator(sender))
            return;

        event.setCanceled(true);

        for (ServerPlayer receiver : sender.server.getPlayerList().getPlayers()) {

            String finalMessage = message;

            if (!canUnderstandLatex(receiver)) {
                finalMessage = LatexLanguageTranslator.translateText(
                        message,
                        LatexLanguageTranslator.TranslationType.TO_LATEX_LANGUAGE
                );
            }

            Style style = event.getMessage().getStyle();
            Component chat = Component.literal("<").append(sender.getDisplayName()).append("> ").append(finalMessage).withStyle(style);

            receiver.sendSystemMessage(chat);
        }
    }

    /* ===== helpers ===== */

    private static boolean canUnderstandLatex(Player player) {
        return isLatex(player) || hasTranslator(player);
    }

    private static boolean isLatex(Player player) {
        return ProcessTransfur.isPlayerTransfurred(player);
    }

    private static boolean hasTranslator(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()
                    && stack.getItem() instanceof TranslatorItem
                    && TranslatorItem.isEnabled(stack)) {
                return true;
            }
        }
        return false;
    }
}

