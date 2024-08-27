package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;

import java.util.Iterator;
import java.util.Objects;

public class LeapAbility extends SimpleAbility {
    public LeapAbility() {
        super();
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.leap");
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/leap_ability.png");
    }

    @Override
    public boolean canUse(IAbstractLatex entity) {
        if (entity instanceof Player player) {
            LatexVariantInstance<?> LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
        }

        LatexVariant<?> Variant = entity.getLatexEntity().getSelfVariant();
        return Variant.is(ChangedTags.LatexVariants.CAT_LIKE) || Variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE);
    }

    public UseType getUseType(IAbstractLatex entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractLatex entity) {
        return 10;
    }

    @Override
    public int getCoolDown(IAbstractLatex entity) {
        return 15;
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        leapAbility(entity.getEntity());

    }

    public static void leapAbility(Entity entity) {
        if (entity == null)
            return;

        double motionZ;
        double deltaZ;
        double deltaX;
        double motionY;
        double deltaY;
        double motionX;
        double maxSpeed = 0;
        double speed;
        double Yspeed;
        if ((entity instanceof Player _plr ? _plr.getFoodData().getFoodLevel() : 0) > 6) {
            if (entity.isOnGround() && !entity.isInWater()) {
                Player Staticplayer = (Player) entity;
                if (!Staticplayer.isSpectator()) {
                    if (!entity.isShiftKeyDown()) {
                        deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                        deltaY = -Math.sin((entity.getXRot() / 180) * (float) Math.PI);
                        deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                        speed = 0.8;
                        motionX = deltaX * speed;
                        motionY = deltaY * speed;
                        motionZ = deltaZ * speed;
                        entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
                        Player player = (Player) entity;
                        if (!player.isCreative()) {
                            player.causeFoodExhaustion((float) 0.25);
                        }
                        {
                            if (!entity.level.isClientSide() && entity.getServer() != null)
                                entity.getServer().getCommands().performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                        }
                    }
                    if (entity.isShiftKeyDown()) {
                        deltaX = -Math.sin((entity.getYRot() / 180) * (float) Math.PI);
                        deltaY = entity.level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(1)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()
                                - entity.getY();
                        deltaZ = Math.cos((entity.getYRot() / 180) * (float) Math.PI);
                        speed = 0.15;
                        Yspeed = 0.5;
                        motionX = deltaX * speed;
                        motionY = deltaY * Yspeed;
                        motionZ = deltaZ * speed;
                        entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
                        Player _player = (Player) entity;
                        if (!_player.isCreative()) {
                            _player.causeFoodExhaustion((float) (motionY * 0.25));
                            LivingEntity _entity = (LivingEntity) entity;
                            if (!_entity.level.isClientSide())
                                _entity.addEffect(new MobEffectInstance(ChangedAddonModMobEffects.FADIGE.get(), 40, 0, false, false));
                        }
                        if (!(entity instanceof ServerPlayer _plr27 && _plr27.level instanceof ServerLevel
                                && _plr27.getAdvancements().getOrStartProgress(Objects.requireNonNull(_plr27.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper")))).isDone())) {
                            if (motionY >= 0.75) {
                                if (entity instanceof ServerPlayer player_) {
                                    Advancement _adv = player_.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:leaper"));
                                    assert _adv != null;
                                    AdvancementProgress _ap = player_.getAdvancements().getOrStartProgress(_adv);
                                    if (!_ap.isDone()) {
                                        for (String string : _ap.getRemainingCriteria())
                                            player_.getAdvancements().award(_adv, string);
                                    }
                                }
                            }
                        }
                        {
                            if (!entity.level.isClientSide() && entity.getServer() != null)
                                entity.getServer().getCommands().performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:bow2 ambient @a ~ ~ ~ 2.5 1 0");
                        }
                    }
                }

            }
        }
    }
}
