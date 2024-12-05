package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class CustomInteraction extends SimpleAbility {

    public CustomInteraction(){
        super();
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.custom_interaction");
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/normal_paw.png");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 3;
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof LatexSnepEntity){
            return new TranslatableComponent("changed_addon.ability.custom_interaction.have_interaction");
        }
        return super.getSelectedDisplayText(entity);
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        if (entity.getChangedEntity() instanceof LatexSnepEntity latexSnepEntity){
            latexSnepEntity.WantLoaf = !latexSnepEntity.WantLoaf;
        }
    }
}
