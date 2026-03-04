package net.foxyas.changedaddon.network.packet;

import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncGrabControlState(int grabberId, int grabbedId, boolean hasControl) {

    // Decode
    public SyncGrabControlState(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt(), buf.readBoolean());
    }

    // Encode
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(grabberId);
        buf.writeVarInt(grabbedId);
        buf.writeBoolean(hasControl);
    }

    // Handler
    public static void handle(SyncGrabControlState message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity grabberEntity = level.getEntity(message.grabberId);
            Entity grabbedEntity = level.getEntity(message.grabbedId);

            if (!(grabberEntity instanceof LivingEntity livingGrabber)) return;
            if (!(grabbedEntity instanceof LivingEntity livingGrabbed)) return;

            IAbstractChangedEntity grabber = GrabEntityAbility.getGrabber(livingGrabbed);
            if (grabber == null) return;
            if (!grabber.getEntity().is(livingGrabber)) return;

            GrabEntityAbilityInstance abilityInstance =
                    grabber.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());

            if (abilityInstance == null) return;

            abilityInstance.grabbedHasControl = message.hasControl;
        });

        ctx.setPacketHandled(true);
    }
}