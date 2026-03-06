package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CatalyzerRecipeCategory implements DisplayCategory<CatalyzerRecipeDisplay> {

    @Override
    public CategoryIdentifier<? extends CatalyzerRecipeDisplay> getCategoryIdentifier() {
        return ChangedAddonReiPlugin.CATALYZER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.changed_addon.catalyzer");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ChangedAddonBlocks.CATALYZER.get());
    }

    @Override
    public List<Widget> setupDisplay(CatalyzerRecipeDisplay display, Rectangle bounds) {
        // Centraliza o conteúdo dentro da área do REI
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = new ArrayList<>();

        // Fundo da receita
        widgets.add(Widgets.createRecipeBase(bounds));

        // Supondo que 200 ticks seja o tempo base e o progressSpeed aumente a velocidade
        double duration = 100.0 / display.getProgressSpeed();

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 48, startPoint.y + 18))
                .animationDurationTicks(duration));

        // Slot de Entrada (Coordenada x=12, y=18 no JEI)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 12, startPoint.y + 18))
                .entries(display.getInputEntries().get(0))
                .markInput());

        // Slot de Saída (Coordenada x=96, y=18 no JEI)
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 96, startPoint.y + 18)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 96, startPoint.y + 18))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());

        // Item Decorativo com Tooltip de Nitrogênio e Progresso
        Slot infoIcon = Widgets.createSlot(new Point(startPoint.x + 51, startPoint.y + 36))
                .entry(EntryStacks.of(ChangedAddonItems.CATALYZER_BLOCK_ILLUSTRATIVE_ITEM.get()))
                .disableBackground();

        // Aplicando o tooltip corretamente via Widgets.withTooltip
        widgets.add(Widgets.withTooltip(infoIcon,
                Component.translatable("gui.changed_addon.catalyzer.nitrogen_usage",
                        display.getProgressSpeed(),
                        display.getNitrogenUsage())
        ));

        return widgets;
    }
}