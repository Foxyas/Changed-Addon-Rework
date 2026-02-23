package net.foxyas.changedaddon.entity.ai;

import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;

public enum LatexFavor implements StringRepresentable {
    NONE("none"),
    FISHING("fishing"),
    CAVING("caving"),
    SUIT_OWNER("suit_owner");

    private final String serializedName;

    LatexFavor(String serializedName) {
        this.serializedName = serializedName;
    }

    public static DataResult<LatexFavor> fromSerial(String serializedName) {
        return Arrays.stream(values()).filter(value -> value.serializedName.equals(serializedName))
                .findAny().map(DataResult::success).orElse(DataResult.error(
                        () -> "Invalid favor " + serializedName
                ));
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }
}
