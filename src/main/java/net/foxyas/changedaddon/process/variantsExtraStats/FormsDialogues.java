package net.foxyas.changedaddon.process.variantsExtraStats;

import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class FormsDialogues {

    private static final List<UUID> YTNames = List.of(
            UUID.fromString("e61e6f3e-4820-4bca-816f-ba5fd7fdf529"),
            UUID.fromString("c1136b82-915d-49b9-b468-e717d371dc1e"),
            UUID.fromString("520f7606-1276-46b1-be2d-b307cc6eddd7"),
            UUID.fromString("f0358d36-d4b5-4aa9-aac6-e9b62bf55a03"),
            UUID.fromString("145f75e9-2636-4c96-99cd-0dbd0973a1d0"));

    private static final List<UUID> FBNames = List.of(
            UUID.fromString("66220457-29ea-4093-9389-5c4b571d4bda"));


    @SubscribeEvent
    public static void SendDeathTexts(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);

            final List<TransfurVariant<?>> TRANSFUR_VARIANT_LIST = List.of(
                    ChangedAddonTransfurVariants.EXP1_FEMALE.get(),
                    ChangedAddonTransfurVariants.EXP1_MALE.get(),
                    ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get(),
                    ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get(),
                    ChangedAddonTransfurVariants.EXP2_MALE.get(),
                    ChangedAddonTransfurVariants.EXP2_FEMALE.get());

            if (PlayerVariant != null && TRANSFUR_VARIANT_LIST.contains(PlayerVariant.getParent())) {
                if (player.level().getLevelData().isHardcore()) {
                    hardcoreWorldHandle(player, PlayerVariant);
                } else {
                    normalWorldHandle(player, PlayerVariant);
                }
            }
        }
    }

    private static void normalWorldHandle(Player player, TransfurVariantInstance<?> PlayerVariant) {
        if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get())) {
            int randomSelector = player.level().getRandom().nextInt(100);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.death.text1"), false);
            } else {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.death.text2"), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get())) {
            int randomSelector = player.level().getRandom().nextInt(100);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.death.text1"), false);
            } else {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.death.text2"), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_MALE.get())
                || PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())) {
            int randomSelector = player.level().getRandom().nextInt(100);
            if (randomSelector <= 3) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text1"), false);
            } else if (randomSelector <= 6) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text2"), false);
            } else if (randomSelector <= 9) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text3"), false);
            } else if (randomSelector <= 12) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text4", player.getDisplayName().getString()), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_MALE.get())
                || PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_FEMALE.get())) {
            int randomSelector = player.level().getRandom().nextInt(100);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp2.death.text1"), false);
            } else if (randomSelector <= 7) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp2.death.text2"), false);
            }
        }
    }

    private static void hardcoreWorldHandle(Player player, TransfurVariantInstance<?> PlayerVariant) {
        if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get())) {
            int randomSelector = player.level().getRandom().nextInt(10);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.death.text1"), false);
            } else {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.death.text2"), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get())) {
            int randomSelector = player.level().getRandom().nextInt(10);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.death.text1"), false);
            } else {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.death.text2"), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_MALE.get())
                || PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())) {
            int randomSelector = player.level().getRandom().nextInt(15);
            if (randomSelector <= 3) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text1"), false);
            } else if (randomSelector <= 6) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text2"), false);
            } else if (randomSelector <= 9) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text3"), false);
            } else if (randomSelector <= 12) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.death.text4", player.getDisplayName().getString()), false);
            }
        } else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_MALE.get())
                || PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_FEMALE.get())) {
            int randomSelector = player.level().getRandom().nextInt(10);
            if (randomSelector <= 4) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp2.death.text1"), false);
            } else if (randomSelector >= 7) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp2.death.text2"), false);
            }
        }
    }

    @SubscribeEvent
    public static void SendTransfurTexts(ProcessTransfur.EntityVariantAssigned.ChangedVariant changedVariantEvent) {
        if (changedVariantEvent.newVariant == null) {
            return;
        }
        if (changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get())) {
            if (changedVariantEvent.livingEntity instanceof Player player) {
                if (YTNames.contains(player.getUUID())) {
                    if (player.level().isClientSide()) {
                        player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.transfur.text.secret"), false);
                    }
                }
            }
        }
        if (changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.EXP1_MALE.get()) || changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())) {
            if (changedVariantEvent.livingEntity instanceof Player player) {
                if (FBNames.contains(player.getUUID())) {
                    if (player.level().isClientSide()) {
                        player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp1.transfur.text.secret"), false);
                    }
                }
            }
        }
    }
}
