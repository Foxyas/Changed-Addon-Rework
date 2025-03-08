package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.util.ChangedAddonSounds;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class ClawsAbility extends SimpleAbility {
    public boolean isActive = false;

    public ClawsAbility() {
        super();
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.saveData(tag, entity);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
        super.readData(tag, entity);
        if (tag.contains("isActive")) {
            this.isActive = tag.getBoolean("isActive");
        }
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return super.getTexture(entity);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.claws");
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> list = new ArrayList<>();
        list.add(new TranslatableComponent("changed_addon.ability.claws.desc"));
        return list;
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        if (entity.getTransfurVariantInstance() == null) {
            return false;
        }

        return entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAT_LIKE)
                || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.LEOPARD_LIKE);
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        UseClaws(entity);
    }

    private void UseClaws(IAbstractChangedEntity entity) {
        this.isActive = !isActive;
    }

    @SubscribeEvent
    public void attack(LivingHurtEvent event) {
        Entity target = event.getEntity();
        if (event.getEntityLiving() instanceof Player player && player.getAttackStrengthScale(0.0f) >= 0.9 && player.isOnGround()) {
            if (this.isActive && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                // ⚔ Área de efeito: Raio de 1.5 blocos ao redor do alvo
                double radius = 1.5;
                AABB attackArea = target.getBoundingBox().inflate(radius, 0, radius);
                List<LivingEntity> nearbyEntities = player.level.getEntitiesOfClass(LivingEntity.class, attackArea);
                float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                float f3 = ((1.0F + EnchantmentHelper.getSweepingDamageRatio(player)) * f) / nearbyEntities.size();
                // 🔥 Knockback em todos os alvos próximos (exceto o atacante)
                for (LivingEntity entity : nearbyEntities) {
                    if (entity != target && entity != player
                            && (!(entity instanceof ArmorStand) || !((ArmorStand) entity).isMarker())
                            && player.canHit(entity, 0)) {
                        //Vec3 knockbackVec = target.position().subtract(entity.position()).normalize();
                        // 🏹 Knockback horizontal (respeita resistência)
                        //entity.knockback(0.8, knockbackVec.x, knockbackVec.z);
                        //entity.hurt(DamageSource.mobAttack(player), 7f / nearbyEntities.size());
                        entity.knockback(0.4, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                        entity.hurt(DamageSource.playerAttack(player), f3);

                    }
                }
                double d0 = (double) (-Mth.sin(player.getYRot() * 0.017453292F)) * 1;
                double d1 = (double) Mth.cos(player.getYRot() * 0.017453292F) * 1;
                Level var7 = player.level;
                if (var7 instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0, player.getY(0.5), player.getZ() + d1, 0, d0, 0.0, d1, 0.0);
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
                }
            }
        }
    }
}
