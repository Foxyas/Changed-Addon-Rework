package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LatexInsulatorEntityCollidesInTheBlockProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        entity.fallDistance = 0;
        if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
            if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 1) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 300, 0, false, false));
                if (entity instanceof LivingEntity _entity)
                    _entity.hurt(new DamageSource("latex_solvent").bypassArmor(), 1);
            }
        }
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                if (ProcessTransfur.isPlayerLatex(player)) {
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 120, 0, false, false));
                    if (player.getHealth() > 1) {
                        player.hurt(new DamageSource("latex_solvent").bypassArmor(), 1);
                    }
                } else {
                    if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns) {
                        if (entity instanceof Player _player && !_player.level.isClientSide())
                            _player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.untransfur.Immune").getString())), true);
                    }
                }
            }
        }
    }
}
