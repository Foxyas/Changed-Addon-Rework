package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;


public class CustomInteractionInstance extends AbstractAbilityInstance {

    public CustomInteractionInstance(AbstractAbility<?> ability, IAbstractChangedEntity entity){
        super(ability,entity);
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
        if (entity.getChangedEntity() instanceof LatexSnepEntity latexSnepEntity){
            latexSnepEntity.WantLoaf = !latexSnepEntity.WantLoaf;
        } else if (entity.getChangedEntity() instanceof AbstractLuminarcticLeopard lumi) {
            lumi.SetActivatedAbility(!lumi.isActivatedAbility());
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void stopUsing() {

    }

    public void onSelected() {
        if (entity.getEntity() instanceof Player player && ability.getSelectedDisplayText(this.entity) != null){
            player.displayClientMessage(ability.getSelectedDisplayText(this.entity),true);
        }
    }
}
