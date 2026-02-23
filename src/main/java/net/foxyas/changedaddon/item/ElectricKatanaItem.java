package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ElectricKatanaItem extends AbstractKatanaItem {

    private static final Cacheable<ResourceLocation> IN_HAND_MODEL =
            Cacheable.of(() -> DistExecutor.unsafeCallWhenOn(
                    Dist.CLIENT,
                    () -> () -> new ModelResourceLocation(
                            ChangedAddonMod.resourceLoc("electric_katana_in_hand_model"),
                            "inventory"
                    )
            ));


    public ElectricKatanaItem() {
        super(new Tier() {
                  public int getUses() {
                      return 1324;
                  }

                  public float getSpeed() {
                      return 4f;
                  }

                  public float getAttackDamageBonus() {
                      return 4f;
                  }

                  public int getLevel() {
                      return 1;
                  }

                  public int getEnchantmentValue() {
                      return 30;
                  }

                  public @NotNull Ingredient getRepairIngredient() {
                      return CompoundIngredient.of(Ingredient.of(new ItemStack(ChangedAddonItems.ELECTRIC_KATANA.get())), Ingredient.of(ItemTags.create(ResourceLocation.parse("changed_addon:tsc_katana_repair"))));
                  }
              }, 3, -2.3f, new Item.Properties()
        );
    }


    @Override
    public ResourceLocation getModelLocation(ItemStack itemStack, ItemDisplayContext type) {
        return SpecializedItemRendering.isGUI(type) ? null : IN_HAND_MODEL.get();
    }

    @Override
    public void loadSpecialModels(Consumer<ResourceLocation> consumer) {
        consumer.accept(IN_HAND_MODEL.get());
    }
}
