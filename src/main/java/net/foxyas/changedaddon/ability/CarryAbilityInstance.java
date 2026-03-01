package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.*;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexCentaur;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class CarryAbilityInstance extends AbstractAbilityInstance {

    @Nullable
    public Entity carriedEntity;

    public CarryAbilityInstance(AbstractAbility<CarryAbilityInstance> carryAbilityInstanceAbstractAbility, IAbstractChangedEntity entity) {
        super(carryAbilityInstanceAbstractAbility, entity);
    }

    @Override
    public boolean canUse() {
        return ability.canUse(entity);
    }

    @Override
    public boolean canKeepUsing() {
        return ability.canKeepUsing(entity);
    }

    @Override
    public void startUsing() {
        Run(entity.getEntity());
    }

    @Override
    public void tick() {}

    @Override
    public void stopUsing() {}

    @Override
    public void onRemove() {
        super.onRemove();
        Run(entity.getEntity());
    }

    @Override
    public void tickIdle() {
        super.tickIdle();

        LivingEntity e = entity.getEntity();

        // Client-side check (Useless for now)
        if (e.getLevel().isClientSide() && e instanceof LocalPlayer player) {
            if (player.getFirstPassenger() != null) {
            }
        }

        if (carriedEntity == null) {
            //broadcastPassengers(e);
            return;
        }

        if (!e.isVehicle() || carriedEntity.getVehicle() != e) {
            onPassengerLost(e, carriedEntity);
            carriedEntity = null;
            broadcastPassengers(e);
            return;
        }

        // Caso especial de água sem coluna de bolhas
        Level level = e.level;
        if (e.isEyeInFluid(FluidTags.WATER) && !level.getBlockState(new BlockPos(e.getX(), e.getEyeY(), e.getZ())).is(Blocks.BUBBLE_COLUMN)) {
            dropPassenger(e);
        }

        if (carriedEntity.hurtMarked) {
            dropPassenger(e);
        }

        if (carriedEntity instanceof LivingEntity living && GrabEntityAbility.getGrabber(living) != null) {
            dropPassenger(e);
        }
    }

    private void dropPassenger(LivingEntity e) {
        assert carriedEntity != null;
        carriedEntity.stopRiding();
        onPassengerLost(e, carriedEntity);
        carriedEntity = null;
        broadcastPassengers(e);
    }


    private void onPassengerLost(LivingEntity carrier, Entity passenger) {
    }


    public static @Nullable Entity carryTarget(Player player) {
        EntityHitResult hitResult = PlayerUtil.getEntityHitLookingAt(player, ((float) player.getAttackRange()), ClipContext.Block.OUTLINE);
        if (hitResult == null) {
            return null;
        }
        return hitResult.getEntity();
    }

    public boolean isPossibleToCarry(LivingEntity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (variant != null) {
                GrabEntityAbilityInstance ability = variant.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
                if (ability != null
                        && ability.suited
                        && ability.grabbedHasControl) {
                    return false;
                }
            }
        }
        return GrabEntityAbility.getGrabber(entity) == null;
    }

    // === Helpers de rede (broadcast correto) ===
    private static void broadcastPassengers(Entity vehicle) {
        if (!vehicle.level.isClientSide) {
            ServerLevel sl = (ServerLevel) vehicle.level;
            sl.getChunkSource().broadcastAndSend(vehicle, new ClientboundSetPassengersPacket(vehicle));
        }
    }

    private static void broadcastMotion(Entity entity) {
        if (!entity.level.isClientSide) {
            ServerLevel sl = (ServerLevel) entity.level;
            sl.getChunkSource().broadcastAndSend(entity, new ClientboundSetEntityMotionPacket(entity));
        }
    }

    // Lança uma entidade na direção do "launcher" e sincroniza
    private static void launchForward(Entity launcher, Entity target, double speed) {
        target.setDeltaMovement(launcher.getLookAngle().normalize().scale(speed));
        target.hasImpulse = true;
        broadcastMotion(target);
    }

    // Toca o som (opcional)
    private static void soundPlay(Player player) {
        player.level.playSound(null, player.blockPosition(), ChangedSounds.BOW2, SoundSource.PLAYERS, 2.5f, 1.0f);
    }


    private void Run(Entity mainEntity) {
        if (!(mainEntity instanceof Player player) || player.level.isClientSide)
            return;

        // Se já está carregando alguém: solta (e opcionalmente arremessa)
        Entity carried = player.getFirstPassenger();
        setCarriedEntity(carried);
        if (carried != null) {
            boolean toss = !player.isShiftKeyDown();

            // Ejetar passageiro
            carried.stopRiding();

            // Sync de passengers do veículo (player)
            broadcastPassengers(player);
            soundPlay(player);

            // Se quiser arremessar pra frente:
            if (toss) {
                launchForward(player, carried, 1.05);
            }
            setCarriedEntity(player.getFirstPassenger());
            return;
        }

        if (player.isSpectator())
            return;

        // Selecionar alvo para carregar
        Entity target = carryTarget(player);
        setCarriedEntity(target);

        if (target == null)
            return;

        if (target instanceof LivingEntity le && !this.isPossibleToCarry(le))
            return;

        // Restrições (centauro, criativo, etc.)
        if (target instanceof WhiteLatexCentaur ||
                (target instanceof Player p && ProcessTransfur.getPlayerTransfurVariant(p) != null &&
                        ProcessTransfur.getPlayerTransfurVariant(p).is(ChangedTransfurVariants.WHITE_LATEX_CENTAUR.get()))) {
            player.displayClientMessage(new TranslatableComponent("changed_addon.warn.cant_carry", target.getDisplayName()), true);
            return;
        }

        if (target instanceof Player carryPlayer && carryPlayer.isCreative() && !player.isCreative())
            return;

        // Só permite tipos permitidos
        if (target.getType().is(ChangedTags.EntityTypes.HUMANOIDS) || target.getType().is(ChangedAddonTags.EntityTypes.CAN_CARRY)) {
            if (target.startRiding(player, true)) {
                // Sync de passengers do veículo (player) para todos
                broadcastPassengers(player);
                soundPlay(player);
            }
        }
    }

    protected void setCarriedEntity(@Nullable Entity carriedEntity) {
        this.carriedEntity = carriedEntity;
    }

    public @Nullable Entity getCarriedEntity() {
        return carriedEntity;
    }

}
