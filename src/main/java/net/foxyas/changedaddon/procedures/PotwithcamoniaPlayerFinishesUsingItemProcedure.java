package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PotwithcamoniaPlayerFinishesUsingItemProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof Player player) {
            boolean transfured = ProcessTransfur.isPlayerTransfurred(player);
            boolean organic = ProcessTransfur.isPlayerNotLatex(player);
            if (transfured && !organic) {
                if (!player.level.isClientSide())
                    player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 1200, 0, false, false));
            }
            if (organic) {
                if (transfured) {
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 1800, 0, false, false));
                    if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns) {
                        if (entity instanceof Player _player && !_player.level.isClientSide())
                            _player.displayClientMessage(new TextComponent("for some reason this seems to have slowed effect"), true);
                    }
                }
            }
        }
    }
}
