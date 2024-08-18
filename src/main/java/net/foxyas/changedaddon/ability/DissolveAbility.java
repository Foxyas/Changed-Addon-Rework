package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class DissolveAbility extends SimpleAbility {
    public DissolveAbility(){
        super();
    }

    public static double LocationX = 0;
    public static double LocationY = 0;
    public static double LocationZ = 0;
    public static boolean isSet = false;

    private static boolean isSet(){
        if (LocationX == 0 && LocationY == 0 && LocationZ == 0 && isSet){
            return true;
        }
        return !(LocationX == 0 && LocationY == 0 && LocationZ == 0) && isSet;
    }


    @Override
    public SimpleAbilityInstance makeInstance(IAbstractLatex entity) {
        return super.makeInstance(entity);
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.dissolve");
    }

    @Override
    public UseType getUseType(IAbstractLatex entity) {
        if (isSet()){
            return UseType.INSTANT;
        }
        return UseType.CHARGE_TIME;
    }

    @Override
    public int getChargeTime(IAbstractLatex entity) {
        return isSet() ? 0 : 20;
    }

    @Override
    public int getCoolDown(IAbstractLatex entity) {
        return isSet() ? 600 : 20;
    }

    @Override
    public boolean canUse(IAbstractLatex entity) {
        return true;
    }

    @Override
    public boolean canKeepUsing(IAbstractLatex entity) {
        return true;
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        if (entity.getEntity() instanceof Player player) {
            Tp(player);
        }
    }

    @Override
    public void tick(IAbstractLatex entity) {
        super.tick(entity);
    }

    @Override
    public void stopUsing(IAbstractLatex entity) {
        super.stopUsing(entity);
    }

    @Override
    public void tickCharge(IAbstractLatex entity, float ticks) {
        super.tickCharge(entity, ticks);
    }

    @Override
    public void onRemove(IAbstractLatex entity) {
        super.onRemove(entity);
        UnSet();
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractLatex entity) {
        super.saveData(tag, entity);
        tag.putDouble("LocationX",LocationX);
        tag.putDouble("LocationY",LocationY);
        tag.putDouble("LocationZ",LocationZ);
        tag.putBoolean("isSet",isSet);
    }

    @Override
    public void readData(CompoundTag tag, IAbstractLatex entity) {
        super.readData(tag, entity);
        if(tag.contains("LocationX")){
            LocationX = tag.getInt("LocationX");
        }
        if(tag.contains("LocationY")){
            LocationY = tag.getInt("LocationY");
        }
        if(tag.contains("LocationZ")){
            LocationZ = tag.getInt("LocationZ");
        }
        if(tag.contains("isSet")){
            isSet = tag.getBoolean("isSet");
        }
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/dodge_ability.png");
    }

    private static void UnSet(){
        LocationX = 0;
        LocationY = 0;
        LocationZ = 0;
        isSet = false;
    }
    private static void SetTp(Player entity) {
            LocationX = entity.getX();
            LocationY = entity.getY();
            LocationZ = entity.getZ();
            isSet = true;
    }

    private static void Tp(Player player){
        LatexVariantInstance<?> Instance = ProcessTransfur.getPlayerLatexVariant(player);
        if (Instance == null){
            return;
        }
        if (!isSet() && !player.isShiftKeyDown()) {
            SetTp(player);
        }
        if (isSet() && player.isShiftKeyDown()){
            UnSet();
        }
        if (isSet() && !player.isShiftKeyDown() && Distance(player.position(),new Vec3(LocationX,LocationY,LocationZ)) <= 1000) {
            player.teleportTo(LocationX,LocationY,LocationZ);
            player.hurt(DamageSource.MAGIC.bypassInvul(),4f);
            ChangedSounds.broadcastSound(player, ChangedSounds.POISON,2.5f,1);
            if (player.getLevel() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getFirst()), player.getX(), player.getY() + 1, player.getZ(), 5, 0.2, 0.3, 0.2, 0);
                serverLevel.sendParticles(ChangedParticles.drippingLatex(Instance.getParent().getColors().getSecond()), player.getX(), player.getY() + 1, player.getZ(), 5, 0.2, 0.3, 0.2, 0);
            }
        } else if (Distance(player.position(),new Vec3(LocationX,LocationY,LocationZ)) > 1000) {
            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dissolve.warn.too_far"),true);
        }
    }

    public static double Distance(Vec3 pos1,Vec3 pos2){
        return pos1.distanceTo(pos2);
    }
}
