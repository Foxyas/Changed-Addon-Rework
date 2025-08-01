package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.mobs.FoxyasEntity;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class SmallTickUpdateProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player.level, event.player);
        }
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;

        final Vec3 _center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2), e -> true)
                .stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(_center))).collect(Collectors.toList());
        for (Entity entityiterator : _entfound) {
            if (entityiterator != entity && entityiterator instanceof FoxyasEntity) {
                if (entity instanceof ServerPlayer _player) {
                    Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:gooey_friend"));
                    AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(Objects.requireNonNull(_adv));
                    if (!_ap.isDone()) {
                        for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
                    }
                }
            }
        }

        if (entity instanceof Player _playerHasItem && (
                _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get())) ||
                        _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.POTWITHCAMONIA.get())) ||
                        _playerHasItem.getInventory().contains(new ItemStack(ChangedAddonItems.DIFFUSION_SYRINGE.get())))) {
            if (entity instanceof ServerPlayer _player) {
                Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:untransfuritemadvancement"));
                AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(Objects.requireNonNull(_adv));
                if (!_ap.isDone()) {
                    for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
                }
            }
        }

        if (entity instanceof Player player) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (variant != null) {
                player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.aredarklatex = variant.getFormId().toString().contains("dark_latex") || variant.getFormId().toString().contains("puro_kind");
                    capability.syncPlayerVariables(entity);
                });
            }
        }

        AttributeModifier Exp009Buff_attack = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba751"), "transfur_stats", 4, AttributeModifier.Operation.ADDITION);
        AttributeModifier Exp009Buff_defense = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba752"), "transfur_stats", 8, AttributeModifier.Operation.ADDITION);
        AttributeModifier Exp009Buff_armor = new AttributeModifier(UUID.fromString("17c5b5cf-bdae-4191-84d1-433db7cba753"), "transfur_stats", 6, AttributeModifier.Operation.ADDITION);

        if (entity instanceof Player player) {
            TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
            String formId = variant != null ? variant.getFormId().toString() : "";

            boolean isTransfurred = variant != null;
            boolean isForm009Boss = formId.equals("changed_addon:form_ket_experiment009_boss");
            boolean isForm010Boss = formId.equals("changed_addon:form_experiment_10_boss");

            if (world.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
                if (isTransfurred && isForm009Boss && !getPlayerVars(entity).Exp009TransfurAllowed) {
                    Exp009TransfurProcedure.execute(entity);
                }
                if (isTransfurred && isForm010Boss && !getPlayerVars(entity).Exp10TransfurAllowed) {
                    Exp009TransfurProcedure.exp10(entity);
                }
            }

            if (isTransfurred && getPlayerVars(entity).Exp009Buff) {
                addModifier((LivingEntity) entity, Attributes.ATTACK_DAMAGE, Exp009Buff_attack);
                addModifier((LivingEntity) entity, Attributes.ARMOR, Exp009Buff_defense);
                addModifier((LivingEntity) entity, Attributes.ARMOR_TOUGHNESS, Exp009Buff_armor);
            } else {
                removeModifier((LivingEntity) entity, Attributes.ATTACK_DAMAGE, Exp009Buff_attack);
                removeModifier((LivingEntity) entity, Attributes.ARMOR, Exp009Buff_defense);
                removeModifier((LivingEntity) entity, Attributes.ARMOR_TOUGHNESS, Exp009Buff_armor);
            }

            boolean holdingTotem = ((LivingEntity) entity).getMainHandItem().getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get()
                    || ((LivingEntity) entity).getOffhandItem().getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get();

            if (holdingTotem && isTransfurred && !getPlayerVars(entity).Exp009Buff) {
                setExp009Buff(entity, true);
            } else if (!holdingTotem && getPlayerVars(entity).Exp009Buff) {
                setExp009Buff(entity, false);
            } else if (!isTransfurred && getPlayerVars(entity).Exp009Buff) {
                setExp009Buff(entity, false);
            }
        }
    }

    private static ChangedAddonModVariables.PlayerVariables getPlayerVars(Entity entity) {
        return entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables());
    }

    private static void setExp009Buff(Entity entity, boolean value) {
        entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.Exp009Buff = value;
            capability.syncPlayerVariables(entity);
        });
    }

    private static void addModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        if (!Objects.requireNonNull(entity.getAttribute(attribute)).hasModifier(modifier)) {
            Objects.requireNonNull(entity.getAttribute(attribute)).addTransientModifier(modifier);
        }
    }

    private static void removeModifier(LivingEntity entity, Attribute attribute, AttributeModifier modifier) {
        Objects.requireNonNull(entity.getAttribute(attribute)).removeModifier(modifier);
    }
}
