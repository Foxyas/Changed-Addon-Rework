package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.entity.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
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
            switch (lumi.getGlowStage()) {
                case AbstractLuminarcticLeopard.GLOW_NONE -> lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_PULSE);
                case AbstractLuminarcticLeopard.GLOW_PULSE -> lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_ALWAYS);
                case AbstractLuminarcticLeopard.GLOW_ALWAYS -> lumi.setGlowStage(AbstractLuminarcticLeopard.GLOW_NONE);
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
