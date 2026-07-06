package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LitixCamoniaItem extends Item {

    private static final ResourceLocation advLocation = ChangedAddonMod.resourceLoc("craft_litix_camonia");

    public LitixCamoniaItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull Player entity) {
        super.onCraftedBy(itemstack, world, entity);
        if (entity instanceof ServerPlayer _player) {
            Advancement advancement = _player.server.getAdvancements().getAdvancement(advLocation);
            assert advancement != null;
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(advancement);
            if (!_ap.isDone()) {
                for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(advancement, s);
            }
        }
    }
}
