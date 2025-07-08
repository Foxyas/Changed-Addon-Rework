package net.foxyas.changedaddon.variants;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface IDynamicCoatColors extends ExtraVariantStats {
    default void setColor(int layer, Color3 color3){
        switch (layer) {
            case 1 -> setSecondaryColor(color3);
            case 2 -> setStripesColor(color3);
            default -> setPrimaryColor(color3);
        };
    }

    <T extends ChangedEntity> Color3 getCoatColor(@Nullable T entityState, int layer);

    Set<String> layersStyles();

    void setPrimaryColor(Color3 color);

    void setSecondaryColor(Color3 color);

    void setStripesColor(Color3 color);

    void setStyleOfColor(String styleOfColor);

    Color3 getPrimaryColor();

    Color3 getSecondaryColor();

    Color3 getThirdColor();

    String getStyleOfColor();

    default void readColors(CompoundTag originalTag) {
        CompoundTag tag = originalTag.getCompound("TransfurColorData");
        if (tag.contains("PrimaryColor")) setPrimaryColor(Color3.fromInt(tag.getInt("PrimaryColor")));
        if (tag.contains("SecondaryColor")) setSecondaryColor(Color3.fromInt(tag.getInt("SecondaryColor")));
        if (tag.contains("ThirdColor")) setStripesColor(Color3.fromInt(tag.getInt("ThirdColor")));
        if (tag.contains("StyleOfColor")) setStyleOfColor(tag.getString("StyleOfColor"));
    }

    default void saveColors(CompoundTag originalTag) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("PrimaryColor", getPrimaryColor().toInt());
        tag.putInt("SecondaryColor", getSecondaryColor().toInt());
        tag.putInt("ThirdColor", getThirdColor().toInt());
        tag.putString("StyleOfColor", getStyleOfColor());
        originalTag.put("TransfurColorData", tag);
    }


    static boolean playerHasTransfurWithExtraColors(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfur != null && transfur.is(ChangedAddonTransfurVariants.AVALI)) {
            return true;
        }
        return transfur != null && transfur.getChangedEntity() instanceof IDynamicCoatColors;
    }
}