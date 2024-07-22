package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.beast.LightLatexCentaur;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class CarryAbility extends SimpleAbility {
    public CarryAbility() {
        super();
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractLatex entity) {
        return new TranslatableComponent("changed_addon.ability.carry");
    }

    @Override
    public ResourceLocation getTexture(IAbstractLatex entity) {
        return new ResourceLocation("changed_addon:textures/screens/carry.png");
    }

    @Override
    public UseType getUseType(IAbstractLatex entity) {
        return UseType.INSTANT;
    }

    @Override
    public boolean canUse(IAbstractLatex entity) {
        return Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.EXP2.female()) ||
                Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.EXP2.male()) ||
                Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ORGANIC_SNOW_LEOPARD.female()) ||
                Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ORGANIC_SNOW_LEOPARD.male()) ||
                Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ADDON_PURO_KIND.female()) ||
                Objects.requireNonNull(entity.getLatexVariantInstance()).getParent().is(AddonLatexVariant.ADDON_PURO_KIND.male());
    }

    @Override
    public void startUsing(IAbstractLatex entity) {
        super.startUsing(entity);
        Run(entity.getEntity());
    }

    @Override
    public void onRemove(IAbstractLatex entity) {
        super.onRemove(entity);
        SafeRemove(entity.getEntity());
    }

    public static void Run(Entity mainEntity){
        if(mainEntity instanceof Player player){
            if (player.getFirstPassenger() != null){
                player.getFirstPassenger().stopRiding();
                return;
            }
            if(player.isSpectator()){
                return;
            }
            Entity carryTarget = PlayerUtilProcedure.getEntityPlayerLookingAt(player,3);
            if(carryTarget != null){
                if(carryTarget instanceof Player carryPlayer){
                    if(carryPlayer.isCreative()){
                        if(!player.isCreative()){
                            return;
                        }
                    }
                    if(ProcessTransfur.getPlayerLatexVariant(carryPlayer).is(LatexVariant.LIGHT_LATEX_CENTAUR)){
                        player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry",carryPlayer.getDisplayName()),true);
                        return;
                    }
                    if(!player.level.isClientSide){
                        carryPlayer.startRiding(player,true);
                    }
                    SoundPlay(player);
                } else {
                    if(carryTarget instanceof LightLatexCentaur){
                        player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry",carryTarget.getDisplayName()),true);
                        return;
                    }
                    if(carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS)){
                        if(!player.level.isClientSide){
                            carryTarget.startRiding(player,true);
                        }
                        SoundPlay(player);
                    }
                }
            }
        }
    }

    public static void SoundPlay(Player player){
        player.level.playSound(player,player, ChangedSounds.BOW2, SoundSource.PLAYERS,2.5f,1);
    }
    
    public static void SafeRemove(Entity MainEntity){
        if(MainEntity.getFirstPassenger() != null) {
            MainEntity.getFirstPassenger().stopRiding();
        }
    }
}
