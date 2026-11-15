package net.foxyas.changedaddon.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ModLivingEvent {

    /**
     * Fired when a {@link LivingEntity} swings its arm/hand.
     * <p>
     * This event is cancelable. If canceled, the entity will not swing its hand.
     * You can also change which hand is swung using {@link #setHand(InteractionHand)}.
     */
    public static class LivingEntitySwingEvent extends LivingEvent {

        protected final InteractionHand originalInteractionHand;
        public InteractionHand hand;

        /**
         * Constructs a new LivingEntitySwingEvent.
         *
         * @param entity The living entity swinging its hand.
         * @param hand   The initial hand being swung.
         */
        public LivingEntitySwingEvent(LivingEntity entity, InteractionHand hand) {
            super(entity);
            this.originalInteractionHand = hand;
            this.hand = originalInteractionHand;
        }

        /**
         * Gets the original hand that triggered the swing action before any event modifications.
         *
         * @return The original {@link InteractionHand}.
         */
        public InteractionHand getOriginalInteractionHand() {
            return originalInteractionHand;
        }

        /**
         * Gets the current hand that will be swung.
         *
         * @return The current {@link InteractionHand}.
         */
        public InteractionHand getHand() {
            return hand;
        }

        /**
         * Sets the hand that will be swung by the entity.
         *
         * @param hand The new {@link InteractionHand} to use.
         */
        public void setHand(InteractionHand hand) {
            this.hand = hand;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}
