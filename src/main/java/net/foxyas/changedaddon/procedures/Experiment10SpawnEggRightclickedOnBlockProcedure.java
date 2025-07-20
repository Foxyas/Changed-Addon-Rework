package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.Experiment10BossEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

public class Experiment10SpawnEggRightclickedOnBlockProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Direction direction, Entity entity, ItemStack itemstack) {
        if (direction == null || entity == null)
            return;
        if (direction == Direction.UP) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x + 0.5), (y + 1), (z + 0.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        } else if (direction == Direction.DOWN) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x + 0.5), (y - 1.5), (z + 0.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        } else if (direction == Direction.NORTH) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x + 0.5), y, (z - 0.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        } else if (direction == Direction.SOUTH) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x + 0.5), y, (z + 1.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        } else if (direction == Direction.WEST) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x - 0.5), y, (z + 0.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        } else if (direction == Direction.EAST) {
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = new Experiment10BossEntity(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), _level);
                entityToSpawn.moveTo((x + 1.5), y, (z + 0.5), world.getRandom().nextFloat() * 360F, 0);
                if (entityToSpawn instanceof Mob _mobToSpawn)
                    _mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                world.addFreshEntity(entityToSpawn);
            }
        }
        if (!(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                }
                return false;
            }
        }.checkGamemode(entity)) && !(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
                }
                return false;
            }
        }.checkGamemode(entity))) {
            if (entity instanceof Player _player) {
                ItemStack _stktoremove = itemstack;
                _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
            }
        }
        if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.glass.break")), SoundSource.NEUTRAL, 1, 1);
            } else {
                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.glass.break")), SoundSource.NEUTRAL, 1, 1, false);
            }
        }
    }
}
