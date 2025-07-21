package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LitixCamoniaFluidMobplayerCollidesBlockProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;

        if (entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player)) {
            if (!ProcessTransfur.isPlayerNotLatex(player)) {
                if (!player.level.isClientSide())
                    player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 120, 0, false, false));
            } else {
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns) {
                    if (entity instanceof Player _player && !_player.level.isClientSide())
                        _player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.untransfur.Immune.fluid").getString())), true);
                }
            }
        } else {
            if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 120, 0, false, false));
        }
        if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
            if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 200, 0, false, false));
        }
    }
}
