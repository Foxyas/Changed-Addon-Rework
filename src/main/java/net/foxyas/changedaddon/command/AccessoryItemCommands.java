package net.foxyas.changedaddon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.ltxprogrammer.changed.data.AccessorySlotContext;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.item.AccessoryItem;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.ltxprogrammer.changed.data.AccessorySlots.getForEntity;

public class AccessoryItemCommands {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_ACCESSORY_SLOTS =
            (context, builder) -> {

                // All registered slots (default fallback)
                List<String> allSlots = ChangedRegistry.ACCESSORY_SLOTS.get()
                        .getValues()
                        .stream()
                        .map(ChangedRegistry.ACCESSORY_SLOTS::getKey)
                        .filter(Objects::nonNull)
                        .map(ResourceLocation::toString)
                        .toList();

                // Try to resolve entities
                Collection<? extends Entity> entities =
                        EntityArgument.getOptionalEntities(context, "targets");

                // No entity selected yet -> suggest everything
                if (entities.isEmpty()) {
                    return SharedSuggestionProvider.suggest(allSlots, builder);
                }

                // Multiple entities selected -> suggest everything
                if (entities.size() > 1) {
                    return SharedSuggestionProvider.suggest(allSlots, builder);
                }

                // Single entity logic
                Entity entity = entities.iterator().next();

                if (!(entity instanceof LivingEntity living)) {
                    return SharedSuggestionProvider.suggest(allSlots, builder);
                }

                // AccessorySlots is Optional
                Optional<AccessorySlots> optionalSlots = getForEntity(living);

                // Entity has no accessory slots -> fallback to default
                if (optionalSlots.isEmpty()) {
                    return SharedSuggestionProvider.suggest(allSlots, builder);
                }

                AccessorySlots slots = optionalSlots.get();

                // Available slots for this specific entity
                List<String> entitySlots = slots.getSlotTypes()
                        .map((ChangedRegistry.ACCESSORY_SLOTS::getKey))
                        .filter(Objects::nonNull)
                        .map(ResourceLocation::toString)
                        .toList();

                // If empty for some reason, fallback to all slots
                if (entitySlots.isEmpty()) {
                    return SharedSuggestionProvider.suggest(allSlots, builder);
                }

                return SharedSuggestionProvider.suggest(entitySlots, builder);
            };
    private static final int MAX_FEEDBACK = 20;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(
                Commands.literal("accessory")
                        .requires(src -> src.hasPermission(2))

                        // /accessory set
                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("slot", ResourceLocationArgument.id())
                                                .suggests(SUGGEST_ACCESSORY_SLOTS)
                                                .then(Commands.argument("item", ItemArgument.item(buildContext))
                                                        .then(Commands.argument("forceSet", BoolArgumentType.bool())
                                                                .executes(AccessoryItemCommands::setAccessory)
                                                        )
                                                )
                                        )
                                )
                        )

                        // /accessory get
                        .then(Commands.literal("get")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("slot", ResourceLocationArgument.id())
                                                .suggests(SUGGEST_ACCESSORY_SLOTS)
                                                .executes(AccessoryItemCommands::getAccessory)
                                        )
                                )
                        )
        );
    }


    public static int setAccessory(CommandContext<CommandSourceStack> ctx)
            throws CommandSyntaxException {

        Collection<? extends Entity> entities =
                EntityArgument.getEntities(ctx, "targets");

        boolean multiple = entities.size() > 1;
        boolean forceSet = BoolArgumentType.getBool(ctx, "forceSet");

        ResourceLocation slotName = ResourceLocationArgument.getId(ctx, "slot");
        ItemStack stack = ItemArgument.getItem(ctx, "item")
                .createItemStack(1, false);

        int changed = 0;
        int shown = 0;
        boolean truncated = false;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity living)) continue;

            Optional<AccessorySlots> optionalSlots = getForEntity(living);

            // Error: Entity has no slots
            if (optionalSlots.isEmpty()) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                multiple
                                        ? "commands.changed_addon.accessory.no_slots.multiple"
                                        : "commands.changed_addon.accessory.no_slots.single",
                                entity.getDisplayName()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            AccessorySlots slots = optionalSlots.get();
            AccessorySlotType slotType =
                    ChangedRegistry.ACCESSORY_SLOTS.get().getValue(slotName);

            // Error: Slot type does not exist for entity
            if (slotType == null || slots.getSlotTypes().noneMatch(s -> s == slotType)) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                multiple
                                        ? "commands.changed_addon.accessory.invalid_slot.multiple"
                                        : "commands.changed_addon.accessory.invalid_slot.single",
                                entity.getDisplayName(),
                                slotName.toString()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            ItemStack copy = stack.copy();

            if (stack.isEmpty()) {
                // Success: clearing the slot
                slots.setItem(slotType, copy);
                changed++;

                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendSuccess(() ->
                                Component.translatable(
                                        "commands.changed_addon.accessory.set.success",
                                        entity.getDisplayName(),
                                        copy.getDisplayName(),
                                        slotName.toString()
                                ),
                        false
                );
                shown++;

                continue;
            }

            // Error: Slot does not accept this item
            if (!slotType.canHoldItem(copy, living)) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                "commands.changed_addon.accessory.set.invalid_item",
                                entity.getDisplayName(),
                                copy.getDisplayName(),
                                slotName.toString()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }


            // Error: The Accessory slot is not available
            boolean available = AccessorySlots.isSlotAvailable(living, slotType);

            if (!available && !forceSet) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                "commands.changed_addon.accessory.set.slot_locked",
                                entity.getDisplayName(),
                                copy.getDisplayName(),
                                slotName.toString()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            // Error: Another Accessory locks the slot
            boolean canReplaceSlot = canReplaceSlot(living, slotType, copy);
            if (!canReplaceSlot && !forceSet) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    continue;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                "commands.changed_addon.accessory.set.locked",
                                entity.getDisplayName(),
                                copy.getDisplayName(),
                                slotName.toString()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            // Success: applying the accessory
            slots.setItem(slotType, copy);
            changed++;

            if (shown > MAX_FEEDBACK) {
                truncated = true;
                continue;
            }

            ctx.getSource().sendSuccess(() ->
                            Component.translatable(
                                    "commands.changed_addon.accessory.set.success",
                                    entity.getDisplayName(),
                                    copy.getDisplayName(),
                                    slotName.toString()
                            ),
                    false
            );
            shown++;
        }

        if (truncated) {
            ctx.getSource().sendSuccess(() ->
                            Component.translatable(
                                    "commands.changed_addon.accessory.too_many",
                                    MAX_FEEDBACK
                            ),
                    false
            );
        }

        if (changed == 0) {
            throw new SimpleCommandExceptionType(
                    Component.literal("No valid accessory slots found")
            ).create();
        }

        return changed;
    }


    private static int getAccessory(CommandContext<CommandSourceStack> ctx)
            throws CommandSyntaxException {

        Collection<? extends Entity> entities =
                EntityArgument.getEntities(ctx, "targets");

        boolean multiple = entities.size() > 1;
        ResourceLocation slotName = ResourceLocationArgument.getId(ctx, "slot");

        int found = 0;
        int shown = 0;
        boolean truncated = false;

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity living)) continue;

            Optional<AccessorySlots> optionalSlots = getForEntity(living);

            if (optionalSlots.isEmpty()) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    break;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                multiple
                                        ? "commands.changed_addon.accessory.no_slots.multiple"
                                        : "commands.changed_addon.accessory.no_slots.single",
                                entity.getDisplayName()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            AccessorySlots slots = optionalSlots.get();
            AccessorySlotType slotType =
                    ChangedRegistry.ACCESSORY_SLOTS.get().getValue(slotName);

            if (slotType == null || slots.getSlotTypes().noneMatch(s -> s == slotType)) {
                if (shown > MAX_FEEDBACK) {
                    truncated = true;
                    break;
                }

                ctx.getSource().sendFailure(
                        Component.translatable(
                                multiple
                                        ? "commands.changed_addon.accessory.invalid_slot.multiple"
                                        : "commands.changed_addon.accessory.invalid_slot.single",
                                entity.getDisplayName(),
                                slotName.toString()
                        )
                );
                shown++;

                if (!multiple) break;
                continue;
            }

            found++;

            Optional<ItemStack> stack = slots.getItem(slotType);

            if (shown > MAX_FEEDBACK) {
                truncated = true;
                continue;
            }

            ctx.getSource().sendSuccess(() ->
                            Component.translatable(
                                    "commands.changed_addon.accessory.get.success",
                                    entity.getDisplayName(),
                                    stack.orElse(ItemStack.EMPTY).getDisplayName(),
                                    slotName.toString()
                            ),
                    false
            );
            shown++;

        }

        if (truncated) {
            ctx.getSource().sendSuccess(() ->
                            Component.translatable(
                                    "commands.changed_addon.accessory.too_many",
                                    MAX_FEEDBACK
                            ),
                    false
            );
        }

        if (found == 0) {
            throw new SimpleCommandExceptionType(
                    Component.literal("No valid accessory slots found")
            ).create();
        }

        return found;
    }

    public static boolean canReplaceSlot(LivingEntity livingEntity, AccessorySlotType slot, ItemStack itemStack) {
        Optional<AccessorySlots> optionalSlots = AccessorySlots.getForEntity(livingEntity);

        if (optionalSlots.isEmpty() || !optionalSlots.get().hasSlot(slot)) {
            return false;
        }

        AccessorySlots slots = optionalSlots.get();

        if (itemStack.isEmpty()) {
            return true;
        }

        return slots.getSlotTypes()
                .filter(otherSlot -> otherSlot != slot)
                .allMatch(otherSlot -> {
                    ItemStack otherStack = slots.getItem(otherSlot).orElse(ItemStack.EMPTY);

                    if (otherStack.isEmpty()) {
                        return true;
                    }

                    Item item = itemStack.getItem();
                    if (item instanceof AccessoryItem accessoryItem) {
                        if (!accessoryItem.allowedWith(itemStack, otherStack, livingEntity, slot, otherSlot)) {
                            return false;
                        }

                        if (accessoryItem.shouldDisableSlot(new AccessorySlotContext<>(livingEntity, slot, itemStack), otherSlot
                        )) {
                            return false;
                        }
                    }

                    Item otherItem = otherStack.getItem();
                    if (otherItem instanceof AccessoryItem accessoryItem) {
                        if (!accessoryItem.allowedWith(
                                otherStack, itemStack, livingEntity, otherSlot, slot)) {
                            return false;
                        }

                        return !accessoryItem.shouldDisableSlot(new AccessorySlotContext<>(livingEntity, otherSlot, otherStack), slot);
                    }

                    return true;
                });
    }

}