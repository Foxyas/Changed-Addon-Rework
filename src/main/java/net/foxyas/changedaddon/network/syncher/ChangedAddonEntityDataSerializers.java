package net.foxyas.changedaddon.network.syncher;

import net.foxyas.changedaddon.entity.ai.LatexAttackCondition;
import net.foxyas.changedaddon.entity.ai.LatexAttackType;
import net.foxyas.changedaddon.entity.ai.LatexFavor;
import net.foxyas.changedaddon.entity.ai.LatexTargetType;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class ChangedAddonEntityDataSerializers {
    
    public static final EntityDataSerializer<LatexTargetType> LATEX_TARGET_TYPE = EntityDataSerializer.simpleEnum(LatexTargetType.class);
    public static final EntityDataSerializer<LatexAttackType> LATEX_ATTACK_TYPE = EntityDataSerializer.simpleEnum(LatexAttackType.class);
    public static final EntityDataSerializer<LatexAttackCondition> LATEX_ATTACK_CONDITION = EntityDataSerializer.simpleEnum(LatexAttackCondition.class);
    public static final EntityDataSerializer<LatexFavor> LATEX_FAVOR = EntityDataSerializer.simpleEnum(LatexFavor.class);

    static {
        EntityDataSerializers.registerSerializer(LATEX_TARGET_TYPE);
        EntityDataSerializers.registerSerializer(LATEX_ATTACK_TYPE);
        EntityDataSerializers.registerSerializer(LATEX_ATTACK_CONDITION);
        EntityDataSerializers.registerSerializer(LATEX_FAVOR);
    }
}
