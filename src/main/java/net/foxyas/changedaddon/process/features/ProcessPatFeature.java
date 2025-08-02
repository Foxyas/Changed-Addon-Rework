package net.foxyas.changedaddon.process.features;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.changedaddon.init.ChangedAddonItems.DARK_LATEX_COAT;
import static net.foxyas.changedaddon.init.ChangedAddonItems.DARK_LATEX_HEAD_CAP;

public class ProcessPatFeature {

    public static void ProcessPat(LevelAccessor world, Entity entity) {
        if (entity instanceof Player player) {
            //player.displayClientMessage(Component.literal("TEST"), false);
            PatFeatureHandle.execute(world, player);
        }
    }

    public static class GlobalPatReaction extends Event {
        public final Player player;
        public final LivingEntity target;

        public final LevelAccessor world;

        public GlobalPatReaction(LevelAccessor world, Player player, LivingEntity target) {
            this.player = player;
            this.target = target;
            this.world = world;
        }

        public boolean isCancelable() {
            return true;
        }
    }


    @Mod.EventBusSubscriber
    public static class HandleGlobalPatReaction {
        @SubscribeEvent
        public static void HandlePat(GlobalPatReaction globalPatReaction) {
            Player player = globalPatReaction.player;
            LivingEntity target = globalPatReaction.target;
            if (globalPatReaction.world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HEART, target.getX(), target.getY() + 1, target.getZ(), 4, 0.3, 0.3, 0.3, 1);
            } else {
                SpawnEmote(player, target);
            }
        }
    }

    public static void SpawnEmote(Player player, LivingEntity target) {
        if (target instanceof ChangedEntity changedEntity) {
            if (changedEntity.getTarget() == player) {
                return;
            }
            if (shouldBeConfused(player, target)) {
                player.getLevel().addParticle(ChangedParticles.emote(changedEntity, Emote.CONFUSED),
                        target.getX(), target.getY() + (double) target.getDimensions(target.getPose()).height + 0.65, target.getZ(),
                        0.0f, 0.0f, 0.0f);
            } else {
                player.getLevel().addParticle(ChangedParticles.emote(changedEntity, Emote.HEART),
                        target.getX(), target.getY() + (double) target.getDimensions(target.getPose()).height + 0.65, target.getZ(),
                        0.0f, 0.0f, 0.0f);
            }
        }
    }

    private static boolean shouldBeConfused(Player player, Entity entity) {
        if (entity instanceof AbstractDarkLatexWolf) {
            // Verificando se o jogador usa a armadura correta
            return player.getItemBySlot(EquipmentSlot.HEAD).is(DARK_LATEX_HEAD_CAP.get())
                    && player.getItemBySlot(EquipmentSlot.CHEST).is(DARK_LATEX_COAT.get());
        }
        return false;
    }

}
