package net.foxyas.changedaddon.entity.simple;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.GenderedEntity;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;
import java.util.Set;

public abstract class AbstractSnowFoxEntity extends ChangedEntity implements GenderedEntity, PowderSnowWalkable {

    public AbstractSnowFoxEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public static Set<ResourceKey<Biome>> getSpawnBiomes() {
        return Set.of(
                Biomes.SNOWY_PLAINS,
                Biomes.SNOWY_BEACH,
                Biomes.SNOWY_TAIGA,
                Biomes.SNOWY_SLOPES
        );
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.95f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }
}
