package net.foxyas.changedaddon.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SpecialSpawnEggItem extends Item {

    private final Supplier<? extends EntityType<?>> supplier;

    public SpecialSpawnEggItem(Supplier<? extends EntityType<?>> supplier, Properties properties) {
        super(properties);
        this.supplier = supplier;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !(context.getLevel() instanceof ServerLevel level)) return InteractionResult.PASS;

        Direction direction = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();

        Entity entityToSpawn = supplier.get().create(level);
        if (entityToSpawn == null) return InteractionResult.FAIL;//This shouldn't happen!
        float yRot = player.getRandom().nextFloat() * 360F;

        switch (direction) {
            case UP -> entityToSpawn.moveTo(x + 0.5, y + 1, z + 0.5, yRot, 0);
            case DOWN -> entityToSpawn.moveTo(x + 0.5, y - 1.5, z + 0.5, yRot, 0);
            case EAST -> entityToSpawn.moveTo(x + 1.5, y, z + 0.5, yRot, 0);
            case WEST -> entityToSpawn.moveTo(x - 0.5, y, z + 0.5, yRot, 0);
            case NORTH -> entityToSpawn.moveTo(x + 0.5, y, z - 0.5, yRot, 0);
            case SOUTH -> entityToSpawn.moveTo(x + 0.5, y, z + 1.5, yRot, 0);
        }

        if(entityToSpawn instanceof Mob mob) mob.finalizeSpawn(level, level.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(entityToSpawn);

        if (!player.isCreative() && !player.isSpectator()) context.getItemInHand().shrink(1);

        postSpawn(level, player, entityToSpawn);
        level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        return InteractionResult.SUCCESS;
    }

    protected void postSpawn(ServerLevel level, Player player, Entity spawnedEntity){}
}
