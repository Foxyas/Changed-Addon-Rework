package net.foxyas.changedaddon.entity.api;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;


public interface ISpeakableEntity {

    /**
     * @param component text that will be displayed.
     * @param targetToHear when null the entity will speak "alone" and whoever nearby will may hear it.
     * @return if anyone heard or not what the entity said.
     * */
    default boolean speak(Component component,@Nullable LivingEntity targetToHear) {
        return canSpeak();
    }

    /**
     * @param component text that will be displayed.
     * */
    default void sout(Component component) {
    }

    default float speakRange() {
        return 64;
    }

    default boolean canSpeak() {
        return true;
    }

    default MutableComponent getEntityChat(Component message) {
        if (this instanceof LivingEntity livingEntity) {
            return Component.translatable("chat.type.text", livingEntity.getDisplayName(), message);
        }
        return message.copy(); //Fail Safe
    }
}
