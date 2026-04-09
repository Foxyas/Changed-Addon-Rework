package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.LatexSnowLeopardMale;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class SnepsiLeopardEntity extends LatexSnowLeopardMale {

    public SnepsiLeopardEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.SNEPSI_LEOPARD.get(), world);
    }

    public SnepsiLeopardEntity(EntityType<SnepsiLeopardEntity> type, Level world) {
        super(type, world);
        xpReward = XP_REWARD_MEDIUM;
        this.setAttributes(getAttributes());
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 22);
        builder = builder.add(Attributes.ARMOR, 4);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(ChangedAttributes.AIR_CAPACITY.get()).setBaseValue(15.0);
    }

    public Color3 getDripColor() {
        Color3 color;
        if (random.nextInt(10) > 5) {
            color = Color3.getColor("#95D161");
        } else {
            color = Color3.getColor("#B5DF90");
        }
        return color;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.GREEN;
    }

    public Color3 getHairColor(int layer) {
        return Color3.GREEN;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }
}
