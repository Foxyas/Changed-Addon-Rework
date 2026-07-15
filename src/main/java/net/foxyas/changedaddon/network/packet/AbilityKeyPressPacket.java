package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.ability.api.IKeyPressHandler;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @param keyCode Pode ser o código da tecla (ex: GLFW.GLFW_KEY_LEFT)
 */
public record AbilityKeyPressPacket(int keyCode, int action, int modifiers, AbstractAbility<?> ability) {

    public AbilityKeyPressPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), ChangedRegistry.ABILITY.readRegistryObject(buf));
    }

    public static void handle(AbilityKeyPressPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            int key = msg.keyCode;
            int action = msg.action;
            int modifiers = msg.modifiers;
            AbstractAbility<?> ability = msg.ability();
            ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent((variantInstance -> {
                AbstractAbilityInstance abilityInstance = variantInstance.getAbilityInstance(ability);
                if (abilityInstance instanceof IKeyPressHandler iKeyPressHandler) {
                    iKeyPressHandler.onServerProcessKeyPressed(player, key, action, modifiers);
                } else {
                    //Generic Fail safe because is a good practice
                    CompoundTag keyInput = new CompoundTag();
                    keyInput.putInt("key", key);
                    keyInput.putInt("action", action);
                    keyInput.putInt("modifiers", modifiers);

                    CompoundTag tag = new CompoundTag();
                    tag.put("keyInput", keyInput);

                    abilityInstance.acceptPayload(tag);
                }
            }));
        });
        context.setPacketHandled(true);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(keyCode);
        buf.writeInt(action);
        buf.writeInt(modifiers);
        ChangedRegistry.ABILITY.writeRegistryObject(buf, this.ability);
    }
}

