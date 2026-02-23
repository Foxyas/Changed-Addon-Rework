package net.foxyas.changedaddon.entity.ai;

import com.mojang.serialization.DataResult;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum LatexAttackType implements BiPredicate<TamableLatexEntityFavors, LivingEntity>, StringRepresentable {
    ALWAYS_KILL("always_kill", (self, target) -> false),
    TRY_TRANSFUR("try_transfur", (self, target) -> {
        return target.getType().is(ChangedTags.EntityTypes.HUMANOIDS) || target instanceof ChangedEntity;
    });

    private final String serializedName;
    private final BiPredicate<TamableLatexEntityFavors, LivingEntity> predicate;

    LatexAttackType(String serializedName, BiPredicate<TamableLatexEntityFavors, LivingEntity> predicate) {
        this.serializedName = serializedName;
        this.predicate = predicate;
    }

    public static DataResult<LatexAttackType> fromSerial(String serializedName) {
        return Arrays.stream(values()).filter(value -> value.serializedName.equals(serializedName))
                .findAny().map(DataResult::success).orElse(DataResult.error(
                        () -> "Invalid attack type " + serializedName
                ));
    }

    @Override
    public boolean test(TamableLatexEntityFavors self, LivingEntity possibleTarget) {
        return predicate.test(self, possibleTarget);
    }

    public Predicate<LivingEntity> forEntity(TamableLatexEntityFavors self) {
        return target -> this.test(self, target);
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public LatexAttackType cycle() {
        if (this.ordinal() + 1 == values().length)
            return values()[0];
        else
            return values()[this.ordinal() + 1];
    }

    public Component getDisplayText() {
        return Component.translatable("changed.tamed_dark_latex.attacking." + serializedName);
    }
}
