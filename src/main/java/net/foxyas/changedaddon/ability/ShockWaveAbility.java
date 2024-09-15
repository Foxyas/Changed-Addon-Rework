package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShockWaveAbility extends SimpleAbility {

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.shock_wave");
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/thunder_wave.png"); //Place holder
    }

    @Override
    public boolean canUse(IAbstractLatex entity) {
        Player player = (Player) entity.getEntity();
        LatexVariantInstance<?> LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
        LatexVariant<?> Variant = entity.getLatexEntity().getSelfVariant();
        return player.getFoodData().getFoodLevel() >= 10 && Variant == AddonLatexVariant.KET_EXPERIMENT_009 || Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT
                && !Spectator(entity.getEntity());
    }

    public static boolean Spectator(Entity entity){
        if (entity instanceof Player player1){
            return player1.isSpectator();
        }
        return true;
    }

    @Override
    public int getCoolDown(IAbstractLatex entity) {
        LatexVariant<?> Variant = entity.getLatexEntity().getSelfVariant();
        if (Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT) {
            return 60;
        }
        return 100;
    }

    @Override
    public int getChargeTime(IAbstractLatex entity) {
        LatexVariant<?> Variant = entity.getLatexEntity().getSelfVariant();
        if (Variant == AddonLatexVariant.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT) {
            return 16;
        }
        return 30;
    }

    public UseType getUseType(IAbstractLatex entity) {
        return UseType.CHARGE_TIME;
    }


    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        execute(entity.getLevel(),entity.getEntity());
    }

    @Override
    public void tick(IAbstractLatex entity) {
        super.tick(entity);
        //execute(entity.getLevel(),entity.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        Player player = (Player) entity;
        final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
        float Zone = 16;
        if (player.isInWaterOrRain()){
            Zone = 24;
        }
        List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(Zone, Zone, Zone), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                .collect(Collectors.toList());
        for (Entity entityiterator : _entfound) {
            if (player != entityiterator && entityiterator instanceof LivingEntity _entity){
                if (_entity instanceof Player player1){
                    if (player1.isSpectator() || player1.isCreative()){
                        return;
                    }
                }
                if (!_entity.level.isClientSide()){
                    MobEffectInstance Effect = new MobEffectInstance(ChangedEffects.SHOCK, 40, 0, false, false,true);
                    Effect.setCurativeItems(List.of());
                    _entity.addEffect(Effect);
                    ChangedSounds.broadcastSound(_entity,ChangedSounds.PARALYZE1,5,1);
                    player.causeFoodExhaustion(4f);
                }
            }
        }
    }
}
