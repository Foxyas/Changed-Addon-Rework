
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.procedures.IfCatLatexProcedure;
import net.foxyas.changedaddon.procedures.IfDogLatexProcedure;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record TransfurSoundsGuiButtonMessage(int buttonId) {

    public TransfurSoundsGuiButtonMessage(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public static void buffer(TransfurSoundsGuiButtonMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.buttonId);
    }

    public static void handler(TransfurSoundsGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleButtonAction(context.getSender(), message.buttonId));
        context.setPacketHandled(true);
    }

    private static final List<Triple<ResourceLocation, Predicate<Entity>, Integer>> sounds = List.of(
            Triple.of(new ResourceLocation("entity.cat.purr"), IfCatLatexProcedure::execute, 60),
            Triple.of(new ResourceLocation("entity.cat.ambient"), IfCatLatexProcedure::execute, 10),
            Triple.of(new ResourceLocation("entity.wolf.growl"), IfDogLatexProcedure::execute, 60),
            Triple.of(new ResourceLocation("entity.wolf.ambient"), IfDogLatexProcedure::execute, 10),
            Triple.of(new ResourceLocation("entity.wolf.howl"), IfDogLatexProcedure::execute, 80),
            Triple.of(new ResourceLocation("entity.cat.hiss"), IfCatLatexProcedure::execute, 40),
            Triple.of(new ResourceLocation("entity.cat.purreow"), IfCatLatexProcedure::execute, 20)
    );

    public static void handleButtonAction(Player player, int buttonID) {
        if(player == null) return;

        if (!ProcessTransfur.isPlayerTransfurred(player)) return;

        ChangedAddonModVariables.PlayerVariables vars = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
        if (vars == null) return;

        switch (buttonID) {
            case 0, 1, 2, 3, 4, 5, 6 -> {
                if (vars.act_cooldown) break;
                Triple<ResourceLocation, Predicate<Entity>, Integer> triple = sounds.get(buttonID);
                if (triple.getMiddle().test(player))
                    playSound(player.level, player, ForgeRegistries.SOUND_EVENTS.getValue(triple.getLeft()), vars, triple.getRight());
            }
            case 7 -> {
                if (!vars.act_cooldown) break;

                vars.act_cooldown = false;
                vars.syncPlayerVariables(player);
            }
            case 8 -> {
                if (vars.act_cooldown) return;
                TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
                if (tf == null) break;

                if (tf.getFormId().toString().contains("changed_addon:form_ket_experiment009")) {
                    player.getLevel().playSound(null, player.position().x, player.position().y, player.position().z, ChangedSounds.MONSTER2, SoundSource.HOSTILE, 35, 0);
                } else {
                    player.getLevel().playSound(null, player.position().x, player.position().y, player.position().z, ChangedSounds.MONSTER2, SoundSource.HOSTILE, 5, 1);
                }

                vars.act_cooldown = true;
                vars.syncPlayerVariables(player);

                new DelayedTask(60, () -> {
                    vars.act_cooldown = false;
                    vars.syncPlayerVariables(player);
                });
            }
        }
    }

    private static void playSound(Level level, Entity entity, SoundEvent sound, ChangedAddonModVariables.PlayerVariables vars, int cooldown) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 2, 1);

        vars.act_cooldown = true;
        vars.syncPlayerVariables(entity);

        new DelayedTask(cooldown, () -> {
            vars.act_cooldown = false;
            vars.syncPlayerVariables(entity);
        });
    }
}
