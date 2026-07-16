package net.foxyas.changedaddon.ability.handle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.PsychicGrab;
import net.foxyas.changedaddon.ability.PsychicGrabInstance;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PsychicGrabKeyHandler {

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        int key = event.getKey();
        int action = event.getAction();
        int modifiers = event.getModifiers();
        if (PsychicGrab.Keys.contains(key) && minecraft.screen == null) {
            ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
                PsychicGrabInstance abilityInstance = variantInstance.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get());
                if (abilityInstance != null) {
                    abilityInstance.onClientKeyPressed(player, key, action, modifiers);
                }
            }));
        }
    }
}
