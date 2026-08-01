package net.foxyas.changedaddon.mixins.level.pathfinder;

import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PathNavigation.class)
public interface PathNavigationAccessor {

    @Accessor("isStuck")
    void setIsStuck(boolean isStuck);

    @Accessor("isStuck")
    boolean isStuck();

    @Accessor("speedModifier")
    double getSpeedModifier();
}
