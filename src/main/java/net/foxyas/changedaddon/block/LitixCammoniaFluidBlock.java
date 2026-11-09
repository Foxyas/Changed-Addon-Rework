package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LitixCammoniaFluidBlock extends LiquidBlock {

    public LitixCammoniaFluidBlock() {
        super(() -> (FlowingFluid) ChangedAddonFluids.LITIX_CAMMONIA_FLUID.get(),
                BlockBehaviour.Properties.copy(Blocks.WATER).mapColor(MapColor.SNOW).strength(100f)
        );
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 10);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void tick(BlockState blockstate, ServerLevel level, BlockPos origin, RandomSource random) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.set(origin);

        level.scheduleTick(pos, this, 10);
    }

    @Override
    public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
        Level level = entity.level();
        if (level.isClientSide()) return;

        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }

        if (livingEntity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.LATEX_SOLVENT.get(), 200, 0, true, false))) {
            if (!(livingEntity instanceof Player player) || !ChangedAddonVariables.ofOrDefault(player).showWarns) {
                return;
            }

            player.displayClientMessage(Component.translatable("changed_addon.untransfur.Immune.fluid"), true);
        }
    }
}
