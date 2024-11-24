package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.foxyas.changedaddon.configuration.ChangedAddonConfigsConfiguration;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BlockBehaviour.class)
public abstract class BlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void getCollisionModShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity collidingEntity = entityContext.getEntity();

            if (collidingEntity != null && state.is(BlockTags.create(new ResourceLocation("changed_addon:passable_blocks")))) {
                // Verifica se a entidade é um jogador
                if (collidingEntity instanceof Player player) {
                    // Verifica uma condição específica do jogador (no caso, ProcessTransfur)
                    if (ProcessTransfur.isPlayerLatex(player)) {
                        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                        if (transfurVariantInstance.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()) != null
                                && transfurVariantInstance.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get()).grabbedEntity == null) {

                            //Config
                            if (ChangedAddonConfigsConfiguration.CAN_PASS_THROUGH_BLOCKS.get()) {
                                cir.setReturnValue(Shapes.empty());
                            }

                            //Ability
                            transfurVariantInstance.ifHasAbility(ChangedAddonAbilitys.SOFTEN_ABILITY.get(), Instance -> {
                                if (Instance.isActivate()) {
                                    // Se for um jogador Latex, permite que ele atravesse a barra de ferro (forma vazia)
                                    cir.setReturnValue(Shapes.empty()); // Colisão desativada para jogadores Latex
                                }
                            });
                            //SoftenAbilityInstance ability = ProcessTransfur.getPlayerTransfurVariant(player).getAbilityInstance(ChangedAddonAbilitys.SOFTEN_ABILITY.get());
                        }
                    }
                }
            }
        }
    }
}