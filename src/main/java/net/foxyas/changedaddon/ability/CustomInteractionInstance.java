package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.entity.advanced.LatexSnepEntity;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.simple.LatexKaylaSharkEntity;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.player.Player;


public class CustomInteractionInstance extends AbstractAbilityInstance {

    public CustomInteractionInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity) {
        super(ability, entity);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public AbstractAbility.UseType getUseType() {
        return AbstractAbility.UseType.INSTANT;
    }

    @Override
    public boolean canKeepUsing() {
        return true;
    }

    @Override
    public void startUsing() {
        if (entity.getChangedEntity() instanceof LatexSnepEntity latexSnepEntity) {
            latexSnepEntity.WantLoaf = !latexSnepEntity.WantLoaf;
        } else if (entity.getChangedEntity() instanceof AbstractLuminarcticLeopard lumi) {
            if (entity.isCrouching()) {
                switch (lumi.getGlowStage()) {
                    case AbstractLuminarcticLeopard.GLOW_NONE ->
                            lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_PULSE);
                    case AbstractLuminarcticLeopard.GLOW_PULSE ->
                            lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_ALWAYS);
                    case AbstractLuminarcticLeopard.GLOW_ALWAYS ->
                            lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_NONE);
                }
            } else {
                lumi.setActivatedAbility(!lumi.isActivatedAbility());
            }
        } else if (entity.getChangedEntity() instanceof AvaliEntity avaliEntity) {
            float scale = avaliEntity.getDimensionScale();
            if (scale <= AvaliEntity.SizeScaling.NORMAL.getScale()) {
                avaliEntity.setDimensionScale(AvaliEntity.SizeScaling.TALL.getScale());
            } else if (scale < AvaliEntity.SizeScaling.VERY_TALL.getScale()
                    && scale >= AvaliEntity.SizeScaling.TALL.getScale()) {
                avaliEntity.setDimensionScale(AvaliEntity.SizeScaling.VERY_TALL.getScale());
            } else {
                avaliEntity.setDimensionScale(AvaliEntity.SizeScaling.NORMAL.getScale());
            }
        } else if (entity.getChangedEntity() instanceof LuminaraFlowerBeastEntity luminaraFlowerBeast) {
            if (!luminaraFlowerBeast.isHyperAwakened()) return;
            luminaraFlowerBeast.spawnParticles = !luminaraFlowerBeast.spawnParticles;
            entity.displayClientMessage(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.luminara_beast.action", luminaraFlowerBeast.spawnParticles), true);
        } else if (entity.getChangedEntity() instanceof LatexKaylaSharkEntity latexKaylaSharkEntity) {
            boolean value = !latexKaylaSharkEntity.getGlowingState();
            latexKaylaSharkEntity.setGlowingState(value);
            entity.displayClientMessage(Component.translatable("ability.changed_addon.custom_interaction.have_interaction.latex_kayla_shark.action", value), true);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void stopUsing() {

    }

    public void onSelected() {
        Component text = ability.getSelectedDisplayText(this.entity);
        if (entity.getEntity() instanceof Player player && text != null) {
            player.displayClientMessage(text, true);
        }
    }
}
