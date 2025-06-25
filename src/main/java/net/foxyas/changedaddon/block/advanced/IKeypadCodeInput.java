package net.foxyas.changedaddon.block.advanced;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface IKeypadCodeInput {

    void sendCodeFeedBack(@Nullable Player player);
}
