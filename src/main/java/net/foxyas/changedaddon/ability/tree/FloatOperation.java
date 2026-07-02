package net.foxyas.changedaddon.ability.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;

public enum FloatOperation implements StringRepresentable, BiPredicate<Float, Float> {
    GREATER_THAN("greater_than", (left, right) -> left > right),
    GREATER_THAN_EQUAL_TO("greater_than_equal_to", (left, right) -> left >= right),
    LESS_THAN("less_than", (left, right) -> left < right),
    LESS_THAN_EQUAL_TO("less_than_equal_to", (left, right) -> left <= right),
    EQUAL_TO("equal_to", Objects::equals),
    NOT_EQUAL_TO("not_equal_to", (left, right) -> !Objects.equals(left, right));

    public static Codec<FloatOperation> CODEC = Codec.STRING.comapFlatMap(FloatOperation::fromSerial, FloatOperation::getSerializedName);
    public final String serialName;
    public final BiPredicate<Float, Float> predicate;

    private FloatOperation(String serialName, BiPredicate<Float, Float> predicate) {
        this.serialName = serialName;
        this.predicate = predicate;
    }

    public String getSerializedName() {
        return this.serialName;
    }

    public static DataResult<FloatOperation> fromSerial(String name) {
        return Arrays.stream(values()).filter((type) -> type.serialName.equals(name)).findFirst().map(DataResult::success).orElseGet(() -> DataResult.error(() -> name + " is not a valid Operation"));
    }

    public boolean test(Float left, Float right) {
        return this.predicate.test(left, right);
    }
}
