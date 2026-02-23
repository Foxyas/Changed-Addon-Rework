package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.api.ExtraConditions;
import net.foxyas.changedaddon.entity.defaults.AbstractBasicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.HairStyle;
import net.ltxprogrammer.changed.entity.HairStyle.Collection;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueLizard extends AbstractBasicChangedEntity implements ExtraConditions.Climb, CustomPatReaction {
    public BlueLizard(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.BLUE_LIZARD.get(), world);
    }

    public BlueLizard(EntityType<? extends BlueLizard> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
    }

    @Override
    public boolean canClimb() {
        AABB box = this.getBoundingBox();

        // Pega a parte superior do corpo (ex: do meio até o topo)
        double minY = box.getCenter().y;
        double maxY = box.maxY;

        // Verifica colisão horizontal nas 4 direções
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Vec3 offset = Vec3.atLowerCornerOf(direction.getNormal()).scale(0.05); // Leve deslocamento para checar contato

            AABB checkBox = new AABB(
                    box.minX + offset.x, minY, box.minZ + offset.z,
                    box.maxX + offset.x, maxY, box.maxZ + offset.z
            );

            if (!this.level().noCollision(this, checkBox)) {
                return true;
            }
        }

        return false;
    }


    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.05f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1F);
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 2000;
    }

    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    public Color3 getDripColor() {
        return Color3.getColor("#00f3ff");
    }

    public Color3 getHairColor(int layer) {
        return Color3.WHITE;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.WHITE;
        Color3 secondColor = Color3.getColor("#00f3ff");
        return ColorUtil.lerpTFColor(firstColor, secondColor, this.getUnderlyingPlayer());
    }

    public @Nullable List<HairStyle> getValidHairStyles() {
        return Collection.EMPTY;
    }

    @Override
    public void WhenPattedReactionSpecific(Player patter, InteractionHand hand, Vec3 pattedLocation) {
        CustomPatReaction.super.WhenPattedReactionSpecific(patter, hand, pattedLocation);
        this.level.playSound(null, this, ChangedAddonSoundEvents.GECKO_BEEP.get(), SoundSource.AMBIENT, 1, 1);
    }
}
