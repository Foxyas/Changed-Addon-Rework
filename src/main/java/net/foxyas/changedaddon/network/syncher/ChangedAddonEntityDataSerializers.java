package net.foxyas.changedaddon.network.syncher;

import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ai.DarkLatexAttackCondition;
import net.ltxprogrammer.changed.entity.ai.DarkLatexAttackType;
import net.ltxprogrammer.changed.entity.ai.DarkLatexFavor;
import net.ltxprogrammer.changed.entity.ai.DarkLatexTargetType;
import net.ltxprogrammer.changed.entity.decoration.WallSignVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class ChangedAddonEntityDataSerializers {
    
    public static final EntityDataSerializer<DarkLatexTargetType> LATEX_TARGET_TYPE = EntityDataSerializer.simpleEnum(DarkLatexTargetType.class);
    public static final EntityDataSerializer<DarkLatexAttackType> LATEX_ATTACK_TYPE = EntityDataSerializer.simpleEnum(DarkLatexAttackType.class);
    public static final EntityDataSerializer<DarkLatexAttackCondition> LATEX_ATTACK_CONDITION = EntityDataSerializer.simpleEnum(DarkLatexAttackCondition.class);
    public static final EntityDataSerializer<DarkLatexFavor> LATEX_FAVOR = EntityDataSerializer.simpleEnum(DarkLatexFavor.class);

    static {
        EntityDataSerializers.registerSerializer(LATEX_TARGET_TYPE);
        EntityDataSerializers.registerSerializer(LATEX_ATTACK_TYPE);
        EntityDataSerializers.registerSerializer(LATEX_ATTACK_CONDITION);
        EntityDataSerializers.registerSerializer(LATEX_FAVOR);
    }
}
