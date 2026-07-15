package net.foxyas.changedaddon.event;

import com.mojang.brigadier.CommandDispatcher;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.block.interfaces.ConditionalLatexCoverableBlock;
import net.foxyas.changedaddon.command.*;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.advanced.LatexSnowFoxFoxyasEntity;
import net.foxyas.changedaddon.entity.ai.goals.simple.AlphaSleepGoal;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.util.RPTransfurDenialMessages;
import net.foxyas.changedaddon.util.TransfurVariantUtils;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.foxyas.changedaddon.variant.LatexInfection;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.ltxprogrammer.changed.process.TransfurEvents.TickPlayerTransfurProgressEvent;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.foxyas.changedaddon.entity.ai.goals.simple.AlphaSleepGoal.hasValidAlphaSleepGoal;
import static net.foxyas.changedaddon.event.TransfurEvents.resolveChangedEntity;
import static net.foxyas.changedaddon.process.features.ProcessPatFeature.GlobalPatReactionEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class CommonEvent {

    // to use this event we need to put exactly the entity that should get the animations.. which is not that ideal... soo we made a mixin to adds the animations in every humanoid animator.
    //    @SubscribeEvent
    //    public static void addCustomDefaultAnimators(HumanoidAnimator.GatherAnimatorsEvent<ChangedEntity, AdvancedHumanoidModel<ChangedEntity>> event) {
    //    }

    @SubscribeEvent
    public static void makeAlphaNotDespawnWhenPatted(GlobalPatReactionEvent event) {
        LivingEntity target = event.target;
        if (target instanceof IAlphaAbleEntity iAlphaAbleEntity) {
            if (iAlphaAbleEntity.isAlpha() && target instanceof Mob mob) {
                mob.setPersistenceRequired();
            }
        }
    }

    @SubscribeEvent
    public static void denyBlockSpread(SpreadingLatexType.CoveringBlockEvent event) {
        LevelAccessor level = event.level;
        BlockPos blockPos = event.blockPos;
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof ConditionalLatexCoverableBlock conditionalLatexCoverableBlock) {
            event.setCanceled(!conditionalLatexCoverableBlock.canBeSpread(level, blockState, blockPos));
        }
    }

    @SubscribeEvent
    public static void denyUseBowItem(LivingEntityUseItemEvent.Start event) {
        ItemStack itemStack = event.getItem();
        LivingEntity entity = event.getEntity();

        // Verificamos se é um Player (pois LivingEntity inclui mobs)
        if (!(entity instanceof Player player)) {
            return;
        }

        Item item = itemStack.getItem();

        // Checa se o item é um arco ou besta
        if (item instanceof BowItem || item instanceof CrossbowItem) {

            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            // Sua lógica para verificar se o player está transformado
            if (transfurVariantInstance != null) {
                boolean isRestricted = false;
                ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();
                if (changedEntity instanceof IVariantExtraStats IVariantExtraStats && !IVariantExtraStats.canUseBows()) {
                    isRestricted = true;
                } else if (ChangedAddonServerConfiguration.STOP_TRANSFURRED_PLAYERS_USE_BOWS.get()) {
                    isRestricted = true;
                }

                if (isRestricted) {
                    // Cancela a ação de usar o item
                    event.setCanceled(true);

                    // Envia a mensagem aleatória (Action Bar)
                    player.displayClientMessage(RPTransfurDenialMessages.getRandomBowDenial(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void modifyExperience(LivingExperienceDropEvent experienceDropEvent) {
        int experience = experienceDropEvent.getDroppedExperience();
        if (experienceDropEvent.getEntity() instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) {
            experienceDropEvent.setDroppedExperience((int) (experience * iAlphaAbleEntity.alphaAdditionalScale()));
        }
    }

    @SubscribeEvent
    public static void modifyFallDamage(LivingFallEvent event) {
        LivingEntity livingEntity = event.getEntity();
        Entity entity = resolveChangedEntity(livingEntity);
        if (entity instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) {
            event.setDistance(event.getDistance() * (1 - (0.25f * (IAlphaAbleEntity.getEntityAlphaScale(entity) / 0.75f))));
        }
    }

    @SubscribeEvent
    public static void allowAlphasSleepOnFluffyBlocksInFloor(SleepingLocationCheckEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof PathfinderMob mob) {
            if (hasValidAlphaSleepGoal(mob)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void allowPlayersToSleepAtAnyMomentWhenCuddling(SleepingTimeCheckEvent event) {
        Player sleeper = event.getEntity();
        if (!ChangedAddonVariables.ofOrDefault(sleeper).isCuddling) return;

        event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public static void forcePlayersToNeverSleepEnough(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player sleeper = event.player;
        if (!sleeper.isSleeping()) return;

        if (!ChangedAddonVariables.ofOrDefault(sleeper).isCuddling) return;

        LivingEntityDataExtensor ext = LivingEntityDataExtensor.ofEntity(sleeper);
        if (ext == null) return;

        ext.setSleepCounter(1);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onInteract(PlayerInteractEvent event) {
        if (!event.isCancelable() || event instanceof PlayerInteractEvent.RightClickItem) return;

        Player player = event.getEntity();
        if (player.isSleeping() && ChangedAddonVariables.ofOrDefault(player).isCuddling) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBedInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.isCrouching() || player.isSleeping() || !ChangedAddonVariables.ofOrDefault(player).isCuddling)
            return;

        Level level = player.level;
        BlockHitResult result = event.getHitVec();
        BlockPos pos = result.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.isBed(level, pos, player)) return;

        if (state.hasProperty(BlockStateProperties.BED_PART) && state.getValue(BlockStateProperties.BED_PART) != BedPart.HEAD) {
            //try find head pos
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                BlockPos headPos = pos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                BlockState head = level.getBlockState(headPos);
                if (head.is(state.getBlock())
                        && head.hasProperty(BlockStateProperties.BED_PART)
                        && head.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD) pos = headPos;
            }
        }

        GrabEntityAbilityInstance selfGrab = ProcessTransfur.ifPlayerTransfurred(player, var -> var.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()), () -> null);
        if (selfGrab != null && selfGrab.grabbedEntity != null) return;

        List<Player> list = level.getEntitiesOfClass(Player.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) return;

        Player target = list.get(0);
        if (!ChangedAddonVariables.ofOrDefault(target).isCuddling) return;

        GrabEntityAbilityInstance targetGrab = ProcessTransfur.ifPlayerTransfurred(target, var -> var.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()), () -> null);
        if (targetGrab != null) {
            if (targetGrab.grabbedEntity != null) return;

            if (((GrabEntityAbilityExtensor) targetGrab).canGrabEntity(player) || !ProcessTransfur.isPlayerTransfurred(player)) {
                if (targetGrab.grabEntity(player)) {
                    Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), GrabEntityPacket.initialGrab(target, player));
                    event.setCanceled(true);
                }
                return;
            }
        }

        if (selfGrab == null) return;

        if (((GrabEntityAbilityExtensor) selfGrab).canGrabEntity(target) || !ProcessTransfur.isPlayerTransfurred(target)) {
            if (!selfGrab.grabEntity(target)) return;

            BlockPos bed = target.getSleepingPos().get();
            target.stopSleeping();
            player.startSleepInBed(bed);
            Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), GrabEntityPacket.initialGrab(player, target));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void sendAlphasAlert(VanillaGameEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        Entity cause = event.getCause();
        if (cause == null) return;

        if (!event.getVanillaEvent().is(ChangedAddonTags.GameEvents.CAN_WAKE_UP_ALPHAS)) return;

        Vec3 eventPosition = event.getEventPosition();
        List<PathfinderMob> entitiesOfClass = level.getEntitiesOfClass(PathfinderMob.class,
                new AABB(eventPosition, eventPosition).inflate(32),
                EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(target -> !target.is(cause)).and(target -> target instanceof PathfinderMob mob && mob.isSleeping() && hasAlphaSleepGoal(mob)));

        if (cause instanceof LivingEntity living && living.isSteppingCarefully()) {
            return;
        }

        float dist;
        int sleepDuration;
        for (PathfinderMob target : entitiesOfClass) {
            dist = cause.distanceTo(target);
            List<AlphaSleepGoal> allSleepGoalsFromEntity = AlphaSleepGoal.getAllSleepGoalsFromEntity(target);
            if (allSleepGoalsFromEntity.isEmpty()) continue;

            for (AlphaSleepGoal alphaSleepGoal : allSleepGoalsFromEntity) {
                sleepDuration = (int) (alphaSleepGoal.sleepDuration / dist);
                alphaSleepGoal.sleepDuration -= sleepDuration;
                alphaSleepGoal.sleepDuration = Math.max(0, alphaSleepGoal.sleepDuration);
            }

            VibrationParticleOption vibrationParticleOption = new VibrationParticleOption(new EntityPositionSource(target, target.getEyeHeight()), 20);
            ParticlesUtil.sendParticles(level, vibrationParticleOption, eventPosition, 0, 0, 0, 1, 0);
        }
    }

    private static boolean hasAlphaSleepGoal(PathfinderMob mob) {
        return mob.goalSelector.getAvailableGoals().stream()
                .map(WrappedGoal::getGoal)
                .anyMatch(goal -> goal instanceof AlphaSleepGoal);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();

        ChangedAddonAdminCommand.register(dispatcher);
        ChangedAddonCommandRootCommand.register(dispatcher);
        AccessoryItemCommands.register(dispatcher, buildContext);
        TransfurMe.register(dispatcher);
        ChangedAddonDebugCommands.register(dispatcher);
    }

    @SubscribeEvent
    public static void persistAttributes(PlayerEvent.Clone event) {
        Player oldP = event.getOriginal();
        Player newP = event.getEntity();
        newP.getAttribute(ChangedAddonAttributes.LATEX_RESISTANCE.get()).setBaseValue(oldP.getAttribute(ChangedAddonAttributes.LATEX_RESISTANCE.get()).getBaseValue());
        newP.getAttribute(ChangedAddonAttributes.LATEX_INFECTION.get()).setBaseValue(oldP.getAttribute(ChangedAddonAttributes.LATEX_INFECTION.get()).getBaseValue());
    }

    //Var sync
    @SubscribeEvent
    public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level.isClientSide()) {
            ChangedAddonVariables.ofOrDefault(player).syncPlayerVariables(player);

            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant instanceof TransfurVariantInstanceExtensor transfurVariantInstanceExtensor) {
                transfurVariantInstanceExtensor.maySendDataUpdate();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level.isClientSide())
            ChangedAddonVariables.ofOrDefault(player).syncPlayerVariables(player);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level.isClientSide())
            ChangedAddonVariables.ofOrDefault(player).syncPlayerVariables(player);
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        Player originalPl = event.getOriginal();
        originalPl.reviveCaps();
        ChangedAddonVariables.PlayerVariables original = ChangedAddonVariables.ofOrDefault(originalPl);
        originalPl.invalidateCaps();

        ChangedAddonVariables.PlayerVariables clone = ChangedAddonVariables.ofOrDefault(event.getEntity());
        original.copyTo(clone, event.isWasDeath());
    }
    //

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (!player.isAlive()) return;

        cleanAlphaAttributes(player);

        maskTransfur(player, player.level);

        tickUntransfur(player);

        triggerSwimRegret(player);

        getFriendlyLatexAchievement(event);
    }

    @SubscribeEvent
    public static void onFarmlandTrampleWhenTransfurred(BlockEvent.FarmlandTrampleEvent event) {
        if (event.getEntity() instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.is(ChangedAddonTransfurVariants.PROTOTYPE)) {
                event.setCanceled(true);
            }
            /* Todo?
                maybe make the player only trample the dirt if the entity base of the variant can trample?
            else if (transfurVariant != null) {
                BlockState state = event.getState();
                BlockPos pos = event.getPos();
                float fallDistance = event.getFallDistance();
                event.setCanceled(!(transfurVariant.getChangedEntity().canTrample(state, pos, fallDistance)));
            }*/
        }
    }

    @SubscribeEvent
    public static void onPlayerProgressTransfurTick(TickPlayerTransfurProgressEvent tickPlayerTransfurProgressEvent) {
        tickInfectionAndRes(tickPlayerTransfurProgressEvent);
        mayStallTransfurProgress(tickPlayerTransfurProgressEvent);
    }

    @SubscribeEvent
    public static void onEntityAbsorbOther(TransfurEvents.AbsorbedEntityEvent event) {
        IAbstractChangedEntity source = event.entity;
        if (source.getEntity() instanceof Player player) {
            player.awardStat(ChangedAddonStatRegistry.ENTITY_ASSIMILATED.get());
        }
    }

    @SubscribeEvent
    public static void onEntityReplicateOther(TransfurEvents.AssimilatedEntityEvent event) {
        IAbstractChangedEntity source = event.entity;
        if (source.getEntity() instanceof Player player) {
            player.awardStat(ChangedAddonStatRegistry.ENTITY_TRANSFURRED.get());
        }
    }

    private static void cleanAlphaAttributes(Player player) {
        if (player.isDeadOrDying()) return;

        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariant == null || (transfurVariant.getChangedEntity() instanceof IAlphaAbleEntity alphaAble && !alphaAble.isAlpha())) {
            IAlphaAbleEntity.removeAlphaModifiers(player);
        }
    }

    public static final String HOLDING_DARK_LATEX_MASK_TAG = "holdingDarkLatexMask";

    private static void maskTransfur(Player player, Level level) {
        int doTransfur = level.getLevelData().getGameRules().getInt(ChangedAddonGameRules.TICKS_TO_DARK_LATEX_MASK_TRANSFUR);
        if (doTransfur <= 0) return;
        if (player.isCreative() || player.isSpectator()) return;

        if (!player.getPersistentData().contains(HOLDING_DARK_LATEX_MASK_TAG)) {
            player.getPersistentData().putInt(HOLDING_DARK_LATEX_MASK_TAG, 0);
        }

        int maskHeldTimer = player.getPersistentData().getInt(HOLDING_DARK_LATEX_MASK_TAG);
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            if (maskHeldTimer > 0) {
                player.getPersistentData().putInt(HOLDING_DARK_LATEX_MASK_TAG, maskHeldTimer - 1);
            } else {
                player.getPersistentData().remove(HOLDING_DARK_LATEX_MASK_TAG);
            }
            return;
        }

        InteractionHand maskHand = null;
        if (player.getMainHandItem().is(ChangedItems.DARK_LATEX_MASK.get())) maskHand = InteractionHand.MAIN_HAND;
        if (maskHand == null && player.getOffhandItem().is(ChangedItems.DARK_LATEX_MASK.get()))
            maskHand = InteractionHand.OFF_HAND;

        if (maskHand == null) {
            if (maskHeldTimer > 0) {
                player.getPersistentData().putDouble(HOLDING_DARK_LATEX_MASK_TAG, maskHeldTimer - 1);
            } else {
                player.getPersistentData().remove(HOLDING_DARK_LATEX_MASK_TAG);
            }
            return;
        }

        if (maskHeldTimer < doTransfur) {
            player.getPersistentData().putInt(HOLDING_DARK_LATEX_MASK_TAG, maskHeldTimer + 1);
            return;
        }

        ItemStack stack = player.getItemInHand(maskHand);
        stack.shrink(1);
        player.getInventory().setChanged();


        if (ProcessTransfur.progressTransfur(player, (float) ProcessTransfur.getEntityTransfurTolerance(player) * 2, Syringe.getVariant(stack), TransfurContext.hazard(TransfurCause.GRAB_REPLICATE))) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                ChangedSounds.broadcastSound(player, instance.getParent().sound, 1, 1);
            }
        }

        player.getPersistentData().putInt(HOLDING_DARK_LATEX_MASK_TAG, 0);
        player.getPersistentData().remove(HOLDING_DARK_LATEX_MASK_TAG);
    }

    private static void mayStallTransfurProgress(TickPlayerTransfurProgressEvent event) {
        Player player = event.getPlayer();
        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(player);
        LatexInfection latexInfection = playerVariables.latexInfection;
        if (latexInfection.shouldStallTransfurProgress()) {
            event.setCanceled(true);
        }
    }

    private static void tickInfectionAndRes(TickPlayerTransfurProgressEvent event) {
        Player player = event.getPlayer();
        if (ProcessTransfur.isPlayerTransfurred(player)) return;

        float progress = ProcessTransfur.getPlayerTransfurProgress(player);
        if (progress < 0) return;
        float currentProgress = event.getCurrentProgress();
        float newDeltaProgress = event.getDeltaProgress();

        float latexRes = (float) player.getAttributeValue(ChangedAddonAttributes.LATEX_RESISTANCE.get());
        float infection = (float) player.getAttributeValue(ChangedAddonAttributes.LATEX_INFECTION.get());
        float tolerance = (float) ProcessTransfur.getEntityTransfurTolerance(player);

        boolean infectionWins = infection > latexRes;
        boolean resistanceWins = latexRes >= infection;


        // --- Resistance Wins
        if (resistanceWins) {
            newDeltaProgress -= 0.5f * latexRes;
        }

        // --- Infection Wins
        else if (infectionWins) {
            newDeltaProgress += (infection / 10f);

            // Block the natural Tick
            event.setCanceled(true);
        }

        if (player.tickCount % 20 == 0) { // only process after 1 second
            if (!player.isCreative() && !player.isSpectator()) {

                // Se o novo progresso for atingir ou passar da tolerância
                if (currentProgress + newDeltaProgress >= tolerance) {
                    // Calcula exatamente quanto falta para chegar no limite
                    float remainingToMax = tolerance - currentProgress;

                    // Define o novo delta para preencher apenas 95% do espaço restante.
                    // Isso cria uma "curva assintótica" (desacelera conforme chega perto)
                    // e garante matematicamente que NUNCA vai encostar na tolerância.
                    newDeltaProgress = remainingToMax * 0.95f;

                    // Salvaguarda extrema para arredondamentos de float:
                    // Se mesmo com o multiplicador o valor ainda somar ≥ tolerance, força um limite fixo
                    if (currentProgress + newDeltaProgress >= tolerance) {
                        newDeltaProgress = remainingToMax - 0.01f;
                    }
                }

                event.setDeltaProgress(newDeltaProgress);
                if (event.isCanceled()) {
                    ProcessTransfur.setPlayerTransfurProgress(player, currentProgress + newDeltaProgress);
                }
            }
        }
    }


    private static void tickUntransfur(Player player) {
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
        if (vars == null) return;

        if (!player.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) {
            if (vars.untransfurProgress > 0) {
                vars.untransfurProgress -= .1f;
                vars.syncPlayerVariables(player);
            }
            return;
        }

        if (!ProcessTransfur.isPlayerTransfurred(player)) return;

        if (vars.untransfurProgress < 0) {
            vars.untransfurProgress = 0;
        } else {
            vars.untransfurProgress += (ProcessTransfur.isPlayerNotLatex(player) ? 0.1 : 0.2);

            if (player.isSleeping()) vars.untransfurProgress += .5f;
        }
        vars.syncPlayerVariables(player);
    }

    private static void triggerSwimRegret(Player player) {
        if (player.level.isClientSide || !ProcessTransfur.isPlayerTransfurred(player)) return;
        CompoundTag playerData = player.getPersistentData();
        if (playerData.contains("TransfurData")) {
            int ticks = playerData.getCompound("TransfurData").getInt("SlowSwimInWaterTicks");
            if (TransfurVariantUtils.getSwimSpeedOfVariantBasedOnPlayer(ProcessTransfur.getPlayerTransfurVariant(player).getParent(), player) > 0.95) {
                if (ticks != 0) {
                    playerData.getCompound("TransfurData").putInt("SlowSwimInWaterTicks", 0);
                }
                return;
            }

            if (ticks == -1) return;

            if (player.isSwimming() && player.isInWaterOrBubble()) {
                ticks++;
            }

            if (ticks >= 600) {
                ServerPlayer sPlayer = (ServerPlayer) player;
                Advancement advancement = sPlayer.server.getAdvancements().getAdvancement(ChangedAddonMod.resourceLoc("swim_regret"));
                AdvancementProgress _ap = sPlayer.getAdvancements().getOrStartProgress(advancement);
                if (!_ap.isDone()) {
                    for (String s : _ap.getRemainingCriteria()) sPlayer.getAdvancements().award(advancement, s);
                }
                ticks = -1;
            }

            playerData.getCompound("TransfurData").putInt("SlowSwimInWaterTicks", ticks);
        } else {
            if (TransfurVariantUtils.getSwimSpeedOfVariantBasedOnPlayer(ProcessTransfur.getPlayerTransfurVariant(player).getParent(), player) > 0.95) {
                if (playerData.contains("TransfurData")) {
                    playerData.remove("TransfurData");
                }
            } else {
                if (!playerData.contains("TransfurData")) {
                    CompoundTag tag = new CompoundTag();
                    tag.putInt("SlowSwimInWaterTicks", 0);
                    playerData.put("TransfurData", tag);
                }
            }
        }
    }

    private static void getFriendlyLatexAchievement(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level;
        if (player instanceof ServerPlayer sPlayer) {
            Advancement adv = sPlayer.server.getAdvancements().getAdvancement(ChangedAddonMod.resourceLoc("gooey_friend"));
            AdvancementProgress ap = sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(adv));

            if (!ap.isDone()) {
                final Vec3 center = new Vec3(player.getX(), player.getY(), player.getZ());
                List<LatexSnowFoxFoxyasEntity> latexSnowFoxFoxyasEntities = level.getEntitiesOfClass(LatexSnowFoxFoxyasEntity.class, new AABB(center, center).inflate(2), e -> true)
                        .stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();

                if (!latexSnowFoxFoxyasEntities.isEmpty()) {
                    for (String s : ap.getRemainingCriteria()) sPlayer.getAdvancements().award(adv, s);
                }
            }
        }
    }
}
