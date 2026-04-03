package net.foxyas.changedaddon.entity.advanced;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.ILatexAssimilatedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DazedLatexEntity extends AbstractDazedEntity {

    public DazedLatexEntity(EntityType<DazedLatexEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        this.setAttributes(this.getAttributes());
        setNoAi(false);
        setPersistenceRequired();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void addLivingEntityToBiomes(SpawnPlacementRegisterEvent event) {
        event.register(ChangedAddonEntities.DAZED_LATEX.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                DazedLatexEntity::canSpawnNear,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    private static boolean canSpawnNear(EntityType<DazedLatexEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        if (!isDarkEnoughToSpawn(world, pos, random)) {
            return false;
        }

        if (!world.getBiome(pos).is(Tags.Biomes.IS_PLAINS)) {
            return false;
        }

        // Certifica-se de que o bloco abaixo não é ar e é sólido
        BlockState blockBelow = world.getBlockState(pos.below());
        if (!blockBelow.isSolidRender(world, pos.below()) || !blockBelow.isFaceSturdy(world, pos.below(), Direction.UP)) {
            return false;
        }

        // Defina uma AABB (Área de Checagem) ao redor do spawn para verificar se há Oak Log por perto.
        AABB checkArea = new AABB(pos).inflate(32); // Raio de 32 blocos ao redor

        return world.getBlockStatesIfLoaded(checkArea)
                .anyMatch(state -> state.is(ChangedAddonBlocks.GOO_CORE.get()));
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

        safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 3);
        safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 26);
        safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 40.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.075F);
        safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.025F);
        safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 3.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 0);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 0);
        safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0);
    }

    @Override
    public @Nullable LatexAssimilationDecision<?> makeLatexAssimilationDecision(TransfurCause cause, LivingEntity targetEntity) {
        LatexAssimilationDecision<?> decision = super.makeLatexAssimilationDecision(cause, targetEntity); // Saves the original value just in case

        AtomicReference<LatexAssimilationDecision<?>> decisionAtomicReference = new AtomicReference<>(decision); // A shut up for the compiler.
        if (decision == null || decision.context() == null || decision.context().source() == null) {
            return decision; // Fail Safe Stuff;
        }

        Either<IAbstractChangedEntity, ILatexAssimilatedEntity> source = decision.context().source();

        if (targetEntity.level().isClientSide()) return decision;
        source.ifLeft(sourceEntity -> {
            // If entity is a Dazed Entity and the "method" is Absorption progress
            if (!(sourceEntity.getChangedEntity() instanceof DazedLatexEntity) || decision.method() != LatexAssimilationDecision.Method.ABSORPTION) return;

            // If the entity is grabbing the target and wants to absorb -> make them into the "buffed" variant.
            sourceEntity.getAbilityInstanceSafe(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).ifPresent(abilityInstance -> {
                if (abilityInstance.grabbedEntity == targetEntity) {
                    decisionAtomicReference.set(decision.withTransfurVariant(ChangedAddonTransfurVariants.BUFF_DAZED_LATEX.get()));
                }
            });
        });

        return decisionAtomicReference.get();
    }
}