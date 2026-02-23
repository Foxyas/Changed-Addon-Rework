package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class PollenCarryAbilityInstance extends AbstractAbilityInstance {

    private int withPollenTicks;

    public PollenCarryAbilityInstance(AbstractAbility<PollenCarryAbilityInstance> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    public boolean canUse() {
        return this.ability.canUse(this.entity);
    }

    public boolean canKeepUsing() {
        return this.ability.canKeepUsing(this.entity);
    }

    public void startUsing() {
        LivingEntity livingEntity = entity.getEntity();
        HitResult entityBlockHitLookingAt = livingEntity.pick(livingEntity instanceof Player player ? player.getEntityReach() : 4, 1, false);
        if (entityBlockHitLookingAt.getType() != HitResult.Type.MISS && entityBlockHitLookingAt instanceof BlockHitResult blockHitResult) {
            Level level = livingEntity.level();
            BlockState blockState = level.getBlockState(blockHitResult.getBlockPos());
            Item item = blockState.getBlock().asItem();
            ItemStack itemStack = new ItemStack(item);
            if (itemStack.is(ItemTags.FLOWERS) || blockState.is(BlockTags.FLOWER_POTS)) {
                withPollenTicks = 120;
                livingEntity.swing(InteractionHand.MAIN_HAND);
                entity.displayClientMessage(Component.translatable("ability.changed_addon.pollen_carry.display.can"), true);
            } else {
                entity.displayClientMessage(Component.translatable("ability.changed_addon.pollen_carry.display.cant"), true);
            }
        }
    }

    @Override
    public void tickIdle() {
        if (withPollenTicks <= 0) return;

        LivingEntity livingEntity = entity.getEntity();
        Level level = livingEntity.level();
        if (level instanceof ServerLevel serverLevel) {
            if (livingEntity.tickCount % 10 == 0) {
                ParticlesUtil.sendParticles(serverLevel, ParticleTypes.FALLING_NECTAR, livingEntity.position().add(0, 1, 0), 0.3f, 0.3f, 0.3f, 5, 1);
                growNearbyCrops(serverLevel, livingEntity);
                withPollenTicks--;
            }
        }
    }

    private void growNearbyCrops(ServerLevel serverLevel, LivingEntity entity) {
        BlockPos basePos = entity.blockPosition();

        for (int i = -1; i <= 2; i++) {
            BlockPos pos = basePos.below(i);
            BlockState state = serverLevel.getBlockState(pos);

            if (!state.is(BlockTags.BEE_GROWABLES)) continue;

            boolean grown = tryGrowCrop(serverLevel, pos, state)
                    || tryGrowStem(serverLevel, pos, state)
                    || tryGrowBerryBush(serverLevel, pos, state)
                    || tryGrowCaveVine(serverLevel, pos, state);

            if (grown) {
                serverLevel.levelEvent(2005, pos, 0); // Partículas tipo bone meal
            }
        }
    }

    private boolean tryGrowCrop(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof CropBlock crop && !crop.isMaxAge(state)) {
            IntegerProperty age = CropBlock.AGE;
            level.setBlockAndUpdate(pos, state.setValue(age, state.getValue(age) + 1));
            return true;
        }
        return false;
    }

    private boolean tryGrowStem(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof StemBlock stem) {
            int age = state.getValue(StemBlock.AGE);
            if (age < 7) {
                level.setBlockAndUpdate(pos, state.setValue(StemBlock.AGE, age + 1));
                return true;
            }
        }
        return false;
    }

    private boolean tryGrowBerryBush(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.SWEET_BERRY_BUSH)) {
            int age = state.getValue(SweetBerryBushBlock.AGE);
            if (age < 3) {
                level.setBlockAndUpdate(pos, state.setValue(SweetBerryBushBlock.AGE, age + 1));
                return true;
            }
        }
        return false;
    }

    private boolean tryGrowCaveVine(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.CAVE_VINES) || state.is(Blocks.CAVE_VINES_PLANT)) {
            ((BonemealableBlock) state.getBlock()).performBonemeal(level, level.getRandom(), pos, state);
            return true;
        }
        return false;
    }


    public void tick() {
        this.ability.tick(this.entity);
    }

    public void stopUsing() {
        this.ability.stopUsing(this.entity);
    }

    public void onRemove() {
        this.ability.onRemove(this.entity);
    }

    @Override
    public void saveData(CompoundTag tag) {
        tag.putInt("withPollenTicks", withPollenTicks);
    }

    @Override
    public void readData(CompoundTag tag) {
        withPollenTicks = tag.getInt("withPollenTicks");
    }
}
