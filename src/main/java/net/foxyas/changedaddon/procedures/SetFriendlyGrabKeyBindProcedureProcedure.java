package net.foxyas.changedaddon.procedures;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModKeyMappings;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.InputConstants;

@Mod.EventBusSubscriber(modid = "changed_addon", bus = Bus.FORGE)
public class SetFriendlyGrabKeyBindProcedureProcedure {/*
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            Player player = event.player;

            // You need to replace "changed_addon" with your actual mod ID
            if (player.level != null) {
                String KeyBind = "";
                String TransLate = "";
                String TransfurText = "";
                String HumanText = "";

                InputConstants.Key keyMapping = ChangedAddonModKeyMappings.FRIENDLY_GRABOFF.getKey();
                KeyBind = keyMapping.toString();
                TransLate = KeyBind;
                if (KeyBind.contains("key.mouse")) {
                    TransLate = KeyBind.replace("key.mouse", "");
                } else if (KeyBind.contains("key.keyboard.unknown")) {
                    TransLate = KeyBind.replace("key.keyboard.unknown", "Not-Set");
                } else if (KeyBind.contains("key.keyboard.semicolon")) {
                    TransLate = KeyBind.replace("key.keyboard.semicolon", "\u00E7");
                } else if (KeyBind.contains("key.keyboard.key.ctrl")) {
                    TransLate = KeyBind.replace("key.keyboard.key.ctrl", "Ctrl +");
                } else if (KeyBind.contains("key.keyboard.key.shift")) {
                    TransLate = KeyBind.replace("key.keyboard.key.shift", "Shift + ");
                } else {
                    TransLate = KeyBind.replace("key.keyboard", "");
                }
                // Rest of your key translation logic...

                // Update your mod's variables with the translated key and sync data
                String Var = TransLate.replace(".", "");
                player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.FriendlyGrabKeybind = Var;
                    capability.syncPlayerVariables(player);
                });

                TransfurText = new TranslatableComponent("translation.FriendlyGrab.TransfurText").getString();
                HumanText = new TranslatableComponent("translation.FriendlyGrab.HumanText").getString();

                // You might want to do something with TransfurText and HumanText here
            }
        }
    }
};

@Mod.EventBusSubscriber(modid = "changed_addon", bus = Bus.FORGE)
class SetFriendlyGrabKeyBindProcedureServer {
    @SubscribeEvent
     @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            // Your server-side code here
            // Update your mod's variables with an empty key and sync data on the server side
            // Example:
            // SomeCapability.getCapability(player).ifPresent(capability -> {
            //     capability.FriendlyGrabKeybind = "";
            //     capability.syncPlayerVariables(player);
            // });
            Player player = event.player;
             if (player.level != null) {
            String Var = "";
            player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.FriendlyGrabKeybind = Var;
                    capability.syncPlayerVariables(player);
                });
      		  }
     	 }
    }*/
}
