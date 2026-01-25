package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class Experiment009SpawnerItem extends SpecialSpawnEggItem {

    public Experiment009SpawnerItem() {
        super(ChangedAddonEntities.EXPERIMENT_009_BOSS, new Item.Properties().tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB).stacksTo(4).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    protected void postSpawn(ServerLevel level, Player player, Entity spawnedEntity) {
        level.playSound(null, player, SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1, 1);
        if (spawnedEntity instanceof Mob mob && mob.canAttack(player)) mob.setTarget(player);
    }
}
