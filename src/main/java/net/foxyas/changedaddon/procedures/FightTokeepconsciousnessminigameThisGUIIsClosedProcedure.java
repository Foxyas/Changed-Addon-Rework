package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModKeyMappings;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.lwjgl.glfw.GLFW;

public class FightTokeepconsciousnessminigameThisGUIIsClosedProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        Entity ent_target = null;
        String Msg = "";
        if (world.isClientSide()) {
            ent_target = entity;
            Msg = (new TranslatableComponent("changedaddon.warn.close_fight_to_keep_consciousness").getString()).replace("(KEY)",
                    GLFW.glfwGetKeyName(ChangedAddonModKeyMappings.OPEN_STRUGGLE_MENU.getKey().getValue(), GLFW.glfwGetKeyScancode(ChangedAddonModKeyMappings.OPEN_STRUGGLE_MENU.getKey().getValue())));
            if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).consciousness_fight_give_up
                    && (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).consciousness_fight_progress >= 25) {
                if (entity instanceof Player _player) {
                    _player.displayClientMessage(new TextComponent(Msg), true);
                }
            }
        }
    }
}
