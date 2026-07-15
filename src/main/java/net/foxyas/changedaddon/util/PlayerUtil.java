package net.foxyas.changedaddon.util;

import com.google.common.base.Predicates;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.client.gui.TransfurSoundsGuiScreen;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.simple.AbstractSnowFoxEntity;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.ability.*;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.beast.AbstractAquaticEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.world.LatexCoverGetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PlayerUtil {

    public static final ClipContext.ShapeGetter BLOCK_COLLISION = ClipContext.Block.COLLIDER;
    public static final Predicate<Entity> NON_SPECTATOR = entity -> !entity.isSpectator();

    public static boolean canTurnCuddleModeOn(Player player) {
        // Verifica se o jogador é a entidade variante agarrando alguém
        Optional<IAbstractChangedEntity> optionalPlayerVariant = IAbstractChangedEntity.forEitherSafe(player);
        if (optionalPlayerVariant.isPresent()) {
            IAbstractChangedEntity playerVariant = optionalPlayerVariant.get();
            GrabEntityAbilityInstance grabEntityAbilityInstance = playerVariant.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor grabEntityAbilityExtensor) {
                return grabEntityAbilityExtensor.isSafeMode() && grabEntityAbilityInstance.grabbedEntity != null && !grabEntityAbilityInstance.suited;
            }
        }

        // Verifica se o jogador está a ser agarrado
        Optional<IAbstractChangedEntity> grabberSafe = GrabEntityAbility.getGrabberSafe(player);
        if (grabberSafe.isPresent()) {
            IAbstractChangedEntity grabber = grabberSafe.get();
            GrabEntityAbilityInstance grabEntityAbilityInstance = grabber.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor grabEntityAbilityExtensor) {
                return grabEntityAbilityExtensor.isSafeMode() && grabEntityAbilityInstance.grabbedEntity == player && !grabEntityAbilityInstance.suited;
            }
        }

        return false;
    }

    public static boolean isCuddleStateValidForBed(Player player) {
        return canTurnCuddleModeOn(player);
    }

    public static void transfurPlayer(Player player, String id, float progress) {
        ResourceLocation form = ResourceLocation.tryParse(id);
        TransfurVariant<?> latexVariant = form == null ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        if (latexVariant == null) return;

        ProcessTransfur.setPlayerTransfurVariant(player, latexVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), progress);
    }

    public static void transfurPlayerAndLoadData(Player player, String id, CompoundTag data, float progress) {
        ResourceLocation form = ResourceLocation.tryParse(id);
        TransfurVariant<?> latexVariant = form == null ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        transfurPlayerAndLoadData(player, latexVariant, data, progress);
    }

    public static void transfurPlayerAndLoadData(Player player, TransfurVariant<?> latexVariant, CompoundTag data, float progress) {
        transfurPlayerAndLoadData(player, latexVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), data, progress);
    }

    public static void transfurPlayerAndLoadData(Player player, TransfurVariant<?> latexVariant, TransfurContext transfurContext, CompoundTag data, float progress) {
        if (latexVariant == null || player == null) return;

        TransfurVariantInstance<?> tf = ProcessTransfur.setPlayerTransfurVariant(player, latexVariant, transfurContext, progress);

        if (tf != null && data != null && !data.isEmpty()) {
            CompoundTag save = tf.save();
            save.merge(data);
            tf.load(save);
            for (Map.Entry<AbstractAbility<?>, AbstractAbilityInstance> abstractAbilityAbstractAbilityInstanceEntry : tf.abilityInstances.entrySet()) {
                IAbstractChangedEntity entity = IAbstractChangedEntity.forEither(tf.getHost());
                if (entity == null) continue;

                abstractAbilityAbstractAbilityInstanceEntry.getKey().setDirty(entity);
            }
        }
    }

    public static void unTransfurPlayer(Player player) {
        if (player.level.isClientSide()) return;

        ProcessTransfur.ifPlayerTransfurred(player, (instance) -> {
            TransfurVariant<?> transfurVariant = null;
            if (instance != null) transfurVariant = instance.getParent();
            UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.SURVIVAL);
            if (ChangedAddonMod.postEvent(untransfurEvent)) {
                if (untransfurEvent.newVariant != null) {
                    ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
                    return;
                }

                player.displayClientMessage(Component.translatable("changed_addon.untransfur.fail"), true);
                return;
            }

            if (instance == null) return;

            instance.unhookAll(player);
            ProcessTransfur.removePlayerTransfurVariant(player);
            ProcessTransfur.setPlayerTransfurProgress(player, 0.0f);
        });
    }

    public static void unTransfurPlayer(Player player, boolean shouldApplyEffects) {
        unTransfurPlayer(player);
        if (shouldApplyEffects && !player.level().isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
        }
    }

    public static void unTransfurPlayerAndPlaySound(Player player, boolean shouldApplyEffects) {
        unTransfurPlayer(player, shouldApplyEffects);
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getX(), player.getEyeY(), player.getZ(), ChangedAddonSoundEvents.UNTRANSFUR.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public static void splitChangedEntityFromPlayer(Level world, Player player) {
        spawnPlayerTransfurAsChangedEntity(world, player);
        PlayerUtil.unTransfurPlayerAndPlaySound(player, !player.isCreative() && !player.isSpectator());
    }


    public static void spawnPlayerTransfurAsChangedEntity(Level world, Player player) {
        if (player.level.isClientSide() || !(world instanceof ServerLevel level)) return;
        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return;

        ChangedEntity fakeEntity = instance.getChangedEntity();

        Entity entityToSpawn = fakeEntity.getType().create(level);
        assert entityToSpawn != null;
        entityToSpawn.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);
        entityToSpawn.setYBodyRot(0);
        entityToSpawn.setYHeadRot(0);

        if (entityToSpawn instanceof Mob mob) {
            ForgeEventFactory.onFinalizeSpawn(mob, level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        }

        if (fakeEntity instanceof IAlphaAbleEntity original && entityToSpawn instanceof IAlphaAbleEntity alphaAble) {
            alphaAble.setAlpha(original.isAlpha());
            alphaAble.setAlphaScale(original.alphaAdditionalScale());
        }

        world.addFreshEntity(entityToSpawn);
    }

    public static boolean isCatTransfur(Player player) {
        TransfurVariant<?> variant = ProcessTransfur.getPlayerTransfurVariant(player).getParent();
        return variant.is(ChangedAddonTags.TransfurVariants.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurVariants.LEOPARD_LIKE);
    }

    public static boolean isWolfTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurVariants.WOLF_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).toString().contains("dog") ||
                ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().contains("wolf") ||
                entity instanceof AbstractLatexWolf;
    }

    public static boolean isFoxTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurVariants.FOX_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().contains("fox") ||
                entity instanceof AbstractSnowFoxEntity;
    }

    public static boolean isDragonTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurVariants.DRAGON_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString().contains("dragon");
    }

    public static boolean isAquaticTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity instanceof AbstractAquaticEntity;
    }

    public static boolean isSpiderTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        return variant.is(ChangedAddonTags.TransfurVariants.SPIDER_LIKE);
    }

    public static boolean canRoar(Player player) {
        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity.getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }

    public static boolean isApexPredator(Player player) {
        if (!ProcessTransfur.isPlayerTransfurred(player))
            return false;

        ResourceLocation id =
                ProcessTransfur.getPlayerTransfurVariant(player).getFormId();

        if (id == null)
            return false;

        String path = id.toString();

        return path.contains("lion")
                || path.contains("tiger")
                || path.startsWith("changed_addon:form_experiment009") || TransfurEvents.resolveChangedEntity(player).getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }


    /* ------------------------------------------------------------
     * Titles & state
     * ------------------------------------------------------------ */
    public static List<Component> getPlayerSubtitle(Player player) {

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            return List.of(Component.literal("§7Not Transfurred"));
        }

        List<Component> subtitles = new ArrayList<>();

        // Prefixo base
        subtitles.add(Component.literal("§fYou are a"));

        // ===============================
        // Species / family
        // ===============================

        List<MutableComponent> species = new ArrayList<>();

        if (isCatTransfur(player)) {
            species.add(Component.literal("§fCat"));
        }

        if (isFoxTransfur(player)) {
            species.add(Component.literal("§fFox"));
        }

        if (isWolfTransfur(player)) {
            species.add(Component.literal("§fCanine"));
        }

        if (isDragonTransfur(player)) {
            species.add(Component.literal("§fDragon"));
        }

        if (isAquaticTransfur(player)) {
            species.add(Component.literal("§fFish"));
        }

        if (isSpiderTransfur(player)) {
            species.add(Component.literal("§fSpider"));
        }

        if (species.isEmpty()) {
            species.add(Component.literal("§7Unknown"));
        }

        subtitles.add(TransfurSoundsGuiScreen.joinWithSeparator(species, "§7 / "));

        // ===============================
        // Special traits
        // ===============================

        if (isApexPredator(player)) {
            subtitles.add(Component.literal("§6Apex Predator"));
        }

        return subtitles;
    }

    //=================================================== LookingAt ==================================================//


    @Nullable
    public static Entity getEntityLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight) {
        EntityHitResult hit = getEntityHitLookingAt(entity, reach, testLineOfSight);
        return hit != null ? hit.getEntity() : null;
    }

    @Nullable
    public static Entity getEntityLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight, Predicate<Entity> targetPredicate) {
        EntityHitResult hit = getEntityHitLookingAt(entity, reach, testLineOfSight, targetPredicate);
        return hit != null ? hit.getEntity() : null;
    }

    @Nullable
    public static <E extends Entity> E getEntityLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight, Class<E> entityClass) {
        return getEntityLookingAt(entity, reach, testLineOfSight, NON_SPECTATOR, entityClass);
    }

    @Nullable
    public static <E extends Entity> E getEntityLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight, Predicate<Entity> targetPredicate, Class<E> entityClass) {
        EntityHitResult hit = getEntityHitLookingAt(entity, reach, testLineOfSight, targetPredicate.and(entityClass::isInstance));
        return hit != null ? (E) hit.getEntity() : null;
    }

    /**
     * @deprecated Use {@link PlayerUtil#getEntityHitLookingAt(Entity, float, ClipContext.ShapeGetter)}
     */
    @Nullable
    @Deprecated(forRemoval = true)
    public static EntityHitResult getEntityHitLookingAt(Entity entity, float reach, boolean testLineOfSight) {
        return getEntityHitLookingAt(entity, reach, testLineOfSight ? ClipContext.Block.OUTLINE : null);
    }

    @Nullable
    public static EntityHitResult getEntityHitLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight) {
        return getEntityHitLookingAt(entity, reach, testLineOfSight, NON_SPECTATOR);
    }

    @Nullable
    public static EntityHitResult getEntityHitLookingAt(Entity entity, float reach, @Nullable ClipContext.ShapeGetter testLineOfSight, Predicate<Entity> targetPredicate) {
        double reachSqr = reach * reach;
        Vec3 eyePos = entity.getEyePosition();
        Vec3 viewVec = entity.getLookAngle();
        Vec3 toVec = eyePos.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);

        if (testLineOfSight != null) {
            HitResult hitResult = entity.level.clip(new DynamicClipContext(eyePos, toVec,
                    testLineOfSight, Predicates.alwaysFalse(), CollisionContext.of(entity)));

            if (hitResult.getType() != HitResult.Type.MISS) {
                reachSqr = hitResult.getLocation().distanceToSqr(eyePos);
                reach = (float) Math.sqrt(reachSqr);
                toVec = eyePos.add(viewVec.x * reach, viewVec.y * reach, viewVec.z * reach);
            }
        }

        return ProjectileUtil.getEntityHitResult(entity, eyePos, toVec, new AABB(eyePos, toVec), targetPredicate, reachSqr);
    }

    public static @NotNull BlockHitResult clipLatex(
            Level level,
            Entity entity,
            double range
    ) {
        Vec3 from = entity.getEyePosition(1.0F);
        Vec3 to = from.add(entity.getLookAngle().scale(range));

        ClipContext context = new ClipContext(
                from,
                to,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                entity
        );

        LatexCoverGetter getter = LatexCoverGetter.extendDefault(level);
        return getter.clip(context);
    }


    //================================================================================================================//

    public static boolean isLineOfSightClear(Player player, Entity entity) {
        var level = player.level();
        var playerEyePos = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        var entityEyePos = entity.getBoundingBox().getCenter(); // Centro da entidade

        // Realiza o traçado de linha
        var result = level.clip(new ClipContext(
                playerEyePos,
                entityEyePos,
                ClipContext.Block.VISUAL, // Apenas blocos visuais são considerados
                ClipContext.Fluid.NONE, // Ignorar fluidos
                player
        ));

        // Retorna true se o resultado for MISS (nenhum bloco obstruindo)
        return result.getType() == HitResult.Type.MISS;
    }

    public static boolean isProjectileMovingTowardsEntity(Entity player, Entity projectile) {
        Vec3 projectilePosition = projectile.position();
        Vec3 projectileMotion = projectile.getDeltaMovement();

        Vec3 directionToPlayer = player.position().subtract(projectilePosition).normalize();

        return projectileMotion.normalize().dot(directionToPlayer) > 0;
    }

    public static boolean isMovingTowardsEntity(Vec3 objectPos, Vec3 motion, Entity player) {
        Vec3 toPlayer = player.position().subtract(objectPos);

        return motion.dot(toPlayer) > 0;
    }


    public static void shootDynamicLaser(ServerLevel world, Player player, int maxRange, int horizontalRadius, int verticalRadius) {
        Vec3 eyePosition = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        Vec3 lookDirection = player.getLookAngle();    // Direção para onde o jogador está olhando

        for (int i = 0; i <= maxRange; i++) {
            // Calcula a posição do bloco na trajetória do laser
            Vec3 targetVec = eyePosition.add(lookDirection.scale(i));
            BlockPos targetPos = new BlockPos((int) targetVec.x, (int) targetVec.y, (int) targetVec.z);

            // Verifica se o bloco é ar; se for, ignora essa fileira
            if (world.getBlockState(targetPos).isAir()) {
                continue;
            }

            // Afeta os blocos ao redor do ponto atual
            affectSurroundingBlocks(world, targetPos, horizontalRadius, verticalRadius);
        }
    }

    private static void affectSurroundingBlocks(Level world, BlockPos center, int horizontalRadius, int verticalRadius) {
        int horizontalRadiusSphere = horizontalRadius - 1;
        int verticalRadiusSphere = verticalRadius - 1;

        for (int y = -verticalRadiusSphere; y <= verticalRadiusSphere; y++) {
            for (int x = -horizontalRadiusSphere; x <= horizontalRadiusSphere; x++) {
                for (int z = -horizontalRadiusSphere; z <= horizontalRadiusSphere; z++) {
                    // Calcula a distância ao centro para uma forma esférica
                    double distanceSq = (x * x) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) +
                            (y * y) / (double) (verticalRadiusSphere * verticalRadiusSphere) +
                            (z * z) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);

                    if (distanceSq <= 1.0) { // Dentro da área de efeito
                        BlockPos affectedPos = center.offset(x, y, z);
                        if (world.getBlockState(affectedPos).isAir()) {
                            break;
                        }
                        // Insira a lógica para afetar os blocos
                        affectBlock(world, affectedPos);
                    }
                }
            }
        }
    }

    private static void affectBlock(Level world, BlockPos pos) {
        // Exemplo de lógica personalizada para afetar blocos
        if (!world.getBlockState(pos).isAir()) {
            // Substituir bloco por vidro como exemplo
            world.setBlock(pos, Blocks.GLASS.defaultBlockState(), 3);

            // Adicionar partículas no bloco afetado
            world.addParticle(ParticleTypes.FLAME,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0.1, 0);
        }
    }

    /**
     * Gets the BlockHitResult of what the player is looking at.
     * * @param player The player entity.
     *
     * @param maxDistance The maximum reach distance (standard survival reach is ~4.5 to 5.0 blocks).
     * @return The HitResult containing the block position and side hit, or null if nothing is in range.
     */
    public static BlockHitResult getBlockThatEntityIsLookingAt(Player player, double maxDistance) {
        if (player == null || player.level() == null) {
            return null;
        }

        // Get the player's eye position
        Vec3 eyePosition = player.getEyePosition(1.0F);

        // Get the direction the player is looking
        Vec3 lookVector = player.getViewVector(1.0F);

        // Calculate the end point of the raytrace based on max distance
        Vec3 traceEnd = eyePosition.add(lookVector.x * maxDistance, lookVector.y * maxDistance, lookVector.z * maxDistance);

        // Perform the raytrace (Collides with blocks, ignoring fluids by default)
        return player.level().clip(new ClipContext(
                eyePosition,
                traceEnd,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));
    }


    public static class GlobalEntityUtil {
        @Nullable
        public static Entity getEntityByUUID(LevelAccessor world, String uuid) {
            try {
                Stream<Entity> entities;

                if (world instanceof ServerLevel serverLevel) {
                    entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
                } else if (world instanceof ClientLevel clientLevel) {
                    entities = StreamSupport.stream(clientLevel.entitiesForRendering().spliterator(), false);
                } else {
                    return null;
                }

                return entities.filter(entity -> entity.getStringUUID().equals(uuid)).findFirst().orElse(null);
            } catch (Exception e) {
                ChangedAddonMod.LOGGER.error(e.getMessage()); // Log the exception for debugging purposes
                return null;
            }
        }


        @Nullable
        public static Entity getEntityByUUID(ServerLevel serverLevel, String uuid) {
            try {
                Stream<Entity> entities;
                entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
                return entities.filter(entity -> entity.getStringUUID().equals(uuid)).findFirst().orElse(null);
            } catch (Exception e) {
                ChangedAddonMod.LOGGER.error(e.getMessage()); // Log the exception for debugging purposes
                return null;
            }
        }

        @Nullable
        public static Entity getEntityByUUID(LevelAccessor world, UUID uuid) {
            try {
                Stream<Entity> entities;

                if (world instanceof ServerLevel serverLevel) {
                    entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
                } else if (world instanceof ClientLevel clientLevel) {
                    entities = StreamSupport.stream(clientLevel.entitiesForRendering().spliterator(), false);
                } else {
                    return null;
                }

                return entities.filter(entity -> entity.getUUID().equals(uuid)).findFirst().orElse(null);
            } catch (Exception e) {
                ChangedAddonMod.LOGGER.error(e.getMessage()); // Log the exception for debugging purposes
                return null;
            }
        }


        @Nullable
        public static Entity getEntityByUUID(ServerLevel serverLevel, UUID uuid) {
            try {
                Stream<Entity> entities;
                entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
                return entities.filter(entity -> entity.getUUID().equals(uuid)).findFirst().orElse(null);
            } catch (Exception e) {
                ChangedAddonMod.LOGGER.error(e.getMessage()); // Log the exception for debugging purposes
                return null;
            }
        }

        @Nullable
        public static Entity getEntityByName(LevelAccessor world, String name) {
            try {
                Stream<Entity> entities;

                if (world instanceof ClientLevel clientLevel) {
                    entities = StreamSupport.stream(clientLevel.entitiesForRendering().spliterator(), false);
                } else if (world instanceof ServerLevel serverLevel) {
                    entities = StreamSupport.stream(serverLevel.getAllEntities().spliterator(), false);
                } else {
                    return null;
                }

                return entities
                        .filter(entity -> {
                            String entityName = entity.getName().getString();
                            return entityName.equalsIgnoreCase(name);
                        })
                        .findFirst()
                        .orElse(null);

            } catch (Exception e) {
                ChangedAddonMod.LOGGER.error("Error getting entity by name: {}", e.getMessage());
                return null;
            }
        }

    }
}
