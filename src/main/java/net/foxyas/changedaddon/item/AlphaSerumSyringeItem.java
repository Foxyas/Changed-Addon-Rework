package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.MathFormulasUtils;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AlphaSerumSyringeItem extends AbstractSyringeItem {

    public AlphaSerumSyringeItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64)
                .rarity(Rarity.RARE)
        );
    }

    @Override
    public void applyEffectsAfterUse(@NotNull ItemStack pStack, Level level, LivingEntity entity) {
        super.applyEffectsAfterUse(pStack, level, entity);

        if (!(entity instanceof Player player)) return;

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            failMessage(player);
            return;
        }

        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariant == null) {
            failMessage(player);
            return;
        }

        ChangedEntity changedEntity = transfurVariant.getChangedEntity();
        if (!(changedEntity instanceof IAlphaAbleEntity iAlphaAbleEntity)) {
            failMessage(player);
            return;
        }

        if (iAlphaAbleEntity.isAlpha()) {
            failMessage(player);
            return;
        }

        iAlphaAbleEntity.setAlpha(true);
        float rand = player.getRandom().nextFloat();
        float alphaScale = MathFormulasUtils.lerpEase(rand, 0.5f, 2.5f, MathFormulasUtils.EasingType.QUAD_IN);
        iAlphaAbleEntity.setAlphaScale(alphaScale);
    }

    private void failMessage(Player player) {
        ChangedAddonVariables.PlayerVariables playerVars = ChangedAddonVariables.ofOrDefault(player);
        if (playerVars.showWarns && !player.level.isClientSide())
            player.displayClientMessage(Component.translatable("changed_addon.untransfur.no_effect"), true);
    }
}
