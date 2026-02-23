package net.foxyas.changedaddon.mixins.entity;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract ItemStack getItem();

    @Unique
    public ItemEntity self() {
        return ((ItemEntity) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void injectTick(CallbackInfo ci) {
        if (this.getItem().is(ChangedAddonItems.TRANSFUR_TOTEM.get())) {
            self().setGlowingTag(true);
            if (self().lifespan == 6000) {
                self().lifespan = 10000;
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource pSource) {
        if (this.getItem().is(ChangedAddonItems.TRANSFUR_TOTEM.get())) {
            if (pSource.is(DamageTypes.CACTUS)) {
                return true;
            } else if (pSource.is(DamageTypes.LIGHTNING_BOLT)) {
                return true;
            } else if (pSource.is(DamageTypeTags.IS_EXPLOSION)) {
                return true;
            }
        }

        return super.isInvulnerableTo(pSource);
    }
}
