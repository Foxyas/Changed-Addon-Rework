package net.foxyas.changedaddon.datagen.animationAssociations;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.animation.AnimationCategory;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class TransfurAnimationAssociationsProvider extends AnimationAssociationsProvider {

    public static final ResourceLocation TRANSFUR_ANIMATION_EVENT = Changed.modResource("transfur");

    protected static final ResourceLocation VARIANT_FIELD = Changed.modResource("variant");
    protected static final ResourceLocation CAUSE_FIELD = Changed.modResource("cause");

    public TransfurAnimationAssociationsProvider(PackOutput packOutput, String modid) {
        super(packOutput, modid);
    }

    protected EventBuilder addTransfur(AnimationCategory category) {
        return add(TRANSFUR_ANIMATION_EVENT, category);
    }

    // --- Single Variant Overloads ---

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, ResourceLocation variantId, TransfurCause... causes) {
        return addTransfurAnimation(builder, animationName, new ResourceLocation[]{variantId}, causes, criteria -> {});
    }

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, TransfurVariant<?> variant, TransfurCause... causes) {
        return addTransfurAnimation(builder, animationName, new ResourceLocation[]{variant.getFormId()}, causes, criteria -> {});
    }

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, ResourceLocation variantId, TransfurCause[] causes, Consumer<CriteriaBuilder> extraCriteria) {
        return addTransfurAnimation(builder, animationName, new ResourceLocation[]{variantId}, causes, extraCriteria);
    }

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, TransfurVariant<?> variant, TransfurCause[] causes, Consumer<CriteriaBuilder> extraCriteria) {
        return addTransfurAnimation(builder, animationName, new ResourceLocation[]{variant.getFormId()}, causes, extraCriteria);
    }

    // --- Multiple Variants Overloads (TransfurVariant) ---

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, TransfurVariant<?>[] variants, TransfurCause... causes) {
        ResourceLocation[] variantIds = Arrays.stream(variants).map(TransfurVariant::getFormId).toArray(ResourceLocation[]::new);
        return addTransfurAnimation(builder, animationName, variantIds, causes, criteria -> {});
    }

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, TransfurVariant<?>[] variants, TransfurCause[] causes, Consumer<CriteriaBuilder> extraCriteria) {
        ResourceLocation[] variantIds = Arrays.stream(variants).map(TransfurVariant::getFormId).toArray(ResourceLocation[]::new);
        return addTransfurAnimation(builder, animationName, variantIds, causes, extraCriteria);
    }

    // --- Multiple Variants Overloads (ResourceLocation) ---

    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, ResourceLocation[] variantIds, TransfurCause... causes) {
        return addTransfurAnimation(builder, animationName, variantIds, causes, criteria -> {});
    }

    // Core processor method
    protected EventBuilder addTransfurAnimation(EventBuilder builder, ResourceLocation animationName, ResourceLocation[] variantIds, TransfurCause[] causes, Consumer<CriteriaBuilder> extraCriteria) {
        return builder.addAnimation(animationName, criteria -> {
            // Apply Variants (Single string or Array of strings)
            if (variantIds.length == 1) {
                criteria.put(VARIANT_FIELD, variantIds[0]);
            } else if (variantIds.length > 1) {
                criteria.put(VARIANT_FIELD, variantIds);
            }

            // Apply Causes (Single string or Array of strings)
            if (causes.length == 1) {
                criteria.put(CAUSE_FIELD, causes[0].getSerializedName());
            } else if (causes.length > 1) {
                String[] causeNames = Arrays.stream(causes)
                        .map(TransfurCause::getSerializedName)
                        .toArray(String[]::new);
                criteria.put(CAUSE_FIELD, causeNames);
            }

            extraCriteria.accept(criteria);
        });
    }
}