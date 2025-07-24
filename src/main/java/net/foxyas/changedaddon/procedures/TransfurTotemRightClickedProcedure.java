package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class TransfurTotemRightClickedProcedure {

    public static void execute(Level level, double x, double y, double z, Player player, ItemStack itemstack) {

        boolean inOffhand = player.getOffhandItem().getItem() == itemstack.getItem();
        boolean inMainhand = player.getMainHandItem().getItem() == itemstack.getItem();
        boolean isValidUse = inOffhand && (inMainhand || player.getMainHandItem().getItem() == Blocks.AIR.asItem());

        if (!isValidUse) return;

        String form = itemstack.getOrCreateTag().getString("form");
        boolean isShift = player.isShiftKeyDown();
        boolean isTransfur = getTransfur(player);

        if (!isShift) {
            if (isTransfur) {
                if (!form.isEmpty()) {
                    untransfurPlayer(level, player, itemstack);
                } else {
                    showMessage(player, "Any form linked please link one §e<Shift+Click>");
                }
            } else {
                if (form.equals("changed_addon:form_puro_kind/female")) {
                    itemstack.getOrCreateTag().putString("form", "changed_addon:form_latex_puro_kind/female");
                }

                if (form.isEmpty()) {
                    showMessage(player, "Any form linked please link one §e<Shift+Click>");
                } else {
                    PlayerUtil.TransfurPlayer(player, form, 1);
                    activateVisuals(level, player, itemstack, "changed_addon:transfur_totem_advancement_1", 100);
                }
            }

            return;
        }

        if (isTransfur) {
            if (form.isEmpty()) {
                ResourceLocation latexFormRes = getTransfurIDVar(player);
                if (latexFormRes == null) {
                    return;
                }
                String latexForm = latexFormRes.toString();
                boolean acceptAll = ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get();

                if (!acceptAll && latexForm.startsWith("changed:form")) {
                    linkForm(itemstack, latexForm);
                    activateVisuals(level, player, itemstack, null, 100, "block.beacon.activate");
                } else if (!acceptAll && latexForm.startsWith("changed_addon:form")) {
                    rejectForm(level, player, itemstack, "changed_addon.latex_totem.notvalid");
                } else if (!acceptAll && latexForm.startsWith("changed:special")) {
                    linkForm(itemstack, "changed:form_light_latex_wolf");
                    activateVisuals(level, player, itemstack, null, 100, "block.beacon.activate");
                } else if (acceptAll) {
                    linkForm(itemstack, latexForm);
                    activateVisuals(level, player, itemstack, null, 100, "block.beacon.activate");
                }
            } else {
                clearForm(level, player, itemstack, 50, "block.beacon.deactivate");
            }
        } else {
            if (!form.isEmpty()) {
                clearForm(level, player, itemstack, 50, "block.beacon.deactivate");
            }
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private static ChangedAddonModVariables.PlayerVariables getVar(Entity entity) {
        return entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .orElse(new ChangedAddonModVariables.PlayerVariables());
    }

    @Nullable
    private static ResourceLocation getTransfurIDVar(Entity entity) {
        if (entity instanceof Player player) {
            var instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                return instance.getFormId();
            }
        }

        return null;
    }

    private static boolean getTransfur(Entity entity) {
        return entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player);
    }

    private static void untransfurPlayer(Level level, Player entity, ItemStack itemstack) {
        SummonDripParticlesProcedure.execute(entity);
        PlayerUtil.UnTransfurPlayer(entity);
        cooldown(entity, itemstack, 100);
        visualActivate(level, entity, "changed_addon:untransfursound");
        grantAdvancement(entity, "changed_addon:transfur_totem_advancement_1");
    }

    private static void showMessage(Entity entity, String message) {
        if (entity instanceof Player player && !player.level.isClientSide())
            player.displayClientMessage(new TextComponent(message), true);
    }

    private static void linkForm(ItemStack stack, String form) {
        stack.getOrCreateTag().putString("form", form);
    }

    private static void clearForm(Level level, Player entity, ItemStack itemstack, int cooldown, String sound) {
        itemstack.getOrCreateTag().putString("form", "");
        activateVisuals(level, entity, itemstack, null, cooldown, sound);
    }

    private static void rejectForm(Level level, Player entity, ItemStack itemstack, String msgKey) {
        cooldown(entity, itemstack, 50);
        visualActivate(level, entity, "entity.zombie.attack_iron_door");
        if (!entity.level.isClientSide())
            entity.displayClientMessage(new TextComponent(new TranslatableComponent(msgKey).getString()), true);
    }

    private static void cooldown(Entity entity, ItemStack itemstack, int ticks) {
        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(itemstack.getItem(), ticks);
    }

    private static void activateVisuals(Level level, Player entity, ItemStack itemstack, String advancement, int cooldown) {
        activateVisuals(level, entity, itemstack, advancement, cooldown, null);
    }

    private static void activateVisuals(Level level, Player entity, ItemStack itemstack, String advancement, int cooldown, String soundId) {
        if (level.isClientSide())
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);

        cooldown(entity, itemstack, cooldown);
        if (soundId != null) visualActivate(level, entity, soundId);

        if (advancement != null)
            grantAdvancement(entity, advancement);
    }

    private static void visualActivate(Level level, Player player, String soundId) {
        var sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundId));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1, 1);
    }

    private static void grantAdvancement(Entity entity, String id) {
        if (entity instanceof ServerPlayer player) {
            Advancement adv = player.server.getAdvancements().getAdvancement(new ResourceLocation(id));
            if (adv == null) return;

            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
            if (!progress.isDone()) {
                for (String criterion : progress.getRemainingCriteria()) {
                    player.getAdvancements().award(adv, criterion);
                }
            }
        }
    }
}
