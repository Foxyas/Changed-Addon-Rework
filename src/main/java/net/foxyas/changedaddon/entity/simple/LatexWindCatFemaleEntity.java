package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class LatexWindCatFemaleEntity extends ChangedEntity implements GenderedEntity, PowderSnowWalkable {

    public LatexWindCatFemaleEntity(EntityType<? extends LatexWindCatFemaleEntity> p_19870_, Level level) {
        super(p_19870_, level);
    }


    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(22.0F);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.2f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.9f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(1.0F);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.3f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }


    public TransfurMode getTransfurMode() {
        return this.getRandom().nextBoolean() ? TransfurMode.ABSORPTION : TransfurMode.REPLICATION;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.getColor("#dfe6ec");
        Color3 secondColor = Color3.getColor("#87a5d4");
        return ColorUtil.lerpTFColor(firstColor, secondColor, this.getUnderlyingPlayer());
    }
}
