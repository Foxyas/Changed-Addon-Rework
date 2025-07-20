package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.advancements.critereon.PatEntityTrigger;
import net.foxyas.changedaddon.advancements.critereon.LavaSwimmingTrigger;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.advancements.CriteriaTriggers.register;

@Mod.EventBusSubscriber
public class ChangedAddonCriteriaTriggers {

    public static final PatEntityTrigger PAT_ENTITY_TRIGGER = register(new PatEntityTrigger());
    public static final LavaSwimmingTrigger LAVA_SWIMMING_TRIGGER = register(new LavaSwimmingTrigger());

}
