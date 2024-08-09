package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DodgeAbilityInstance extends AbstractAbilityInstance {

    public DodgeAbilityInstance(AbstractAbility<?> ability, IAbstractLatex entity){
        super(ability,entity);
    }
    private static int DodgeAmount = 4;
    private static int MaxDodgeAmount = 4;
    private static boolean DodgeActivate = false;
    public static int DodgeRegenCooldown = 5;

    public boolean isDodgeActivate(){
        return DodgeActivate;
    }

    public void SetDodgeActivate(boolean set){
        DodgeActivate = set;
    }
    public int getDodgeAmount(){
        return DodgeAmount;
    }
    public int getMaxDodgeAmount(){
        return DodgeAmount;
    }

    public void setDodgeAmount(int set){
        DodgeAmount = set;
    }

    public void addDodgeAmount(){
        DodgeAmount++;
    }

    public void subDodgeAmount(){
        DodgeAmount--;
    }

    public void setMaxDodgeAmount(int set){
        MaxDodgeAmount = set;
    }

    public void addMaxDodgeAmount(){
        MaxDodgeAmount++;
    }

    public void subMaxDodgeAmount(){
        MaxDodgeAmount--;
    }


    @Override
    public boolean canUse() {
        return DodgeAmount > 0 && !Spectator(entity.getEntity());
    }

    public static boolean Spectator(Entity entity){
        if (entity instanceof Player player1){
            return player1.isSpectator();
        }
        return true;
    }

    @Override
    public boolean canKeepUsing() {
        return DodgeAmount > 0;
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
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
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        tag.putInt("DodgeAmount",DodgeAmount);
        tag.putInt("MaxDodgeAmount",MaxDodgeAmount);
        tag.putInt("DodgeRegenCooldown",DodgeRegenCooldown);
        tag.putBoolean("DodgeActivate",DodgeActivate);
    }


    @Override
    public void startUsing() {
        if(entity.getEntity() instanceof Player player){
            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", + DodgeAmount),true);
        }
    }

    @Override
    public void tick() {
        SetDodgeActivate(canUse());
        if(entity.getEntity() instanceof Player player){
            player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", + DodgeAmount),true);
        }
    }

    @Override
    public void stopUsing() {
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
