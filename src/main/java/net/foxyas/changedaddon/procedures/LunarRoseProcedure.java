package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LunarRoseProcedure {
    public static void execute(LevelAccessor world, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = getPlayerTransfurVariant(player);

            if (instance == null) {
                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, false, false));
                }
                {
                    final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
                    List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                            .toList();
                    for (Entity entityiterator : _entfound) {
                        if (!(entityiterator == entity)) {
                            if (!(entityiterator instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.REGENERATION))) {
                                if ((player.hasEffect(MobEffects.REGENERATION) ? player.getEffect(MobEffects.REGENERATION).getAmplifier() : 0) < 3) {
                                    if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 3, false, false));
                                    if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 270, 1, false, false));
                                }
                            }
                        }
                    }
                }
                itemstack.getOrCreateTag().putBoolean("Unbreakable", true);
            } else if (instance.getFormId().toString().equals("changed_addon:form_puro_kind") || instance.getFormId().toString().equals("changed_addon:form_light_latex_wolf")) {
                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, false, false));
                }
                {
                    final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
                    List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                            .toList();
                    for (Entity entityiterator : _entfound) {
                        if (!(entityiterator == entity)) {
                            if (!(entityiterator instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.REGENERATION))) {
                                if ((player.hasEffect(MobEffects.REGENERATION) ? player.getEffect(MobEffects.REGENERATION).getAmplifier() : 0) < 3) {
                                    if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 3, false, false));
                                    if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
                                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 270, 1, false, false));
                                }
                            }
                        }
                    }
                }
                itemstack.getOrCreateTag().putBoolean("Unbreakable", true);
            }
        }
    }

    private static @Nullable TransfurVariantInstance<?> getPlayerTransfurVariant(Player player) {
        return ProcessTransfur.getPlayerTransfurVariant(player);
    }
}
