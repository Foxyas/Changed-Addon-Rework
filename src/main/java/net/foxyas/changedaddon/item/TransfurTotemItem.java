package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.item.tooltip.TransfurTotemTooltipComponent;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.foxyas.changedaddon.util.TitleUtils;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.item.VariantHoldingBase;
import net.ltxprogrammer.changed.process.Pale;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.StackUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class TransfurTotemItem extends Item implements VariantHoldingBase {

    public static final AttributeModifier TOTEM_BUFF_ATTACK = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba751"), "transfur_stats", 4, AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier TOTEM_BUFF_DEFENSE = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba752"), "transfur_stats", 8, AttributeModifier.Operation.ADDITION);
    public static final AttributeModifier TOTEM_BUFF_ARMOR = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba753"), "transfur_stats", 6, AttributeModifier.Operation.ADDITION);

    public TransfurTotemItem() {
        super(new Item.Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }

    private static boolean tryLinkForm(Level level, Player player, ItemStack itemstack) {
        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
        ResourceLocation latexFormRes = tf == null ? null : tf.getFormId();
        if (latexFormRes == null) return false;

        String latexForm = latexFormRes.toString();

        if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() || latexFormRes.getNamespace().equals(Changed.MODID)) {
            linkForm(level, player, itemstack, tf, latexForm);
            return true;
        } else if (latexFormRes.getNamespace().equals(ChangedAddonMod.MODID)) {
            applyCooldownForTotem(player, itemstack, 50);
            visualActivate(level, player, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR);
            player.displayClientMessage(Component.translatable("changed_addon.latex_totem.not_valid"), true);
            return true;
        } else if (latexForm.startsWith("changed:special") && TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getFormId).noneMatch(form -> form.equals(latexFormRes))) {
            linkForm(level, player, itemstack, tf, ChangedTransfurVariants.FALLBACK_VARIANT.get().getFormId());
            return true;
        }

        return false;
    }

    private static void linkForm(Level level, Player player, ItemStack stack, TransfurVariantInstance<?> tf, String form) {
        CompoundTag itemStackTag = stack.getOrCreateTag();
        itemStackTag.putString("form", form);
        itemStackTag.putBoolean("curedFromPale", Pale.getPaleExposure(player) < 0);

        CompoundTag variantData = tf.save();
        variantData.remove("previousAttributes");
        variantData.remove("newAttributes");
        variantData.remove("transfurProgressionO");
        variantData.remove("transfurProgression");
        itemStackTag.put("TransfurVariantData", variantData);
        activateVisuals(level, player, stack, null, 100, SoundEvents.BEACON_ACTIVATE);
    }

    private static void linkForm(Level level, Player player, ItemStack stack, TransfurVariantInstance<?> tf, ResourceLocation form) {
        linkForm(level, player, stack, tf, form.toString());
    }

    private static void applyCooldownForTotem(Player entity, ItemStack itemstack, int ticks) {
        if (!entity.getAbilities().instabuild) entity.getCooldowns().addCooldown(itemstack.getItem(), ticks);
    }

    private static void activateVisuals(Level level, Player entity, ItemStack itemstack, String advancement, int cooldown, SoundEvent soundEvent) {
        activateVisuals(level, entity, itemstack, advancement, cooldown, soundEvent, 1f);
    }

    private static void activateVisuals(Level level, Player entity, ItemStack itemstack, String advancement, int cooldown, SoundEvent soundEvent, float pitch) {
        if (level.isClientSide())
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);

        applyCooldownForTotem(entity, itemstack, cooldown);
        if (soundEvent != null) visualActivate(level, entity, soundEvent, pitch);

        if (advancement != null)
            grantAdvancement(entity, advancement);
    }

    private static void visualActivate(Level level, Player player, SoundEvent sound) {
        visualActivate(level, player, sound, 1f);
    }

    private static void visualActivate(Level level, Player player, SoundEvent sound, float pitch) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1, pitch);
    }

    private static void grantAdvancement(Entity entity, String id) {
        if (!(entity instanceof ServerPlayer player)) return;

        Advancement adv = player.server.getAdvancements().getAdvancement(ResourceLocation.parse(id));
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        if (!progress.isDone())
            for (String criterion : progress.getRemainingCriteria()) player.getAdvancements().award(adv, criterion);
    }

    private static void addModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) return;

        if (!attributeInstance.hasModifier(modifier)) attributeInstance.addTransientModifier(modifier);
    }

    private static void removeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) return;

        if (attributeInstance.hasModifier(modifier)) attributeInstance.removeModifier(modifier);
    }

    public static float itemPropertyFunc(Entity entity) {
        if (!(entity instanceof Player player)) return 0;

        var instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null || isNotBenign(instance)) return 0;

        return 0.5f;
    }

    private static boolean isNotBenign(TransfurVariantInstance<?> instance) {
        boolean lacksBenignTag = !instance.getParent().getEntityType().is(ChangedTags.EntityTypes.BENIGN_LATEXES);
        boolean isNeitherWolfNorOrca = !instance.is(ChangedTransfurVariants.LATEX_BENIGN_WOLF) && !instance.is(ChangedTransfurVariants.LATEX_BENIGN_ORCA);
        return lacksBenignTag || isNeitherWolfNorOrca;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        if (Syringe.getVariant(pStack) == null
                || !pStack.getOrCreateTag().contains("form")
                || getTransfurVariantIdOfTotem(pStack).isBlank()) {
            return super.getTooltipImage(pStack);
        }
        return Optional.of(new TransfurTotemTooltipComponent(pStack));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag isAdvanced) {
        CompoundTag itemTag = stack.getOrCreateTag();
        String form = itemTag.getString("form");
        if (form.isEmpty()) {
            tooltip.add(1, (Component.translatable("item.changed_addon.transfur_totem.no_form_linked")));
            return;
        }

        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(ResourceLocation.parse(form));
        if (variant == null) {
            tooltip.add(1, (Component.translatable("item.changed_addon.transfur_totem.no_form_linked")));
            return;
        }

        if (Screen.hasShiftDown() && !Screen.hasAltDown() && !Screen.hasControlDown()) {
            tooltip.add(1, Component.literal(("§6Form=" + itemTag.getString("form"))));
        } else if (Screen.hasAltDown() && Screen.hasControlDown()) {
            tooltip.add(1, (Component.translatable("item.changed_addon.transfur_totem.desc_1")));
        } else {
            String ID = Syringe.getVariantDescriptionId(stack);
            tooltip.add(1, Component.literal(("§6(" + Component.translatable(ID).getString() + ")")));
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BLOCK;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        return new ItemStack(this);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemstack) {
        String form = getTransfurVariantIdOfTotem(itemstack);
        if (form.isEmpty()) return false;

        ResourceLocation parse = ResourceLocation.parse(form);
        TransfurVariant<?> value = ChangedRegistry.TRANSFUR_VARIANT.getValue(parse);
        if (value != null) {
            return parse.getNamespace().equals("changed") || ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get();
        }

        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel)) return InteractionResultHolder.pass(stack);

        boolean isValidUse = (player.getOffhandItem().is(stack.getItem()) && (player.getMainHandItem().is(stack.getItem())))
                || (player.getOffhandItem().is(stack.getItem()) && player.getMainHandItem().isEmpty())
                || player.getMainHandItem().is(stack.getItem());

        if (!isValidUse) return InteractionResultHolder.pass(stack);

        CompoundTag tag = stack.getOrCreateTag();
        String form = tag.getString("form");
        boolean isTransfurred = ProcessTransfur.isPlayerTransfurred(player);

        if (player.isShiftKeyDown()) {
            if (!form.isEmpty()) {
                tag.remove("form");
                if (tag.contains("TransfurVariantData")) tag.remove("TransfurVariantData");
                activateVisuals(level, player, stack, null, 50, SoundEvents.BEACON_DEACTIVATE);
                return InteractionResultHolder.consume(stack);
            }

            if (isTransfurred) return tryLinkForm(level, player, stack) ? InteractionResultHolder.consume(stack) : InteractionResultHolder.pass(stack);
            return InteractionResultHolder.pass(stack);
        }

        if (form.isEmpty()) {
            player.displayClientMessage(Component.literal("No form linked, please link one with §e<Shift+Click>"), true);
            return InteractionResultHolder.pass(stack);
        }

        if (isTransfurred) {
            PlayerUtil.unTransfurPlayerAndSpawnParticles(player);
            applyCooldownForTotem(player, stack, 100);
            visualActivate(level, player, ChangedAddonSoundEvents.UNTRANSFUR.get());
            grantAdvancement(player, "changed_addon:transfur_totem_advancement_1");
            return InteractionResultHolder.consume(stack);
        }

        if (tag.contains("TransfurVariantData")) {
            CompoundTag data = tag.getCompound("TransfurVariantData");
            PlayerUtil.transfurPlayerAndLoadData(player, form, data, 0.85f);
            // 0.85f to avoid issues with the transfur animation and because is design choice
        } else PlayerUtil.transfurPlayer(player, form, 0.85f);

        activateVisuals(level, player, stack, "changed_addon:transfur_totem_advancement_1", 100, null);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player player, @NotNull LivingEntity targetEntity, @NotNull InteractionHand pUsedHand) {
        if (pUsedHand != player.getUsedItemHand()) return InteractionResult.PASS;

        Level level = player.level;

        ItemStack totem = player.getMainHandItem();
        if (!totem.is(ChangedAddonItems.TRANSFUR_TOTEM.get()) || !getTransfurVariantIdOfTotem(totem).isEmpty())
            totem = player.getOffhandItem();
        if (!totem.is(ChangedAddonItems.TRANSFUR_TOTEM.get()) || !getTransfurVariantIdOfTotem(totem).isEmpty())
            return InteractionResult.PASS;

        if (player.getCooldowns().isOnCooldown(ChangedAddonItems.TRANSFUR_TOTEM.get()) || !player.isShiftKeyDown())
            return InteractionResult.PASS;

        if (targetEntity instanceof Player target) {
            if (!ProcessTransfur.isPlayerTransfurred(target)) return InteractionResult.PASS;

            String transfurId = ProcessTransfur.getPlayerTransfurVariant(target).getFormId().toString();

            if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get()) {
                setTransfurVariantForTotem(totem, transfurId);
                activateVisuals(level, player, totem, null, 20, SoundEvents.BEACON_ACTIVATE);
                return InteractionResult.SUCCESS;
            }

            if (transfurId.startsWith("changed:form")) {
                setTransfurVariantForTotem(totem, transfurId);
                activateVisuals(level, player, totem, null, 20, SoundEvents.BEACON_ACTIVATE);
                return InteractionResult.SUCCESS;
            }

            if (transfurId.startsWith("changed_addon:form")) {
                // Note: pitch 0 here matches the original behavior; may be intentional or a typo worth revisiting.
                activateVisuals(level, player, totem, null, 50, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, 0f);
                if (!target.level.isClientSide()) {
                    target.displayClientMessage(Component.translatable("changed_addon.latex_totem.not_valid"), true);
                }
                return InteractionResult.SUCCESS;
            }
        } else if (targetEntity instanceof ChangedEntity changedEntity) {
            String formId = changedEntity.getSelfVariant() != null ? changedEntity.getSelfVariant().getFormId().toString() : "";

            setTransfurVariantForTotem(totem, formId);
            activateVisuals(level, player, totem, null, 20, SoundEvents.BEACON_ACTIVATE);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.CONSUME;
    }

    private static @NotNull String getTransfurVariantIdOfTotem(ItemStack totem) {
        return totem.getOrCreateTag().getString("form");
    }

    private static void setTransfurVariantForTotem(ItemStack totem, String string) {
        totem.getOrCreateTag().putString("form", string);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, level, entity, slot, selected);

        if (!(entity instanceof Player player)) return;
        boolean isHoldingTotem = player.getMainHandItem().is(itemstack.getItem()) || player.getOffhandItem().is(itemstack.getItem());

        if (player instanceof ServerPlayer serverPlayer) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
            boolean isTransfurred = variant != null;
            if (isTransfurred && isHoldingTotem) {
                addModifier(serverPlayer, Attributes.ATTACK_DAMAGE, TOTEM_BUFF_ATTACK);
                addModifier(serverPlayer, Attributes.ARMOR, TOTEM_BUFF_DEFENSE);
                addModifier(serverPlayer, Attributes.ARMOR_TOUGHNESS, TOTEM_BUFF_ARMOR);
            } else {
                removeModifier(serverPlayer, Attributes.ATTACK_DAMAGE, TOTEM_BUFF_ATTACK);
                removeModifier(serverPlayer, Attributes.ARMOR, TOTEM_BUFF_DEFENSE);
                removeModifier(serverPlayer, Attributes.ARMOR_TOUGHNESS, TOTEM_BUFF_ARMOR);
            }
        }

        if (player.getCooldowns().isOnCooldown(itemstack.getItem()) || !ProcessTransfur.isPlayerTransfurred(player)
                || isNotBenign(ProcessTransfur.getPlayerTransfurVariant(player)))
            return;

        if (isHoldingTotem) {

        }

        PlayerUtil.unTransfurPlayerAndSpawnParticles(player);

        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1, 1);

        player.getCooldowns().addCooldown(itemstack.getItem(), 100);
        if (level.isClientSide()) Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);

        if (entity instanceof ServerPlayer serverPlayer) {
            player.displayClientMessage(Component.literal("The totem you were carrying has been activated"), true);
            ChangedAddonCriteriaTriggers.SIMPLE_ID_TRIGGER.trigger(serverPlayer, "untransfur.from:benign_latex");
        }
    }

    @Override
    public boolean canBeHurtBy(DamageSource pSource) {
        if (pSource.is(DamageTypes.CACTUS) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        return super.canBeHurtBy(pSource);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        boolean update = super.onEntityItemUpdate(stack, entity);
        if (!entity.level.isClientSide() && entity.tickCount % 5 == 0) {
            entity.setGlowingTag(true);
            if (entity.lifespan == 6000) {
                entity.lifespan = 10000;
            }
        }
        return update;
    }

    @Override
    public Item getOriginalItem() {
        return this;
    }

    @Override
    public void fillItemList(Predicate<TransfurVariant<?>> predicate, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {}

    @Mod.EventBusSubscriber
    public static class EventHandler {

        @SubscribeEvent
        public static void onLightning(EntityStruckByLightningEvent event) {
            if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

            if (itemEntity.getItem().is(ChangedAddonItems.TRANSFUR_TOTEM.get())) event.setCanceled(true);
        }

        @SubscribeEvent
        public static void onKeepConsciousEvent(ProcessTransfur.KeepConsciousEvent event) {
            Player player = event.player;
            if (event.shouldKeepConscious || player == null) return;

            boolean totemFound = false;
            for (ItemStack stack : player.getInventory().items) {
                if (!stack.is(ChangedAddonItems.TRANSFUR_TOTEM.get())) continue;

                totemFound = true;
                break;
            }

            if (!totemFound) return;

            if (ProcessTransfur.getPlayerTransfurVariant(player) != null && StackUtil.callStackContainsClass(WhiteLatexTransportInterface.class, 15))
                return;

            event.shouldKeepConscious = true;
            if (player instanceof ServerPlayer serverPlayer) {
                Component text = Component.translatable("changed_addon.latex_totem.tittle.text_1");
                Component text2 = Component.translatable("changed_addon.latex_totem.tittle.text_2");
                TitleUtils.sendTitleAndReset(serverPlayer, text, Component.empty(), 20, 30, 20);
                serverPlayer.displayClientMessage(text, false);
                serverPlayer.displayClientMessage(text2, false);
            }
        }
    }
}