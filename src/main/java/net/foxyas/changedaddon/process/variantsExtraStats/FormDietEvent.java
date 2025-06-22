package net.foxyas.changedaddon.process.variantsExtraStats;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class FormDietEvent extends Event {

    private final TransfurVariant<?> variant;
    private final LivingEntity entity;
    private final boolean isGoodFood;
    private final ItemStack itemStack;
    public int additionalFood;
    public float additionalSaturation;

    /**
     * @param additionalFood is the additionalFood apply AFTER the player consume the food
     * @param additionalSaturation is the additionalSaturation apply AFTER the player consume the food
     *                             by default both values are -> (foodData.values/4)
     *                             change this value as you please but remember that this is the exactly value and not an additional
     *                             so this means that -> (additionalFood = 4) is a truly +4 of food level and not a (defaultValue + 4)
     */
    public FormDietEvent(TransfurVariant<?> variant, LivingEntity entity, boolean isGoodFood, ItemStack itemStack, int additionalFood, float additionalSaturation){
        this.variant = variant;
        this.entity = entity;
        this.isGoodFood = isGoodFood;
        this.itemStack = itemStack;
        this.additionalFood = additionalFood;
        this.additionalSaturation = additionalSaturation;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public TransfurVariant<?> getVariant() {
        return variant;
    }

    public boolean isGoodFood() {
        return isGoodFood;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
