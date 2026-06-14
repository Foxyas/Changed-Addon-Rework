package net.foxyas.changedaddon.extension.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

import java.util.*;

public class DefaultInformationDisplay implements Display {
    public static CategoryIdentifier<DefaultInformationDisplay> INFO = CategoryIdentifier.of(ChangedAddonMod.MODID, "plugins/information");

    private EntryIngredient entryStacks;
    private List<Component> texts;
    private Component name;
    
    protected DefaultInformationDisplay(EntryIngredient entryStacks, Component name) {
        this.entryStacks = entryStacks;
        this.name = name;
        this.texts = Lists.newArrayList();
    }
    
    public static DefaultInformationDisplay createFromEntries(EntryIngredient entryStacks, Component name) {
        return new DefaultInformationDisplay(entryStacks, name);
    }
    
    public static DefaultInformationDisplay createFromEntry(EntryStack<?> entryStack, Component name) {
        return createFromEntries(EntryIngredient.of(entryStack), name);
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        return Collections.singletonList(entryStacks);
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        return Collections.singletonList(entryStacks);
    }
    
    public DefaultInformationDisplay line(Component line) {
        texts.add(line);
        return this;
    }
    
    @SafeVarargs
    public final <T extends Component> DefaultInformationDisplay lines(T... lines) {
        texts.addAll(Arrays.asList(lines));
        return this;
    }
    
    public <T extends Component> DefaultInformationDisplay lines(Collection<T> lines) {
        texts.addAll(lines);
        return this;
    }
    
    public EntryIngredient getEntryStacks() {
        return entryStacks;
    }
    
    public Component getName() {
        return name;
    }
    
    public List<Component> getTexts() {
        return texts;
    }
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return INFO;
    }
    
    public static DisplaySerializer<DefaultInformationDisplay> serializer() {
        return new DisplaySerializer<DefaultInformationDisplay>() {
            @Override
            public CompoundTag save(CompoundTag tag, DefaultInformationDisplay display) {
                tag.put("stacks", display.getEntryStacks().saveIngredient());
                tag.putString("name", Component.Serializer.toJson(display.getName()));
                ListTag descriptions = new ListTag();
                for (Component text : display.getTexts()) {
                    descriptions.add(StringTag.valueOf(Component.Serializer.toJson(text)));
                }
                tag.put("descriptions", descriptions);
                return tag;
            }
            
            @Override
            public DefaultInformationDisplay read(CompoundTag tag) {
                EntryIngredient stacks = EntryIngredient.read(tag.getList("stacks", Tag.TAG_COMPOUND));
                Component name = Component.Serializer.fromJson(tag.getString("name"));
                List<Component> descriptions = new ArrayList<>();
                for (Tag descriptionTag : tag.getList("descriptions", Tag.TAG_STRING)) {
                    descriptions.add(Component.Serializer.fromJson(descriptionTag.getAsString()));
                }
                return new DefaultInformationDisplay(stacks, name).lines(descriptions);
            }
        };
    }
}
