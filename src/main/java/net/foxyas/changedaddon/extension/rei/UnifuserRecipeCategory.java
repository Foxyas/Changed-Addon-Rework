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

public class UnifuserRecipeCategory implements DisplayCategory<UnifuserRecipeDisplay> {
    public final static ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/screens/jei_unifuser_screen");

    @Override
    public CategoryIdentifier<? extends UnifuserRecipeDisplay> getCategoryIdentifier() {
        return ChangedAddonReiPlugin.UNIFUSER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.changed_addon.unifuser");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ChangedAddonBlocks.UNIFUSER.get());
    }

    @Override
    public List<Widget> setupDisplay(UnifuserRecipeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = new ArrayList<>();

        // Fundo (Usando o slot padrão do REI ou sua textura)
        widgets.add(Widgets.createRecipeBase(bounds));
        // Supondo que 200 ticks seja o tempo base e o progressSpeed aumente a velocidade
        double duration = 100.0 / display.getProgressSpeed();

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18))
                .animationDurationTicks(duration));

        // Slots de Entrada (Baseado nas posições do seu JEI)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 36)).entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 37, startPoint.y + 18)).entries(display.getInputEntries().get(2)).markInput());

        // Slot de Saída
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 96, startPoint.y + 18)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 96, startPoint.y + 18)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        // Criamos o slot normalmente
        Slot itemSlot = Widgets.createSlot(new Point(startPoint.x + 64, startPoint.y + 36))
                .entries(List.of(EntryStacks.of(ChangedAddonItems.UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM.get())))
                .disableBackground() // Opcional: remove o quadrado de slot se for apenas decorativo
                .markInput(); // Ou markOutput se preferir

        // Usamos o Widgets.withTooltip para envolver o widget anterior com a tradução
        widgets.add(Widgets.withTooltip(itemSlot, Component.translatable("gui.changed_addon.recipe_progress", display.getProgressSpeed())));

        return widgets;
    }
}