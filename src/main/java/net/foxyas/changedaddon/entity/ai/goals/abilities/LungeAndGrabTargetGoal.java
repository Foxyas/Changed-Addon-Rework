package net.foxyas.changedaddon.entity.ai.goals.abilities;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.mixins.abilities.AbilityControllerAccessor;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;

public class LungeAndGrabTargetGoal extends Goal {

    private final IGrabberEntity grabber;
    private LivingEntity target;
    private int lungeTicks = 0; // Controla o tempo de duração da investida

    public LungeAndGrabTargetGoal(IGrabberEntity grabber) {
        this.grabber = grabber;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if (!(grabber instanceof LivingEntity living)) return false;
        
        // Verifica se a entidade está no chão para poder pular/dar o bote
        if (!living.onGround()) return false;

        boolean canEntityGrab = grabber.canEntityGrab(living.getType(), living.level());
        if (!canEntityGrab) return false;

        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        if (grabAbilityInstance == null || grabAbilityInstance.grabbedEntity != null) return false;

        this.target = grabber.asMob().getTarget();
        if (this.target == null || !this.target.isAlive()) return false;
        if (this.target instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) return false;
        if (GrabEntityAbility.getGrabber(this.target) != null) return false;
        if (grabber.getGrabCooldown() > 0) return false;
        if (!this.target.getType().is(ChangedTags.EntityTypes.HUMANOIDS)) return false;

        // Distância ideal para o Lunge (Investida) -> Entre 3.0 e 7.0 blocos
        double distance = living.distanceTo(this.target);
        return distance >= 3.0D && distance <= 7.0D;
    }

    @Override
    public boolean canContinueToUse() {
        // Continua o Goal até o tempo do bote acabar ou ela tocar o chão após o pulo
        PathfinderMob mob = grabber.asMob();
        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        
        if (this.target == null || !this.target.isAlive() || grabAbilityInstance == null) return false;
        if (grabAbilityInstance.grabbedEntity != null) return false; // Parar se já pegou
        
        // Se já passou de 5 ticks e ela tocou o chão de novo, o bote falhou e o goal reseta
        if (this.lungeTicks > 5 && mob.onGround()) return false;

        return this.lungeTicks < 25; // Timeout de segurança (pouco mais de 1 segundo)
    }

    @Override
    public void start() {
        PathfinderMob mob = grabber.asMob();
        this.lungeTicks = 0;

        if (this.target != null) {
            // Olha fixamente para o alvo antes do pulo
            mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            
            // --- Cálculo do Vetor do Lunge ---
            Vec3 mobPos = mob.position();
            Vec3 targetPos = this.target.position();
            Vec3 direction = new Vec3(targetPos.x - mobPos.x, 0.0D, targetPos.z - mobPos.z).normalize();
            
            // Aplica força horizontal (0.75) e um pequeno pulo vertical (0.25)
            mob.setDeltaMovement(direction.x * 0.75D, 0.25D, direction.z * 0.75D);
            mob.hasImpulse = true; // Avisa o jogo que a entidade recebeu um impulso externo
            
            // Opcional: Tocar um som de "ataque/pulo" aqui ficaria bem legal!
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.target = null;
        this.lungeTicks = 0;
        
        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        if (grabAbilityInstance != null && grabAbilityInstance.grabbedEntity == null) {
            if (grabAbilityInstance.getController() instanceof AbilityControllerAccessor accessor) {
                accessor.setHoldTicks(0); // Reseta caso o bote falhe
            }
        }
    }

    @Override
    public void tick() {
        PathfinderMob mob = grabber.asMob();
        this.lungeTicks++;

        if (this.target == null || mob.level().isClientSide()) return;

        // Sempre vira o rosto na direção do alvo durante o vôo
        mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        if (grabAbilityInstance == null) return;

        // Mantém a habilidade carregando agressivamente no ar
        if (grabAbilityInstance.getController() instanceof AbilityControllerAccessor accessor) {
            accessor.setHoldTicks(20);
        }

        // Verifica colisão ou proximidade estendida enquanto está no ar
        EntityDimensions dimensions = mob.getDimensions(mob.getPose()).scale(1.4f); // Área ligeiramente maior no ar
        AABB grabReach = dimensions.makeBoundingBox(mob.position());

        if (grabReach.contains(this.target.position()) || mob.distanceTo(this.target) <= 2.2F) {
            mayGrabEntity(this.target, grabAbilityInstance);
        }
    }

    private void mayGrabEntity(LivingEntity target, GrabEntityAbilityInstance grabAbilityInstance) {
        LivingEntity grabbedEntity = grabAbilityInstance.grabbedEntity;
        if (grabbedEntity == null && GrabEntityAbility.getGrabber(target) == null) {
            if (grabAbilityInstance.grabEntity(target)) {
                Changed.PACKET_HANDLER.send(
                        PacketDistributor.TRACKING_ENTITY.with(grabber::asMob),
                        new GrabEntityPacket(grabber.asMob(), target, GrabEntityPacket.GrabType.ARMS)
                );

                ProcessTransfur.forceNearbyToRetarget(target.level(), target);
                grabber.asMob().setTarget(null);

                ChangedSounds.broadcastSound(
                        grabber.asMob(),
                        ChangedSounds.LATEX_GRAB_ENTITY,
                        1.0f,
                        1.0f
                );

                grabber.applyGrabCooldown(0);
            }
        }

        // TODO: Check this
    }
}