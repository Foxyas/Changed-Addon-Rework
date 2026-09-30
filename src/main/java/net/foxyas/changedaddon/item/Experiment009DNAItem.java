package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.item.api.ICustomGlowingOutline;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.awt.*;

public class Experiment009DNAItem extends ItemWithDescription implements ICustomGlowingOutline {

    public Experiment009DNAItem() {
        super(new Item.Properties()
                .stacksTo(64).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public boolean canBeHurtBy(DamageSource pSource) {
        if (pSource.is(DamageTypes.CACTUS) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        return super.canBeHurtBy(pSource);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        boolean returnValue = super.onEntityItemUpdate(stack, entity);
        entity.setGlowingTag(true);
        if (entity.lifespan == 6000) {
            entity.lifespan = 10000;
        }
        return returnValue;
    }

    @Override
    public Color getEntityItemOutlineColor() {
        return new Color(0, 255, 255, 255);
    }
}
