
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.procedures.IfCatLatexProcedure;
import net.foxyas.changedaddon.procedures.IfDogLatexProcedure;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TransfurSoundsGuiButtonMessage {
    private final int buttonID, x, y, z;

    public TransfurSoundsGuiButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    public TransfurSoundsGuiButtonMessage(int buttonID, int x, int y, int z) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void buffer(TransfurSoundsGuiButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static void handler(TransfurSoundsGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player entity = context.getSender();
            int buttonID = message.buttonID;
            int x = message.x;
            int y = message.y;
            int z = message.z;
            handleButtonAction(entity, buttonID, x, y, z);
        });
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

    public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
        Level level = entity.level;
        // security measure to prevent arbitrary chunk generation
        if (!level.hasChunkAt(new BlockPos(x, y, z))) return;

        if (!ProcessTransfur.isPlayerTransfurred(entity)) return;

        ChangedAddonModVariables.PlayerVariables vars = entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
        if (vars == null) return;

        switch (buttonID) {
            case 0, 1, 2, 3, 4, 5, 6 -> {
                if (vars.act_cooldown) break;
                Triple<ResourceLocation, Predicate<Entity>, Integer> triple = sounds.get(buttonID);
                if (triple.getMiddle().test(entity))
                    playSound(level, entity, ForgeRegistries.SOUND_EVENTS.getValue(triple.getLeft()), vars, triple.getRight());
            }
            case 7 -> {
                if (!vars.act_cooldown) break;

                vars.act_cooldown = false;
                vars.syncPlayerVariables(entity);
            }
            case 8 -> {
                if (level.isClientSide || vars.act_cooldown) return;
                TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(entity);
                if (tf == null) break;

                Commands commands = entity.getServer().getCommands();
                if (tf.getFormId().toString().contains("changed_addon:form_ket_experiment009")) {
                    //commands.performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:monster2 hostile @a ~ ~ ~ 35 0 0");
                    entity.getLevel().playSound(null, entity.position().x, entity.position().y, entity.position().z, ChangedSounds.MONSTER2, SoundSource.HOSTILE, 35, 0);
                } else {
                    //commands.performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:monster2 hostile @a ~ ~ ~ 5 1");
                    entity.getLevel().playSound(null, entity.position().x, entity.position().y, entity.position().z, ChangedSounds.MONSTER2, SoundSource.HOSTILE, 5, 1);
                }


                vars.act_cooldown = true;
                vars.syncPlayerVariables(entity);

                new DelayedTask(60, null, i -> {
                    vars.act_cooldown = false;
                    vars.syncPlayerVariables(entity);
                });
            }
        }
    }

    private static void playSound(Level level, Entity entity, SoundEvent sound, ChangedAddonModVariables.PlayerVariables vars, int cooldown) {
        if (!level.isClientSide()) {
            level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()), sound, SoundSource.PLAYERS, 2, 1);
        } else {
            level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), sound, SoundSource.PLAYERS, 2, 1, false);
        }

        vars.act_cooldown = true;
        vars.syncPlayerVariables(entity);

        new DelayedTask(cooldown, null, i -> {
            vars.act_cooldown = false;
            vars.syncPlayerVariables(entity);
        });
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        ChangedAddonMod.addNetworkMessage(TransfurSoundsGuiButtonMessage.class, TransfurSoundsGuiButtonMessage::buffer, TransfurSoundsGuiButtonMessage::new, TransfurSoundsGuiButtonMessage::handler);
    }
}
