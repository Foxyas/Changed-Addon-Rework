package net.foxyas.changedaddon.mixins.blocks;

import net.foxyas.changedaddon.ability.WindPassiveAbility;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class FluidsMixin {

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    public void injectCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        LiquidBlock thisFixed = (LiquidBlock) (Object) this;
        if (pContext instanceof EntityCollisionContext entityCollisionContext) {
            Entity entity = entityCollisionContext.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.isShiftKeyDown()) return;
                final VoxelShape wantedHitShape = Shapes.create(new AABB(0, 0.99, 0, 1, 1, 1));

                if ((livingEntity instanceof Player player)) {
                    TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
                    if (variant == null) return;
                    if (variant.is(ChangedAddonTransfurVariants.LATEX_WIND_CAT_MALE) || variant.is(ChangedAddonTransfurVariants.LATEX_WIND_CAT_FEMALE)) {
                        SimpleAbilityInstance ability = variant.getAbilityInstance(ChangedAddonAbilities.WIND_PASSIVE.get());
                        if (ability != null) {
                            if (ability.getAbility() instanceof WindPassiveAbility windPassiveAbility && windPassiveAbility.isActive) {
                                if (entityCollisionContext.isAbove(wantedHitShape, pPos, true) && player.fallDistance <= 2) {
                                    WindPassiveAbility.spawnAirParticles(player);
                                    cir.setReturnValue(wantedHitShape);
                                }
                            }
                        }
                    }
                } else {
                    var variant = TransfurVariant.getEntityVariant(livingEntity);
                    if (variant == null) return;
                    if (variant.is(ChangedAddonTransfurVariants.LATEX_WIND_CAT_MALE) || variant.is(ChangedAddonTransfurVariants.LATEX_WIND_CAT_FEMALE)) {
                        if (entityCollisionContext.isAbove(wantedHitShape, pPos, true) && livingEntity.fallDistance <= 2) {
                            WindPassiveAbility.spawnAirParticles(livingEntity);
                            cir.setReturnValue(wantedHitShape);
                        }
                    }
                }

            }
        }
    }

}
