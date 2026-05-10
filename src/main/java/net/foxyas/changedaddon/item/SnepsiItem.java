package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.item.api.IDrinkItem;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

public class SnepsiItem extends BlockItem implements SpecializedItemRendering, IDrinkItem {

    private static final Cacheable<ResourceLocation> GUIMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("snepsi_gui"),
                            "inventory"
                    )
            ));

    private static final Cacheable<ResourceLocation> HANDMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("snepsi_hand"),
                            "inventory"
                    )
            ));

    private static final Cacheable<ResourceLocation> GROUNDMODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("snepsi_ground"),
                            "inventory"
                    )
            ));


    public SnepsiItem() {
        super(ChangedAddonBlocks.SNEPSI_CAN.get(), new Item.Properties()
                //.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64)
                .rarity(Rarity.RARE)
                .food(new FoodProperties.Builder()
                        .nutrition(9)
                        .saturationMod(1f)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 2), 0.25F)
                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 1), 0.25F)
                        .effect(() -> new MobEffectInstance(ChangedAddonMobEffects.LATEX_CONTAMINATION.get(), 20 * 15, 3), 0.25F)
                        .build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
        ItemStack retval = super.finishUsingItem(itemstack, world, entity);

        if (!(entity instanceof ServerPlayer sPlayer)) return retval;

        // Distância percorrida no ar
        String form = itemstack.getOrCreateTag().getString("form");
        TransfurVariant<?> var = switch (form) {
            case "changed_addon:form_latex_snow_leopard_partial" ->
                    ChangedAddonTransfurVariants.SNOW_LEOPARD_PARTIAL.get();
            case "changed_addon:form_exp2/male" -> ChangedAddonTransfurVariants.Gendered.EXP2.getMaleVariant();
            case "changed_addon:form_exp2/female" -> ChangedAddonTransfurVariants.Gendered.EXP2.getFemaleVariant();
            case "changed_addon:form_exp6" -> ChangedAddonTransfurVariants.EXP6.get();
            case "changed_addon:form_latex_snep" -> ChangedAddonTransfurVariants.LATEX_SNEP.get();
            default ->
                    sPlayer.getRandom().nextFloat() <= 0.001f ? ChangedAddonTransfurVariants.SNEPSI_LEOPARD.get() : ChangedAddonTransfurVariants.SNOW_LEOPARD_PARTIAL.get();
        };

        ProcessTransfur.progressTransfur(sPlayer, 15, var, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
        return retval;
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
