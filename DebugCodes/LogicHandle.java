package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.abilities.ChangedAddonAbilitys;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

public class LogicHandle {

    public static boolean canPassThroughBlock(BlockState state, CollisionContext context){
        if (context instanceof EntityCollisionContext entityContext) {
            Entity collidingEntity = entityContext.getEntity();

            if (collidingEntity != null && state.is(BlockTags.create(ResourceLocation.parse("changed_addon:passable_blocks")))) {
                // Verifica se a entidade é um jogador
                if (collidingEntity instanceof Player player) {
                    // Verifica uma condição específica do jogador (no caso, ProcessTransfur)
                    if (ProcessTransfur.isPlayerLatex(player)) {
                        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                        return transfurVariantInstance.ifHasAbility(ChangedAddonAbilitys.SOFTEN_ABILITY.get(), (instance) -> {
                        }) && transfurVariantInstance.getAbilityInstance(ChangedAddonAbilitys.SOFTEN_ABILITY.get()).isActivate();
                        //SoftenAbilityInstance ability = ProcessTransfur.getPlayerTransfurVariant(player).getAbilityInstance(ChangedAddonAbilitys.SOFTEN_ABILITY.get());
                    }
                }
            }
        }
        return false;
    }

}
