package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class LeapProcedure {

    public static void execute(Level level, double x, double y, double z, Player player) {
        double deltaX, deltaY, deltaZ;
        double motionX, motionY, motionZ;
        double speed, ySpeed;

        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
        if(tf == null) return;

        if (canLeap(tf)) {
            if (!player.hasEffect(ChangedAddonMobEffects.FADIGE.get()) && player.getFoodData().getFoodLevel() > 6
                    && player.isOnGround() && !player.isInWater()) {
                if (!player.isShiftKeyDown()) {
                    deltaX = -Math.sin((player.getYRot() / 180) * (float) Math.PI);
                    deltaY = -Math.sin((player.getXRot() / 180) * (float) Math.PI);
                    deltaZ = Math.cos((player.getYRot() / 180) * (float) Math.PI);
                    speed = 0.4;
                    motionX = deltaX * speed;
                    motionY = deltaY * speed;
                    motionZ = deltaZ * speed;
                    player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY, motionZ));

                    if (!player.isCreative()) {
                        player.causeFoodExhaustion((float) 0.3);
                        if (!player.level.isClientSide())
                            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 40, 0, false, false));
                    }

                    if (!player.level.isClientSide() && player.getServer() != null)
                        player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                } else {
                    deltaX = -Math.sin((player.getYRot() / 180) * (float) Math.PI);
                    deltaY = player.level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos().getY()
                            - player.getY();
                    deltaZ = Math.cos((player.getYRot() / 180) * (float) Math.PI);
                    speed = 0.15;
                    ySpeed = 0.5;
                    motionX = deltaX * speed;
                    motionY = deltaY * ySpeed;
                    motionZ = deltaZ * speed;
                    player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY, motionZ));

                    if (!player.isCreative()) {
                        player.causeFoodExhaustion((float) (motionY * 1.25));
                        if (!player.level.isClientSide())
                            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 40, 0, false, false));
                    }

                    if (!(player instanceof ServerPlayer sPlayer && sPlayer.level instanceof ServerLevel
                            && sPlayer.getAdvancements().getOrStartProgress(sPlayer.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper"))).isDone())) {
                        if (motionY >= 0.75) {
                            if (player instanceof ServerPlayer _player) {
                                Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper"));
                                AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                                if (!_ap.isDone()) {
                                    for (String s : _ap.getRemainingCriteria())
                                        _player.getAdvancements().award(_adv, s);
                                }
                            }
                        }
                    }

                    if (!player.level.isClientSide() && player.getServer() != null)
                        player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                }
            }
        } else if (tf.getFormId().toString().equals("changed_addon:form_ket_experiment009_boss")) {
            if (player.hasEffect(ChangedAddonMobEffects.FADIGE.get()) || player.isSpectator()) return;

            deltaX = -Math.sin((player.getYRot() / 180) * (float) Math.PI);
            deltaY = -Math.sin((player.getXRot() / 180) * (float) Math.PI);
            deltaZ = Math.cos((player.getYRot() / 180) * (float) Math.PI);
            speed = 2;
            motionX = deltaX * speed;
            motionY = deltaY * 1;
            motionZ = deltaZ * speed;

            if (player.isOnGround() && !player.isInWater() && !player.isShiftKeyDown()) {
                level.levelEvent(2001, new BlockPos(player.getX(), player.getY() - 1, player.getZ()), Block.getId((level.getBlockState(new BlockPos(player.getX(), player.getY() - 1, player.getZ())))));
                player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY, motionZ));

                if (!player.level.isClientSide() && player.getServer() != null)
                    player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");

                if (!player.isCreative()) {
                    player.causeFoodExhaustion((float) 0.1);
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 60, 0));
                }
            }

            int horizontalRadiusHemiTop = 4 - 1;
            int verticalRadiusHemiTop = 1;
            for (int yi = 0; yi < verticalRadiusHemiTop; yi++) {
                for (int xi = -horizontalRadiusHemiTop; xi <= horizontalRadiusHemiTop; xi++) {
                    for (int zi = -horizontalRadiusHemiTop; zi <= horizontalRadiusHemiTop; zi++) {
                        double distanceSq = (xi * xi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop) + (yi * yi) / (double) (verticalRadiusHemiTop * verticalRadiusHemiTop)
                                + (zi * zi) / (double) (horizontalRadiusHemiTop * horizontalRadiusHemiTop);
                        if (distanceSq > 1.0) continue;

                        final Vec3 center = new Vec3(x + xi, y + yi, z + zi);
                        List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(1, 2, 1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
                        for (Entity entity : _entfound) {
                            if (entity == player) continue;

                            if (!player.isCreative()) {
                                player.causeFoodExhaustion((float) 0.1);
                                if (!player.level.isClientSide())
                                    player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 100, 0));
                            }

                            if(!(entity instanceof Player pl) || pl.isCreative() || pl.isSpectator()) continue;

                            entity.setDeltaMovement(new Vec3(0, 0.7, 0));
                            level.addParticle(ParticleTypes.FLASH, x + xi, y + yi, z + zi, 0, 0.7, 0);
                            player.swing(InteractionHand.MAIN_HAND, true);
                            entity.hurt(((new EntityDamageSource("lightningBolt", player) {
                                @Override
                                public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity _livingEntity) {
                                    Component entityName = _livingEntity.getDisplayName();
                                    Component itemName = null;
                                    Entity attacker = this.getEntity();
                                    ItemStack stack = ItemStack.EMPTY;
                                    Component attackerName = attacker.getDisplayName();
                                    if (attacker instanceof LivingEntity livingAttacker) {
                                        stack = livingAttacker.getMainHandItem();
                                    }
                                    if (!stack.isEmpty() && stack.hasCustomHoverName()) {
                                        itemName = stack.getDisplayName();
                                    }

                                    return itemName != null
                                            ? new TranslatableComponent("death.attack." + "lightningBolt.player", entityName, attackerName, itemName)
                                            : new TranslatableComponent("death.attack." + "lightningBolt.player", entityName, attackerName);
                                }
                            })), 3);

                            if (!level.isClientSide()) {
                                level.playSound(null, new BlockPos(entity.getX(), entity.getY(), entity.getZ()),
                                        ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.lightning_bolt.impact")), SoundSource.NEUTRAL, 1, 0);
                            } else {
                                level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()),
                                        ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.lightning_bolt.impact")), SoundSource.NEUTRAL, 1, 0, false);
                            }
                        }
                    }
                }
            }
        } else if (canFly(tf)) {
            if (player.hasEffect(ChangedAddonMobEffects.FADIGE.get()) || player.getFoodData().getFoodLevel() < 8) return;

            if (player.getAbilities().flying && !player.isFallFlying()) {
                deltaX = -Math.sin((player.getYRot() / 180) * (float) Math.PI);
                deltaY = -Math.sin((player.getXRot() / 180) * (float) Math.PI);
                deltaZ = Math.cos((player.getYRot() / 180) * (float) Math.PI);
                speed = 2;
                motionX = deltaX * speed;
                motionY = deltaY * speed;
                motionZ = deltaZ * speed;
                player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY, motionZ));

                if (!player.isCreative()) {
                    player.causeFoodExhaustion((float) 0.8);
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 30, 0, false, false));
                }

            } else {
                if (!player.isFallFlying()) return;

                deltaX = -Math.sin((player.getYRot() / 180) * (float) Math.PI);
                deltaY = -Math.sin((player.getXRot() / 180) * (float) Math.PI);
                deltaZ = Math.cos((player.getYRot() / 180) * (float) Math.PI);
                speed = 2;
                motionX = deltaX * speed;
                motionY = deltaY * speed;
                motionZ = deltaZ * speed;
                player.setDeltaMovement(player.getDeltaMovement().add(motionX, motionY, motionZ));

                if (!player.isCreative()) {
                    player.causeFoodExhaustion(4);
                    if (!player.level.isClientSide())
                        player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.FADIGE.get(), 60, 0, false, false));
                }
            }

            if (!player.level.isClientSide() && player.getServer() != null)
                player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
        }
    }

    private static boolean canLeap(TransfurVariantInstance<?> tf){
        TransfurVariant<?> Variant = TransfurVariant.getEntityVariant(tf.getChangedEntity());
        if (Variant.is(ChangedAddonTags.TransfurTypes.CAT_LIKE) || Variant.is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE)) {
            return !Variant.is(ChangedAddonTransfurVariants.LATEX_SNEP.get())
                    && !Variant.is(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD.get())
                    && !Variant.is(ChangedAddonTransfurVariants.EXPERIMENT_10.get())
                    && !Variant.is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get())
                    && !Variant.is(ChangedAddonTransfurVariants.LYNX.get());
        }
        return false;
    }

    private static boolean canFly(TransfurVariantInstance<?> tf){
        /*TransfurVariant Variant = TransfurVariant.getEntityVariant(tf.getChangedEntity());
		if (Variant.canGlide){
			return true;
		}*/
        return false;
    }
}
