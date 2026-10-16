package net.foxyas.changedaddon.util;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import com.google.common.base.Suppliers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ArmorPartMapper {
    private final Map<String, ModelPart> mappedParts = new HashMap<>();

    public ArmorPartMapper() {
        // Default all standard humanoid slots to empty parts
        clear("head");
        clear("hat");
        clear("body");
        clear("right_arm");
        clear("left_arm");
        clear("right_leg");
        clear("left_leg");
    }

    private void clear(String name) {
        mappedParts.put(name, new ModelPart(Collections.emptyList(), Collections.emptyMap()));
    }

    /**
     * Maps a custom ModelPart to a specific humanoid part name.
     */
    public ArmorPartMapper put(String partName, ModelPart part) {
        if (part != null) {
            mappedParts.put(partName, part);
        }
        return this;
    }

    // Convenience mapping methods for standard slots
    public ArmorPartMapper mapHead(ModelPart part) {
        return put("head", part);
    }

    public ArmorPartMapper mapHat(ModelPart part) {
        return put("hat", part);
    }

    public ArmorPartMapper mapBody(ModelPart part) {
        return put("body", part);
    }

    public ArmorPartMapper mapRightArm(ModelPart part) {
        return put("right_arm", part);
    }

    public ArmorPartMapper mapLeftArm(ModelPart part) {
        return put("left_arm", part);
    }

    public ArmorPartMapper mapRightLeg(ModelPart part) {
        return put("right_leg", part);
    }

    public ArmorPartMapper mapLeftLeg(ModelPart part) {
        return put("left_leg", part);
    }

    /**
     * Builds and wraps the result in a memoized Supplier.
     */
    public Supplier<HumanoidModel<?>> buildSupplier() {
        final Map<String, ModelPart> snapshot = new HashMap<>(this.mappedParts);
        return Suppliers.memoize(() -> new HumanoidModel<>(new ModelPart(Collections.emptyList(), snapshot)));
    }

    /**
     * Builds the HumanoidModel directly.
     */
    public HumanoidModel<?> build() {
        return new HumanoidModel<>(new ModelPart(Collections.emptyList(), new HashMap<>(this.mappedParts)));
    }
}