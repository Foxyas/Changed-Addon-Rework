package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WitherWaveAbility  extends SimpleAbility {

    @Override
    public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.wither_wave");
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/wither_wave.png"); //Place holder
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        Player player = (Player) entity.getEntity();
        TransfurVariantInstance<?> LatexInstace = ProcessTransfur.getPlayerTransfurVariant(player);
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        return player.getFoodData().getFoodLevel() >= 10 && Variant == AddonLatexVariant.EXPERIMENT_10.get() && !Spectator(entity.getEntity());
    }

    public static boolean Spectator(Entity entity){
        if (entity instanceof Player player1){
            return player1.isSpectator();
        }
        return true;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        return 100;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        return 30;
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }


    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        execute(entity.getLevel(),entity.getEntity());
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
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
                    MobEffectInstance Effect = new MobEffectInstance(MobEffects.WITHER, 60, 1, false, false,true);
                    Effect.setCurativeItems(List.of(new ItemStack(Items.MILK_BUCKET,1)));
                    _entity.addEffect(Effect);
                    ChangedSounds.broadcastSound(_entity, SoundEvents.COMPOSTER_READY,5,0);
                    ChangedSounds.broadcastSound(player, SoundEvents.WITHER_SHOOT,5,1);
                    player.causeFoodExhaustion(4f);
                }
            }
        }
    }
}
