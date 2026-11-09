package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimedKeypadItem extends BlockItem {

    public TimedKeypadItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    public TimedKeypadItem() {
        this(ChangedAddonBlocks.TIMED_KEYPAD.get(), new Properties());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        CompoundTag tag = pStack.getOrCreateTag();
        if (tag.contains("TimerValue")) {
            pTooltip.add(Component.translatable("block.changed_addon.timed_keypad.info", tag.getInt("TimerValue")));
        }
    }
}
