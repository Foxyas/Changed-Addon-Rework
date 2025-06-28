package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.foxyas.changedaddon.variants.ExtraVariantStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;

import java.util.Objects;

public class AvaliEntity extends AbstractBasicOrganicChangedEntity implements ExtraVariantStats {

    public AvaliEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.AVALI.get(), world);
    }

    public AvaliEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((20));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.85f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    public Color3 getDripColor() {
        return Color3.WHITE;
    }

    @Override
    public float extraBlockBreakSpeed() {
        return 0;
    }

    @Override
    public FlyType getFlyType() {
        return FlyType.ONLY_FALL;
    }
}
