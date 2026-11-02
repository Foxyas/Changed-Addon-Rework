package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class TransfurVariantDietEvent extends Event {

    private final TransfurVariantInstance<?> transfurVariantInstance;
    private TransfurVariantDiet diet;
    private final LivingEntity entity;
    private final boolean hadAnyGoodDiet;
    private final ItemStack itemStack;

    public TransfurVariantDietEvent(TransfurVariantInstance<?> transfurVariantInstance, TransfurVariantDiet diet, LivingEntity entity, boolean hadAnyGoodDiet, ItemStack itemStack) {
        this.transfurVariantInstance = transfurVariantInstance;
        this.diet = diet;
        this.entity = entity;
        this.hadAnyGoodDiet = hadAnyGoodDiet;
        this.itemStack = itemStack;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public TransfurVariantInstance<?> getTransfurVariantInstance() {
        return transfurVariantInstance;
    }

    public TransfurVariant<?> getTransfurVariant() {
        return transfurVariantInstance.getParent();
    }

    public TransfurVariantDiet getDiet() {
        return diet;
    }

    public void setDiet(TransfurVariantDiet diet) {
        this.diet = diet;
    }

    public boolean hasAnyGoodFoodMatch() {
        return hadAnyGoodDiet;
    }

    public boolean isOffDietCheck() {
        return !hasAnyGoodFoodMatch();
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    // ==========================================
    // --- SUB-EVENTO: APLICAÇÃO DE COMIDA ---
    // ==========================================
    public static class ApplyFood extends TransfurVariantDietEvent {
        private final FoodDietEntry foodDietEntry;
        private int hunger;
        private float saturation;

        public ApplyFood(TransfurVariantInstance<?> transfurVariantInstance, TransfurVariantDiet diet, LivingEntity entity, boolean hadAnyGoodDiet, ItemStack itemStack, FoodDietEntry foodDietEntry, int hunger, float saturation) {
            super(transfurVariantInstance, diet, entity, hadAnyGoodDiet, itemStack);
            this.foodDietEntry = foodDietEntry;
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public FoodDietEntry getFoodDietEntry() {
            return foodDietEntry;
        }

        public int getHunger() {
            return hunger;
        }

        public void setHunger(int hunger) {
            this.hunger = hunger;
        }

        public float getSaturation() {
            return saturation;
        }

        public void setSaturation(float saturation) {
            this.saturation = saturation;
        }
    }

    // ==========================================
    // --- SUB-EVENTO: APLICAÇÃO DE EFEITO ---
    // ==========================================
    public static class ApplyMobEffect extends TransfurVariantDietEvent {
        private final MobEffectHolder effectHolder;
        @Nullable
        private final FoodDietEntry foodDietEntry;
        private MobEffectInstance mobEffectInstance;
        private final boolean isOffDiet;

        public ApplyMobEffect(TransfurVariantInstance<?> transfurVariantInstance,
                              TransfurVariantDiet diet,
                              LivingEntity entity,
                              boolean hadAnyGoodDiet,
                              ItemStack itemStack,
                              MobEffectHolder effectHolder,
                              @Nullable FoodDietEntry foodDietEntry,
                              MobEffectInstance mobEffectInstance,
                              boolean isOffDiet) {
            super(transfurVariantInstance, diet, entity, hadAnyGoodDiet, itemStack);
            this.effectHolder = effectHolder;
            this.foodDietEntry = foodDietEntry;
            this.mobEffectInstance = mobEffectInstance;
            this.isOffDiet = isOffDiet;
        }

        public MobEffectHolder getEffectHolder() {
            return effectHolder;
        }

        @Nullable
        public FoodDietEntry getFoodDietEntry() {
            return foodDietEntry;
        }

        public MobEffectInstance getMobEffectInstance() {
            return mobEffectInstance;
        }

        public void setMobEffectInstance(MobEffectInstance mobEffectInstance) {
            this.mobEffectInstance = mobEffectInstance;
        }

        public boolean isOffDiet() {
            return isOffDiet;
        }
    }
}