package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DodgeAbility extends SimpleAbility {

    private static int DodgeAmount = 3;
    private static int MaxDodgeAmount = 5;
    private static boolean DodgeActivate = false;

    public static boolean isDodgeActivate(){
        return DodgeActivate;
    }

    public static void SetDodgeActivate(boolean set){
        DodgeActivate = set;
    }

    public static int getDodgeAmount(){
     return DodgeAmount;
    }
    public static int getMaxDodgeAmount(){
        return DodgeAmount;
    }

    public static void setDodgeAmount(int set){
        DodgeAmount = set;
    }

    public static void addDodgeAmount(){
        DodgeAmount++;
    }

    public static void subDodgeAmount(){
        DodgeAmount--;
    }

    public static void setMaxDodgeAmount(int set){
        MaxDodgeAmount = set;
    }

    public static void addMaxDodgeAmount(){
        MaxDodgeAmount++;
    }

    public static void subMaxDodgeAmount(){
        MaxDodgeAmount--;
    }

    @Override
    public void readData(CompoundTag tag, IAbstractLatex entity) {
        super.readData(tag, entity);
        if(tag.contains("DodgeAmount")){
            DodgeAmount = tag.getInt("DodgeAmount");
        }
        if(tag.contains("MaxDodgeAmount")){
            MaxDodgeAmount = tag.getInt("MaxDodgeAmount");
        }
        if(tag.contains("DodgeActivate")){
            DodgeActivate = tag.getBoolean("DodgeActivate");
        }
    }

    @Override
    public void saveData(CompoundTag tag, IAbstractLatex entity) {
        super.saveData(tag, entity);
        tag.putInt("DodgeAmount",DodgeAmount);
        tag.putInt("MaxDodgeAmount",MaxDodgeAmount);
        tag.putBoolean("DodgeActivate",DodgeActivate);
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.dodge");
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/thunderbolt.png");
    }

    @Override
    public UseType getUseType(IAbstractLatex entity) {
        return UseType.HOLD;
    }


    @Override
    public boolean canUse(IAbstractLatex entity) {
        return this.DodgeAmount > 0;
    }

    @Override
    public boolean canKeepUsing(IAbstractLatex entity) {
        return this.DodgeAmount > 0;
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        SetDodgeActivate(true);
    }

    @Override
    public void tick(IAbstractLatex entity) {
        super.tick(entity);
        SetDodgeActivate(true);
    }

    @Override
    public void stopUsing(IAbstractLatex entity) {
        super.stopUsing(entity);
        if(DodgeAmount < MaxDodgeAmount){
            DodgeAmount++;
        }
        if(isDodgeActivate()){
            SetDodgeActivate(false);
        }
    }
}
