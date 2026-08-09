package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.event.TransfurEvents;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.item.clothes.DyeableClothingItem;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.GenderedEntity;
import net.ltxprogrammer.changed.init.ChangedAccessorySlots;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.item.AccessoryItem;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.foxyas.changedaddon.util.DynamicClipContext.IGNORE_TRANSLUCENT;

@ParametersAreNonnullByDefault
public interface ChangedEntityExtension {

    static ChangedEntityExtension of(ChangedEntity entity) {
        return (ChangedEntityExtension) entity;
    }

    static boolean isNeutralTo(ChangedEntity entity, LivingEntity target) {
        return of(entity).isNeutralTo(target);
    }

    default boolean isPacified() {
        return false;
    }

    default void setPacified(boolean value) {
    }

    default boolean isNeutralTo(LivingEntity target) {
        return false;
    }

    default List<Item> getAcceptedSpawnClothes(ChangedEntity changedEntity) {
        List<Item> acceptedSpawnClothes = new ArrayList<>();
        acceptedSpawnClothes.add(ChangedItems.BENIGN_SHORTS.get());
        acceptedSpawnClothes.add(ChangedItems.BLACK_TSHIRT.get());
        if (changedEntity instanceof GenderedEntity gendered) {
            if (gendered.getGender() == Gender.FEMALE) {
                acceptedSpawnClothes.add(ChangedItems.SPORTS_BRA.get());
            }
        } else if (changedEntity.getRandom().nextFloat() <= 0.01) {
            acceptedSpawnClothes.add(ChangedItems.SPORTS_BRA.get()); // Funny Easter egg... may be removed before release
        }
        acceptedSpawnClothes.add(ChangedAddonItems.DYEABLE_SHORTS.get());
        acceptedSpawnClothes.add(ChangedAddonItems.DYEABLE_TSHIRT.get());
        return acceptedSpawnClothes;
    }

    default boolean shouldAlwaysHoldInGrab(@Nullable LivingEntity grabbedEntity, GrabEntityAbilityInstance grabEntityAbilityInstance) {
        if (grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor entityAbilityExtensor) {
            if (entityAbilityExtensor.isSafeMode()) {
                if (grabbedEntity == null) {
                    return false;
                }
                return !(grabbedEntity instanceof Player);
            }
        }
        return false;
    }

    default void setDefaultClothing(ChangedEntity changedEntity) {
        Optional<AccessorySlots> accessorySlots = AccessorySlots.getForEntity(changedEntity);
        accessorySlots.ifPresent((slots) -> {
            for (AccessorySlotType accessorySlotType : ChangedRegistry.ACCESSORY_SLOTS.get()) {
                if (slots.hasSlot(accessorySlotType)) {
                    Optional<ItemStack> item = slots.getItem(accessorySlotType);
                    if (item.isEmpty() || item.get().isEmpty()) {
                        List<Item> acceptedSpawnClothes = this.getAcceptedSpawnClothes(changedEntity);
                        if (acceptedSpawnClothes.isEmpty()) return;

                        List<Item> itemStream = acceptedSpawnClothes.stream()
                                .filter(thisItem -> thisItem instanceof AccessoryItem accessory &&
                                        accessory.allowedInSlot(new ItemStack(thisItem), changedEntity, accessorySlotType))
                                .toList();

                        ItemStack stack = getRandomItemFromList(changedEntity, itemStream);
                        if (stack.getItem() instanceof AccessoryItem accessoryItem) {
                            if (accessoryItem instanceof DyeableClothingItem dyeableClothes) {
                                DyeableClothingItem.DefaultColors color = Util.getRandom(DyeableClothingItem.DefaultColors.values(), changedEntity.getRandom());
                                dyeableClothes.setColor(stack, color.getColorToInt());
                            }
                            boolean flag = accessorySlotType.canHoldItem(stack, changedEntity);
                            do stack = getRandomItemFromList(changedEntity, itemStream); while (stack.isEmpty());
                            if (flag) slots.setItem(accessorySlotType, stack);

                            if (accessorySlotType == ChangedAccessorySlots.BODY.get() || stack.is(ChangedItems.SPORTS_BRA.get())) {
                                ItemStack randomItemFromList = getRandomItemFromList(changedEntity, acceptedSpawnClothes.stream().filter((item1) -> item1 instanceof AccessoryItem accessory && accessory.allowedInSlot(new ItemStack(item1), changedEntity, ChangedAccessorySlots.LEGS.get())).toList());
                                boolean flag2 = ChangedAccessorySlots.LEGS.get().canHoldItem(randomItemFromList, changedEntity);
                                if (flag2) {
                                    slots.setItem(ChangedAccessorySlots.LEGS.get(), randomItemFromList);
                                }
                            } // To Stop the Half Naked Entities To Spawn... if it spawns with only a shorts is less odd than only with a bra...
                        }
                    }
                }
            }

        });
    }

    private @NotNull ItemStack getRandomItemFromList(ChangedEntity changedEntity, List<Item> itemList) {
        return new ItemStack(Util.getRandom(itemList, changedEntity.getRandom()));
    }

    default int getDripParticleMultiplier() {
        if (!(this instanceof ChangedEntity changedEntity)) return 0;
        if (!IAlphaAbleEntity.isEntityAlpha(changedEntity)) return 0;

        Entity underlying = TransfurEvents.resolveChangedEntity(changedEntity.maybeGetUnderlying());
        if (!(underlying instanceof LivingEntity living)) return 0;

        double reach = living instanceof Player player
                ? player.getEntityReach()
                : 4.0D;

        EntityHitResult hit = PlayerUtil.getEntityHitLookingAt(
                living,
                (float) reach,
                IGNORE_TRANSLUCENT
        );

        if (hit == null || hit.getType() != HitResult.Type.ENTITY) return 0;

        Entity target = hit.getEntity();
        if (target.getType().is(ChangedTags.EntityTypes.HUMANOIDS)) {
            return 4;
        }

        return 0;
    }
}
