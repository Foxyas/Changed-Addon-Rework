package net.foxyas.changedaddon.procedures;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class PainiteOreBlockDestroyedByPlayerProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null || isCreative(entity)) return;
        if (!(entity instanceof LivingEntity living)) return;

        ItemStack tool = living.getMainHandItem();
        if (!(tool.getItem() instanceof PickaxeItem)) return;

        boolean hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
        int tierLevel = tool.getItem() instanceof TieredItem tiered ? tiered.getTier().getLevel() : 0;

        if (!hasSilkTouch && tierLevel >= 4 && world instanceof Level level && !level.isClientSide()) {
            level.addFreshEntity(new ExperienceOrb(level, x, y, z, 35));
        }
    }

    private static boolean isCreative(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
        } else if (entity.level.isClientSide() && entity instanceof Player player) {
            var info = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
            return info != null && info.getGameMode() == GameType.CREATIVE;
        }
        return false;
    }
}
