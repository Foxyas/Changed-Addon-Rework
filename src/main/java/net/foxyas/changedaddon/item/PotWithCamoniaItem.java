package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PotWithCamoniaItem extends Item {

    public PotWithCamoniaItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(4).saturationMod(2f).alwaysEat().build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack retval = new ItemStack(Items.GLASS_BOTTLE);
        super.finishUsingItem(itemstack, level, entity);

        if (entity instanceof Player player) {
            boolean transfurred = ProcessTransfur.isPlayerTransfurred(player);
            boolean organic = ProcessTransfur.isPlayerNotLatex(player);
            if (transfurred && !organic) {
                if (!player.level.isClientSide())
                    player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 1200, 0, false, false));
            }
            if (organic) {
                if (transfurred) {
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 1800, 0, false, false));
                    if ((entity.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonVariables.PlayerVariables())).showWarns) {
                        if (entity instanceof Player _player && !_player.level.isClientSide())
                            _player.displayClientMessage(Component.literal("for some reason this seems to have slowed effect"), true);
                    }
                }
            }
        }

        if (itemstack.isEmpty()) {
            return retval;
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(retval))
                    player.drop(retval, false);
            }
            return itemstack;
        }
    }
}
