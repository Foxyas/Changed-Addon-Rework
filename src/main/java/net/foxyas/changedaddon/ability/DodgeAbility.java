package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class DodgeAbility extends SimpleAbility {

    private static int DodgeAmount = 5;
    private static int MaxDodgeAmount = 5;
    private static boolean DodgeActivate = false;
    public static int DodgeRegenCooldown = 5;

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
        if(tag.contains("DodgeRegenCooldown")){
            DodgeRegenCooldown = tag.getInt("DodgeRegenCooldown");
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
        tag.putInt("DodgeRegenCooldown",DodgeRegenCooldown);
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
        return DodgeAmount > 0;
    }

    @Override
    public boolean canKeepUsing(IAbstractLatex entity) {
        return DodgeAmount > 0;
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        SetDodgeActivate(canUse(entity));
        if(entity.getEntity() instanceof Player player){
            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", + DodgeAmount),true);
        }
    }

    @Override
    public void tick(IAbstractLatex entity) {
        super.tick(entity);
        SetDodgeActivate(canUse(entity));
        if(entity.getEntity() instanceof Player player){
            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", + DodgeAmount),true);
        }
    }

    @Override
    public void stopUsing(IAbstractLatex entity) {
        super.stopUsing(entity);
        if(DodgeAmount < MaxDodgeAmount){
            if(DodgeRegenCooldown < 0) {
                DodgeAmount++;
                DodgeRegenCooldown = 5;
                if(entity.getEntity() instanceof Player player){
                    player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", + DodgeAmount),true);
                }
            } else {
                DodgeRegenCooldown--;
            }
        }
        if(isDodgeActivate()){
            SetDodgeActivate(false);
        }
    }
}
