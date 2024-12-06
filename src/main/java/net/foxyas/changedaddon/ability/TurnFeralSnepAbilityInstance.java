package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class TurnFeralSnepAbilityInstance extends AbstractAbilityInstance {

    public TransfurVariant<?> OldVariant = null;
    //public TransfurVariant<?> NewVariant = null;

    /*public static Map<TransfurVariant<?>, Function<TransfurVariant<?>, TransfurVariant<?>>> VARIANT_CONVERSION () {
        return new HashMap<TransfurVariant<?>, Function<TransfurVariant<?>, TransfurVariant<?>>>() {{
                // Mapeamento de variantes e suas conversões
                put(ChangedAddonTransfurVariants.EXP2_MALE.get(), input -> ChangedAddonTransfurVariants.LATEX_SNEP.get());
                put(ChangedAddonTransfurVariants.EXP2_FEMALE.get(), input -> ChangedAddonTransfurVariants.LATEX_SNEP.get());
            }

        };
    }*/
    private static final String OLD_TRANSFUR_VARIANT = "OldVariant";
    //private static final String NEW_TRANSFUR_VARIANT = "NewVariant";
    public TurnFeralSnepAbilityInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void onSelected() {
        super.onSelected();
        if (entity.getEntity() instanceof Player player){
            var Instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (this.OldVariant == null
                    && Instance != null){
                if (Instance.getParent() != ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get()
                        && Instance.getParent() != ChangedAddonTransfurVariants.LATEX_SNEP.get()){
                    setOldVariant(entity.getSelfVariant());
                }
            }
        }
    }

    @Override
    public void saveData(CompoundTag tag) {
        super.saveData(tag);
        if (this.OldVariant != null && OldVariant.getRegistryName() != null){
            tag.putString(OLD_TRANSFUR_VARIANT,OldVariant.getRegistryName().toString());
        }
        /*if (this.NewVariant != null && NewVariant.getRegistryName() != null){
            tag.putString(NEW_TRANSFUR_VARIANT,NewVariant.getRegistryName().toString());
        }*/
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);

        if (tag.contains(OLD_TRANSFUR_VARIANT)){
            ResourceLocation form;
            try {
                form = new ResourceLocation(tag.getString(OLD_TRANSFUR_VARIANT));
            } catch (Exception e) {
                form = new ResourceLocation("");
            }
            if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)){
                this.OldVariant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
            }
        }

        /*if (tag.contains(NEW_TRANSFUR_VARIANT)){
            ResourceLocation form;
            try {
                form = new ResourceLocation(tag.getString(NEW_TRANSFUR_VARIANT));
            } catch (Exception e) {
                form = new ResourceLocation("");
            }
            if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)){
                this.OldVariant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
            }

         }*/
    }

    public static String getOldTransfurVariant() {
        return OLD_TRANSFUR_VARIANT;
    }

    /*
    public static String getNewTransfurVariant() {
        return NEW_TRANSFUR_VARIANT;
    }
    public void setNewVariant(TransfurVariant<?> newVariant) {
        NewVariant = newVariant;
    }*/

    public void setOldVariant(TransfurVariant<?> oldVariant) {
        OldVariant = oldVariant;
    }

    @Override
    public void startUsing() {
        if (entity.getEntity() instanceof Player player) {
            TransfurVariant<?> targetVariant = determineNextVariant(entity.getTransfurVariant());
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.setPlayerTransfurVariant(
                    player,
                    targetVariant,
                    TransfurContext.hazard(TransfurCause.GRAB_REPLICATE),
                    1
            );
            if (variantInstance != null) {
                variantInstance.ifHasAbility(ChangedAddonAbilitys.TURN_FERAL_SNEP.get(), abilityInstance ->
                        abilityInstance.setOldVariant(entity.getTransfurVariant())
                );
                entity.getEntity().getLevel().playSound(null,entity.getEntity(), ChangedSounds.POISON, SoundSource.PLAYERS,1,1);
            }
        }
    }

    private TransfurVariant<?> determineNextVariant(TransfurVariant<?> currentVariant) {
        if (currentVariant == ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get() || currentVariant == ChangedAddonTransfurVariants.LATEX_SNEP.get()) {
            return this.OldVariant != null ? this.OldVariant : ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getRandomVariant(new Random());
        } else {
            return ChangedAddonTransfurVariants.LATEX_SNEP_FERAL_FORM.get();
        }
    }

    @Override
    public void tick() {}

    @Override
    public void stopUsing() {}
}
