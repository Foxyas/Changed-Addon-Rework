package net.foxyas.changedaddon.mixins.entity;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor {
    @Accessor("inGround")
    boolean inGround();

    @Accessor("inGround")
    void setInGround(boolean value);
}
