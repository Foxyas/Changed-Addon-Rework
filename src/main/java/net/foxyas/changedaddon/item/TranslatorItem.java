package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TranslatorItem extends Item {

    private static final String TAG_ENABLED = "Enabled";

    public TranslatorItem() {
        super(new Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON));
    }

    public static boolean isEnabled(ItemStack stack) {
        return !stack.hasTag() || stack.getOrCreateTag().getBoolean(TAG_ENABLED);
    }

    /* ===== STATE ===== */

    public static void setEnabled(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(TAG_ENABLED, value);
    }

    public static void toggle(ItemStack stack) {
        setEnabled(stack, !isEnabled(stack));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            @NotNull InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ChangedSounds.broadcastSound(player, ChangedSounds.KEYPAD_CLICK, 1, 1);
            toggle(stack);
            player.displayClientMessage(
                    Component.translatable(
                            isEnabled(stack)
                                    ? "item.changedaddon.translator.on"
                                    : "item.changedaddon.translator.off"
                    ),
                    true
            );
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}

