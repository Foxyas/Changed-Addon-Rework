package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.animations.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChangedAddonAnimationsDefinitions {
    public static final AnimationDefinition DODGE_WEAVE = AnimationDefinition.Builder.withLength(0.5F).withTransition(0f)
            .addAnimation(Limb.HEAD, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(16.0F, 14.0F, -3.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.HEAD, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.25F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.TORSO, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-7.0F, 10.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.TORSO, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.2F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_ARM, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(10.0F, 10.0F, -5.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_ARM, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.LEFT_ARM, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-16.0F, -7.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.LEFT_ARM, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_LEG, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_LEG, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.9F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.LEFT_LEG, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(4.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.LEFT_LEG, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.9F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .build();

    public static final AnimationDefinition DODGE_RIGHT = AnimationDefinition.Builder.withLength(0.5F).withTransition(0f)
            .addAnimation(Limb.LEFT_LEG, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(-1.8698F, 20.5492F, 0.5652F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-4.6693F, 41.78F, 1.813F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-11.6481F, 17.4154F, 3.0488F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-11.6481F, 17.4154F, 3.0488F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM)
            ))
            .addAnimation(Limb.LEFT_LEG, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-0.19F, -0.23F, -0.91F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(2.7F, -1.0F, -4.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(2.7F, -1.0F, -4.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_LEG, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.0417F, KeyframeAnimations.degreeVec(5.25F, 26.31F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(20.3356F, 36.5204F, 11.9884F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(48.2844F, 64.9128F, 52.092F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(48.2844F, 64.9128F, 52.092F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(28.291F, 44.0637F, 35.1278F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM)
            ))
            .addAnimation(Limb.RIGHT_LEG, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.0417F, KeyframeAnimations.posVec(-0.21F, -0.07F, 2.84F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(6.4F, -0.4F, 3.1F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(6.4F, -0.4F, 3.1F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.2917F, KeyframeAnimations.posVec(4.16F, -0.34F, 2.49F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.TORSO, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(33.2318F, 39.4556F, 19.2452F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(50.6685F, 60.158F, 28.9726F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(50.6685F, 60.158F, 28.9726F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.2917F, KeyframeAnimations.degreeVec(45.0228F, 53.9232F, 26.4566F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.375F, KeyframeAnimations.degreeVec(37.2766F, 49.2053F, 22.5815F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(14.8067F, 28.6223F, 5.9259F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM)
            ))
            .addAnimation(Limb.TORSO, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.posVec(0.53F, -0.95F, -3.57F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(1.0F, -2.0F, -4.5F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(1.0F, -2.0F, -4.5F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.375F, KeyframeAnimations.posVec(1.03F, -1.07F, -2.56F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.posVec(0.82F, -0.48F, -1.16F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.LEFT_ARM, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(-29.6378F, 32.9421F, -11.2727F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-29.6378F, 32.9421F, -11.2727F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM)
            ))
            .addAnimation(Limb.LEFT_ARM, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(-2.3F, -4.2F, -9.7F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(-2.3F, -4.2F, -9.7F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_ARM, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(72.5882F, 29.8029F, 56.1268F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(72.5882F, 29.8029F, 56.1268F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.RIGHT_ARM, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(4.1F, 0.0F, 1.6F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(4.1F, 0.0F, 1.6F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .addAnimation(Limb.HEAD, new AnimationChannel(AnimationChannel.Target.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(75.8049F, 60.7506F, 65.6208F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(75.8049F, 60.7506F, 65.6208F), AnimationChannel.Interpolation.CATMULLROM),
                    new Keyframe(0.4583F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.CATMULLROM)
            ))
            .addAnimation(Limb.HEAD, new AnimationChannel(AnimationChannel.Target.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(2.1F, -1.3F, -4.5F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.posVec(2.1F, -1.3F, -4.5F), AnimationChannel.Interpolation.LINEAR),
                    new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolation.LINEAR)
            ))
            .build();
}
