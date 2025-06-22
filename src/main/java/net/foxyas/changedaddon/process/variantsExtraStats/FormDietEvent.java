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
