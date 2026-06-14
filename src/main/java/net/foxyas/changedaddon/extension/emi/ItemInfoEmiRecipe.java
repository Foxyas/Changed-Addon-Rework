package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.foxyas.changedaddon.extension.emi.ChangedAddonEmiPlugin.getItemIdFromStack;

public class ItemInfoEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<? extends Component> description;
    private final int width;
    private final int height;

    public ItemInfoEmiRecipe(ResourceLocation id, List<EmiStack> items, List<? extends Component> description) {
        this.id = id;
        // Transforma a lista de ItemStacks em ingredientes visíveis do EMI
        this.inputs = items.stream().map(EmiIngredient.class::cast).toList();
        this.description = description;
        this.width = 144; // Largura padrão para exibição de texto

        // Calcula a altura dinamicamente baseado na quantidade de linhas de texto
        var font = Minecraft.getInstance().font;
        int textHeight = this.description.size() * (font.lineHeight + 2);
        this.height = Math.max(40, textHeight + 24); // Garante espaço para o item + texto
    }

    public ItemInfoEmiRecipe(EmiStack item, List<? extends Component> description) {
        this(
                ResourceLocation.fromNamespaceAndPath(getItemIdFromStack(item.getItemStack().getItem()).getNamespace(), "info/" + getItemIdFromStack(item.getItemStack().getItem()).getPath()),
                List.of(item),
                description
        );
    }


    public ItemInfoEmiRecipe(ItemStack item, List<? extends Component> description) {
        this(
                ResourceLocation.fromNamespaceAndPath(getItemIdFromStack(item.getItem()).getNamespace(), "info/" + getItemIdFromStack(item.getItem()).getPath()),
                List.of(EmiStack.of(item)),
                description
        );
    }


    public static ItemInfoEmiRecipe createFromItemStacks(ResourceLocation id, List<ItemStack> items, List<? extends Component> descriptions) {
        List<EmiStack> emiStacks = items.stream().map(EmiStack::of).toList();
        return new ItemInfoEmiRecipe(id, emiStacks, descriptions);
    }

    public static ItemInfoEmiRecipe createFromItemStack(ResourceLocation id, ItemStack item, List<? extends Component> descriptions) {
        EmiStack emiStack = EmiStack.of(item);
        return new ItemInfoEmiRecipe(id, List.of(emiStack), descriptions);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.INFO;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(); // Receitas de informação não geram um "output" físico
    }

    @Override
    public int getDisplayWidth() {
        return this.width;
    }

    @Override
    public int getDisplayHeight() {
        return this.height;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int itemX = (this.width / 2) - (this.inputs.size() * 18 / 2);
        for (int i = 0; i < this.inputs.size(); i++) {
            widgets.addSlot(this.inputs.get(i), itemX + (i * 18), 0);
        }

        var font = Minecraft.getInstance().font;
        int yOffset = 22;
        for (Component line : this.description) {
            widgets.addText(line.getVisualOrderText(), 0, yOffset, 0xFFFFFF, true);
            yOffset += font.lineHeight + 2;
        }
    }
}