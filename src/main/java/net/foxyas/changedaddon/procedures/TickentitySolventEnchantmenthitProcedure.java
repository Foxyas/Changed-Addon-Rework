package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TickentitySolventEnchantmenthitProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity target = event.getEntity();
        Entity sourceEntity = event.getSource().getEntity();
        Entity directEntity = event.getSource().getDirectEntity();

        if (target == null || directEntity == null) {
            return;
        }

        // Verifica o nível do encantamento "Solvent"
        int enchantLevel = getSolventEnchantmentLevel(directEntity);
        if (enchantLevel == 0) {
            return;
        }

        // Verifica se a entidade deve ser afetada
        if (shouldAffectEntity(target) && isValidWeapon(directEntity)) {
            applyEffects(event, target, enchantLevel);
        }
    }

    private static int getSolventEnchantmentLevel(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack mainHandItem = livingEntity.getMainHandItem();
            return EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), mainHandItem);
        } else if (entity instanceof ThrownTrident trident) {
            CompoundTag tag = new CompoundTag();
            trident.save(tag);
            ItemStack tridentItem = tag.contains("Trident") ? ItemStack.of(tag.getCompound("Trident")) : new ItemStack(Items.TRIDENT);
            return EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), tridentItem);
        }
        return 0;
    }

    private static boolean shouldAffectEntity(Entity entity) {
        return entity instanceof Player player && ProcessTransfur.isPlayerLatex(player)
                || (entity.getType().is(ChangedTags.EntityTypes.LATEX)
                && entity instanceof ChangedEntity);
    }

    private static boolean isValidWeapon(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack mainHandItem = livingEntity.getMainHandItem();
            return !(mainHandItem.getItem() instanceof BowItem || mainHandItem.getItem() instanceof CrossbowItem);
        }
        return entity instanceof ThrownTrident || !(entity instanceof Projectile);
    }

    private static void applyEffects(LivingHurtEvent event, Entity target, int enchantLevel) {
        // Multiplica o dano baseado no nível do encantamento
        double multiplier = event.getAmount() * calculateDamageMultiplier(enchantLevel);
        event.setAmount((float) (multiplier));

        // Aplica som e partículas
        playSoundAndParticles(target);
    }

    private static double calculateDamageMultiplier(float enchantLevel) {
        return 1.0 + (enchantLevel) * 0.20;

    }

	/*
	private static double calculateDamageMultiplier(float enchantLevel) {
			return switch ((int) enchantLevel) {
				case 1 -> 1.25f;
				case 2 -> 1.45f;
				case 3 -> 1.6f;
				case 4 -> 1.7f;
				case 5 -> 1.8f;
				default -> 1.0 + (enchantLevel - 1) * 0.25;
			};
		}

	*/

    private static void playSoundAndParticles(Entity entity) {
        Level level = entity.level;

        // Toca som de extinção de fogo
        if (entity instanceof Player player) {
            playSound(level, player.getX(), player.getY(), player.getZ());
        } else {
            entity.playSound(SoundEvents.FIRE_EXTINGUISH, 2.5f, 0);
        }

        // Emite partículas
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    (SimpleParticleType) ChangedAddonModParticleTypes.SOLVENT_PARTICLE.get(),
                    entity.getX(),
                    entity.getY() + 1,
                    entity.getZ(),
                    10,
                    0.2, 0.3, 0.2, 0.1
            );
        }
    }

    private static void playSound(Level level, double x, double y, double z) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER, 2.5f, 0);
        } else {
            level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.MASTER, 2.5f, 0, true);
        }
    }
}
