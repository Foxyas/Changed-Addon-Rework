package net.foxyas.changedaddon.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MathOperation implements StringRepresentable {
    ADD("add") {
        @Override
        public float apply(float base, float value) {
            return base + value;
        }
    },
    SUBTRACT("subtract") {
        @Override
        public float apply(float base, float value) {
            return base - value;
        }
    },
    MULTIPLY("multiply") {
        @Override
        public float apply(float base, float value) {
            return base * value;
        }
    },
    DIVIDE("divide") {
        @Override
        public float apply(float base, float value) {
            return value != 0 ? base / value : base;
        }
    },
    SET("set") {
        @Override
        public float apply(float base, float value) {
            return value;
        }
    };

    private final String name;

    MathOperation(String name) {
        this.name = name;
    }

    public abstract float apply(float base, float value);

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public static final Codec<MathOperation> CODEC = StringRepresentable.fromEnum(MathOperation::values);
}