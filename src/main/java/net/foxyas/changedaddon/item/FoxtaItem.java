package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.item.api.IDrinkItem;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class FoxtaItem extends BlockItem implements SpecializedItemRendering , IDrinkItem {

    private static final Cacheable<ResourceLocation> GUIMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("foxta_gui"),
                            "inventory"
                    )
            ));

    private static final Cacheable<ResourceLocation> HANDMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("foxta_hand"),
                            "inventory"
                    )
            ));

    private static final Cacheable<ResourceLocation> GROUNDMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("foxta_ground"),
                            "inventory"
                    )
            ));


    public FoxtaItem() {
        super(ChangedAddonBlocks.FOXTA_CAN.get(), new Item.Properties()
                ////.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)

                .stacksTo(64)
                .rarity(Rarity.RARE)
                .food(new FoodProperties.Builder()
                        .nutrition(9)
                        .saturationMod(1f)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 2), 0.25F)
                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 1), 0.25F)
                        .build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        if (pContext.getPlayer() != null && pContext.getPlayer().isShiftKeyDown()) {
            return super.useOn(pContext);
        } else {
            InteractionResult result = this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
            return result == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : result;
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack retval = super.finishUsingItem(itemstack, world, entity);
        if (entity.getRandom().nextFloat() <= 0.001f) {
            ProcessTransfur.progressTransfur(entity, 15, ChangedAddonTransfurVariants.FOXTA_FOXY.get(), TransfurContext.hazard(TransfurCause.FACE_HAZARD));
        }
        return retval;
    }

    @Override
    public ResourceLocation getModelLocation(ItemStack itemStack, ItemDisplayContext type) {
        return type == ItemDisplayContext.GUI || type == ItemDisplayContext.FIXED ? GUIMODEL.get()
                : type == ItemDisplayContext.GROUND ? GROUNDMODEL.get() : HANDMODEL.get();
    }

    @Override
    public void loadSpecialModels(Consumer<ResourceLocation> consumer) {
        consumer.accept(GUIMODEL.get());
        consumer.accept(HANDMODEL.get());
        consumer.accept(GROUNDMODEL.get());
    }
}
