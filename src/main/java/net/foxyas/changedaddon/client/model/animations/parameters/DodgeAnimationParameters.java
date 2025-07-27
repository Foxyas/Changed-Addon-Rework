package net.foxyas.changedaddon.client.model.animations.parameters;

import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.block.StasisChamber;
import net.ltxprogrammer.changed.entity.animation.AnimationAssociation;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.minecraft.world.entity.LivingEntity;

public class DodgeAnimationParameters implements AnimationParameters {
    public static DodgeAnimationParameters INSTANCE = new DodgeAnimationParameters();

    public static Codec<DodgeAnimationParameters> CODEC = Codec.unit(() -> INSTANCE);

    private DodgeAnimationParameters() {}

    @Override
    public AnimationAssociation.Match matchesAssociation(AnimationAssociation association) {
        return AnimationAssociation.Match.DEFAULT;
    }

    @Override
    public boolean shouldEndAnimation(LivingEntity livingEntity, float totalTime) {
        return totalTime > 0.5f;
    }

    @Override
    public boolean shouldLoop(LivingEntity livingEntity, float totalTime) {
        return false;
    }
}