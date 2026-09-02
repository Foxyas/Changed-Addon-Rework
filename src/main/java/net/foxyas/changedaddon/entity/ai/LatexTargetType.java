//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.foxyas.changedaddon.entity.ai;

import com.mojang.serialization.DataResult;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.ChangedTags.EntityTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Deprecated
public enum LatexTargetType implements BiPredicate<TamableLatexEntityFavors, LivingEntity>, StringRepresentable {
    TRANSFURABLE_ENTITIES("transfurable_entities", (self, target) -> {
        if (target != self.getOwner() && self.getOwner() != null) {
            LatexType targetLatexType = LatexType.getEntityLatexType(target);
            if (targetLatexType != null && self.getSelf().getLatexType().isHostileTo(targetLatexType)) {
                return true;
            } else {
                return target.getType().is(EntityTypes.HUMANOIDS) || target instanceof ChangedEntity;
            }
        } else {
            return false;
        }
    }),
    MONSTERS("monsters", (self, target) -> {
        if (target != self.getOwner() && self.getOwner() != null) {
            LatexType targetLatexType = LatexType.getEntityLatexType(target);
            if (targetLatexType != null && self.getSelf().getLatexType().isHostileTo(LatexType.getEntityLatexType(target))) {
                return true;
            } else {
                return target.getType().getCategory() == MobCategory.MONSTER;
            }
        } else {
            return false;
        }
    }),
    HOSTILE_TO_OWNER("hostile_to_owner", (self, target) -> {
        if (target != self.getOwner() && self.getOwner() != null) {
            if (target instanceof Mob mob) {
                return mob.getTarget() == self.getOwner();
            } else {
                return self.getOwner().getLastAttacker() == target;
            }
        } else {
            return false;
        }
    });

    private final String serializedName;
    private final BiPredicate<TamableLatexEntityFavors, LivingEntity> predicate;

    LatexTargetType(String serializedName, BiPredicate<TamableLatexEntityFavors, LivingEntity> predicate) {
        this.serializedName = serializedName;
        this.predicate = predicate;
    }

    public static DataResult<LatexTargetType> fromSerial(String serializedName) {
        return Arrays.stream(values()).filter((value) -> value.serializedName.equals(serializedName)).findAny().map(DataResult::success).orElse(DataResult.error(() -> "Invalid target type " + serializedName));
    }

    public boolean test(TamableLatexEntityFavors self, LivingEntity possibleTarget) {
        return this.predicate.test(self, possibleTarget);
    }

    public Predicate<LivingEntity> forEntity(TamableLatexEntityFavors self) {
        return (target) -> this.test(self, target);
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public Component getDisplayText() {
        return Component.translatable("changed.tamed_dark_latex.targeting." + this.serializedName);
    }

    public LatexTargetType cycle() {
        return this.ordinal() + 1 == values().length ? values()[0] : values()[this.ordinal() + 1];
    }
}
