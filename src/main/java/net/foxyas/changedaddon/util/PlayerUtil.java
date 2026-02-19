package net.foxyas.changedaddon.util;

import com.google.common.base.Predicates;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.beast.AbstractAquaticEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PlayerUtil {

    public static void TransfurPlayer(Player player, String id, float progress) {
        ResourceLocation form = ResourceLocation.tryParse(id);
        TransfurVariant<?> latexVariant = form == null ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        if (latexVariant == null) return;

        ProcessTransfur.setPlayerTransfurVariant(player, latexVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), progress);
    }

    public static void TransfurPlayerAndLoadData(Player player, String id, CompoundTag data, float progress) {
        ResourceLocation form = ResourceLocation.tryParse(id);
        TransfurVariant<?> latexVariant = form == null ? null : ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
        if (latexVariant == null) return;
        var tf = ProcessTransfur.setPlayerTransfurVariant(player, latexVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), progress);
        if (tf != null) {
            CompoundTag save = tf.save();
            save.merge(data);
            tf.load(save);
        }
    }

    public static void UnTransfurPlayer(Player player) {
        if (player.getLevel().isClientSide()) return;

        ProcessTransfur.ifPlayerTransfurred(player, (instance) -> {
            TransfurVariant<?> transfurVariant = null;
            if (instance != null) transfurVariant = instance.getParent();
            UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.SURVIVAL);
            if (ChangedAddonMod.postEvent(untransfurEvent)) {
                if (untransfurEvent.newVariant != null) {
                    ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
                    return;
                }

                player.displayClientMessage(new TranslatableComponent("changed_addon.untransfur.fail"), true);
                return;
            }

            if (instance == null) return;

            instance.unhookAll(player);
            ProcessTransfur.removePlayerTransfurVariant(player);
            ProcessTransfur.setPlayerTransfurProgress(player, 0.0f);
        });
    }

    public static void UnTransfurPlayer(Player player, boolean shouldApplyEffects) {
        if (player.getLevel().isClientSide()) return;

        ProcessTransfur.ifPlayerTransfurred(player, (instance) -> {
            TransfurVariant<?> transfurVariant = null;
            if (instance != null) transfurVariant = instance.getParent();
            UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.SURVIVAL);
            if (ChangedAddonMod.postEvent(untransfurEvent)) {
                if (untransfurEvent.newVariant != null) {
                    ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
                    return;
                }

                player.displayClientMessage(new TranslatableComponent("changed_addon.untransfur.fail"), true);
                return;
            }

            if (instance == null) return;

            instance.unhookAll(player);
            ProcessTransfur.removePlayerTransfurVariant(player);
            ProcessTransfur.setPlayerTransfurProgress(player, 0.0f);
            if (shouldApplyEffects && !player.getLevel().isClientSide()) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
            }
        });
    }

    public static void UnTransfurPlayerAndPlaySound(Player player, boolean shouldApplyEffects) {
        if (player.getLevel().isClientSide()) return;

        ProcessTransfur.ifPlayerTransfurred(player, (instance) -> {
            TransfurVariant<?> transfurVariant = null;
            if (instance != null) transfurVariant = instance.getParent();
            UntransfurEvent untransfurEvent = new UntransfurEvent(player, transfurVariant, UntransfurEvent.UntransfurType.SURVIVAL);
            if (ChangedAddonMod.postEvent(untransfurEvent)) {
                if (untransfurEvent.newVariant != null) {
                    ProcessTransfur.setPlayerTransfurVariant(player, untransfurEvent.newVariant, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE), 1, false);
                    return;
                }

                player.displayClientMessage(new TranslatableComponent("changed_addon.untransfur.fail"), true);
                return;
            }

            if (instance == null) return;

            instance.unhookAll(player);
            ProcessTransfur.removePlayerTransfurVariant(player);
            ProcessTransfur.setPlayerTransfurProgress(player, 0.0f);
            if (shouldApplyEffects && !player.getLevel().isClientSide()) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
                if (player.getLevel() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, player.getX(), player.getEyeY(), player.getZ(), ChangedAddonSoundEvents.UNTRANSFUR.get(), SoundSource.PLAYERS, 1, 1);
                }
            }
        });
    }

    public static boolean isCatTransfur(Player player) {
        TransfurVariant<?> variant = ProcessTransfur.getPlayerTransfurVariant(player).getParent();
        return variant.is(ChangedAddonTags.TransfurTypes.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE);
    }

    public static boolean isWolfTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurTypes.WOLF_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return Objects.requireNonNull(entity.getType().getRegistryName()).toString().contains("dog") ||
                entity.getType().getRegistryName().toString().contains("wolf") ||
                entity instanceof AbstractLatexWolf;
    }

    public static boolean isFoxTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurTypes.FOX_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity.getType().getRegistryName().toString().contains("fox");
    }

    public static boolean isDragonTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurTypes.DRAGON_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity.getType().getRegistryName().toString().contains("dragon");
    }

    public static boolean isAquaticTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        if (variant.is(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE)) return true;

        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity instanceof AbstractAquaticEntity;
    }

    public static boolean isSpiderTransfur(Player player) {
        TransfurVariant<?> variant = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getParent();
        return variant.is(ChangedAddonTags.TransfurTypes.SPIDER_LIKE);
    }

    public static boolean canRoar(Player player) {
        ChangedEntity entity = Objects.requireNonNull(ProcessTransfur.getPlayerTransfurVariant(player)).getChangedEntity();
        return entity.getType().is(ChangedAddonTags.EntityTypes.CAN_ROAR);
    }

    //=================================================== LookingAt ==================================================//

    public static final ClipContext.ShapeGetter BLOCK_COLLISION = ClipContext.Block.COLLIDER;

    public static final Predicate<Entity> NON_SPECTATOR = entity -> !entity.isSpectator();

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

    //================================================================================================================//

    public static boolean isLineOfSightClear(Player player, Entity entity) {
        var level = player.getLevel();
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
            BlockPos targetPos = new BlockPos(targetVec);

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
