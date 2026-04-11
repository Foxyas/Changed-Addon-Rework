package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.ltxprogrammer.changed.client.animations.AnimationAssociations;
import net.ltxprogrammer.changed.entity.animation.AnimationCategory;
import net.ltxprogrammer.changed.entity.animation.AnimationEvent;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class S2CPlayAnimationAfterParticleFade<T extends AnimationParameters> {
    public final int entityId;
    public final int colorRGB;
    public final Vec3 pos;
    public final Vec3 motion;
    public final float speed;
    public final int count;

    // Campos da Animação
    public final AnimationEvent<T> event;
    @Nullable public final AnimationCategory category;
    @Nullable public final T parameters;

    public S2CPlayAnimationAfterParticleFade(LivingEntity entity, Color fadeColor, Vec3 pos, Vec3 motion, float speed, int count,
                                             AnimationEvent<T> event, @Nullable AnimationCategory category, @Nullable T parameters) {
        this.entityId = entity.getId();
        this.colorRGB = fadeColor.getRGB();
        this.pos = pos;
        this.motion = motion;
        this.speed = speed;
        this.count = count;
        this.event = event;
        this.category = category;
        this.parameters = parameters;
    }

    @SuppressWarnings("unchecked")
    public S2CPlayAnimationAfterParticleFade(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.colorRGB = buffer.readInt();
        this.pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.motion = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.speed = buffer.readFloat();
        this.count = buffer.readInt();

        // Decodificação da Animação (Baseado no AnimationEventPacket)
        this.event = (AnimationEvent<T>) ChangedRegistry.ANIMATION_EVENTS.readRegistryObject(buffer);
        this.category = buffer.readOptional(FriendlyByteBuf::readUtf)
                .map(AnimationCategory::fromSerial)
                .flatMap(com.mojang.serialization.DataResult::result)
                .orElse(null);
        this.parameters = (T) buffer.readOptional(FriendlyByteBuf::readAnySizeNbt)
                .map(nbt -> (AnimationParameters) this.event.getCodec().parse(NbtOps.INSTANCE, nbt).getOrThrow(false, err -> {}))
                .orElse(null);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.colorRGB);
        buffer.writeDouble(this.pos.x);
        buffer.writeDouble(this.pos.y);
        buffer.writeDouble(this.pos.z);
        buffer.writeDouble(this.motion.x);
        buffer.writeDouble(this.motion.y);
        buffer.writeDouble(this.motion.z);
        buffer.writeFloat(this.speed);
        buffer.writeInt(this.count);

        // Codificação da Animação
        ChangedRegistry.ANIMATION_EVENTS.writeRegistryObject(buffer, this.event);
        buffer.writeOptional(Optional.ofNullable(this.category), (buf, cat) -> buf.writeUtf(cat.getSerializedName()));
        buffer.writeOptional(Optional.ofNullable(this.parameters), (buf, param) ->
                buf.writeNbt((CompoundTag) this.event.getCodec().encodeStart(NbtOps.INSTANCE, param).getOrThrow(false, err -> {}))
        );
    }
}