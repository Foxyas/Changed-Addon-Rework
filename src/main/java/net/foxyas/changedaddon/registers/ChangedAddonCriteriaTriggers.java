package net.foxyas.changedaddon.registers;

import net.foxyas.changedaddon.advancements.critereon.PatEntityTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import static net.minecraft.advancements.CriteriaTriggers.register;

@Mod.EventBusSubscriber
public class ChangedAddonCriteriaTriggers {

    public static final PatEntityTrigger PAT_ENTITY_TRIGGER = register(new PatEntityTrigger());

}
