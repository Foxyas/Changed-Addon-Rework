package net.foxyas.changedaddon.event;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class ProgressTransfurEvents {

    public static class TickPlayerTransfurProgressEvent extends Event {

        private final Player player;

        public TickPlayerTransfurProgressEvent(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }


    public static class ProgressTransfurEvent extends Event {

        public final LivingEntity livingEntity;
        public final float amount;
        public final TransfurVariant<?> transfurVariant;
        public final TransfurContext context;
        public final boolean hit;

        public ProgressTransfurEvent(LivingEntity entity, float amount, TransfurVariant<?> transfurVariant, TransfurContext context, boolean hit) {
            this.livingEntity = entity;
            this.amount = amount;
            this.transfurVariant = transfurVariant;
            this.context = context;
            this.hit = hit;
        }

        public LivingEntity getLivingEntity() {
            return livingEntity;
        }

        public float getAmount() {
            return amount;
        }

        public TransfurVariant<?> getTransfurVariant() {
            return transfurVariant;
        }

        public TransfurContext getContext() {
            return context;
        }

        public boolean isHit() {
            return hit;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class NewlyTransfurred extends Event {

        public final LivingEntity livingEntity;
        public final TransfurVariantInstance<?> transfurVariantInstance;
        public final IAbstractChangedEntity iAbstractChangedEntity;

        public NewlyTransfurred(LivingEntity entity, TransfurVariantInstance<?> transfurVariantInstance) {
            this.livingEntity = entity;
            this.transfurVariantInstance = transfurVariantInstance;
            this.iAbstractChangedEntity = IAbstractChangedEntity.forEither(entity);
        }

        public NewlyTransfurred(IAbstractChangedEntity iAbstractChangedEntity) {
            this.iAbstractChangedEntity = iAbstractChangedEntity;
            this.livingEntity = iAbstractChangedEntity.getEntity();
            this.transfurVariantInstance = iAbstractChangedEntity.getTransfurVariantInstance();
        }

        public LivingEntity getLivingEntity() {
            return livingEntity;
        }

        public TransfurVariantInstance<?> getTransfurVariantInstance() {
            return transfurVariantInstance;
        }

        public IAbstractChangedEntity getiAbstractChangedEntity() {
            return iAbstractChangedEntity;
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }
}
