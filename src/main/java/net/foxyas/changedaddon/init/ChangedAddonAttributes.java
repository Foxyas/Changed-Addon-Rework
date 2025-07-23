package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ChangedAddonMod.MODID);
    public static final RegistryObject<Attribute> LATEX_RESISTANCE = ATTRIBUTES.register("latex_resistance", () -> (new RangedAttribute("attribute." + ChangedAddonMod.MODID + ".latex_resistance", 0, 0, 100)).setSyncable(true));
    public static final RegistryObject<Attribute> LATEX_INFECTION = ATTRIBUTES.register("latex_infection", () -> (new RangedAttribute("attribute." + ChangedAddonMod.MODID + ".latex_infection", 0, 0, 100)).setSyncable(true));

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus()));
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, LATEX_RESISTANCE.get());
        event.add(EntityType.PLAYER, LATEX_INFECTION.get());
    }
}
