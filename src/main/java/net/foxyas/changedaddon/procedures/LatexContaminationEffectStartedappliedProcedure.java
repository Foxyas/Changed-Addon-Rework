package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

import java.util.UUID;

public class LatexContaminationEffectStartedappliedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        double LatexSolvent_level = 0;
        double LatexContamination_level = 0;
        AttributeModifier LatexContamination = null;
        if (entity instanceof Player) {
            if (entity instanceof LivingEntity && ((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXINFECTION.get()) != null) {
                LatexContamination_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get())
                        ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier()
                        : 0) == 0
                        ? 0.1
                        : (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) * 0.1
                        + 0.1;
                LatexContamination = new AttributeModifier(UUID.fromString("0-0-0-0-1"), "Latex Contamination Effect Attribute Change", LatexContamination_level, AttributeModifier.Operation.ADDITION);
                if (!(((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXINFECTION.get()).hasModifier(LatexContamination)))
                    ((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXINFECTION.get()).addTransientModifier(LatexContamination);
                if (!(new Object() {
                    public boolean checkGamemode(Entity _ent) {
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                            return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                        } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                            return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                    && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                        }
                        return false;
                    }
                }.checkGamemode(entity)) && !(new Object() {
                    public boolean checkGamemode(Entity _ent) {
                        if (_ent instanceof ServerPlayer _serverPlayer) {
                            return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                        } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                            return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
                                    && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                        }
                        return false;
                    }
                }.checkGamemode(entity))) {
                    float LatexC = (float) LatexContamination_level;
                    float Math = 0.5f * LatexC;
                    AddTransfurProgressProcedure.setAdd(entity, Math);
                }
            }
        }
    }
}
