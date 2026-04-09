package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedLeopardEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;

public class BorealisMaleEntity extends AbstractBasicOrganicChangedLeopardEntity {

    public BorealisMaleEntity(EntityType<? extends AbstractSnowLeopard> type, Level level) {
        super(type, level);
    }

    public BorealisMaleEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.BOREALIS_MALE.get(), world);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.05f);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.25);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return this.getRandom().nextBoolean() ? TransfurMode.ABSORPTION : TransfurMode.REPLICATION;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.getColor("#6682C1");
        Color3 secondColor = Color3.getColor("#1C2A4E");
        return ColorUtil.lerpTFColor(firstColor, secondColor, getUnderlyingPlayer());
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        Player player = this.getUnderlyingPlayer();
        if (player != null) {
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            GrabEntityAbilityInstance grabEntityAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor abilityExtensor) {
                abilityExtensor.setSafeMode(true);
            }
        }
    }

    public Color3 getDripColor() {
        return Color3.WHITE;
    }
}
