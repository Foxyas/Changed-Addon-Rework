package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.advancements.critereon.*;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.advancements.CriteriaTriggers.register;

@Mod.EventBusSubscriber
public class ChangedAddonCriteriaTriggers {

    public static final PatEntityTrigger PAT_ENTITY_TRIGGER = register(new PatEntityTrigger());
    public static final GrabEntityTrigger GRAB_ENTITY_TRIGGER = register(new GrabEntityTrigger());
    public static final LavaSwimmingTrigger LAVA_SWIMMING_TRIGGER = register(new LavaSwimmingTrigger());
    public static final SleepNextAPlushyTrigger SLEEP_NEXT_A_PLUSHY_TRIGGER = register(new SleepNextAPlushyTrigger());
    public static final UsedItemAmountTrigger USED_ITEM_AMOUNT_TRIGGER = register(new UsedItemAmountTrigger());
    public static final SimpleIDTrigger SIMPLE_ID_TRIGGER = register(new SimpleIDTrigger());
}
