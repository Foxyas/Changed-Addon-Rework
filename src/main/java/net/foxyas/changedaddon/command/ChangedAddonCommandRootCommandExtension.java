package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.foxyas.changedaddon.block.advanced.TimedKeypad;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonCommandRootCommandExtension {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("setTimerInKeypad")
				.then(Commands.argument("timer", IntegerArgumentType.integer(0, 9999))
						.requires(cs -> cs.hasPermission(0)) // Minimum permission
						.executes(context -> {
							CommandSourceStack source = context.getSource();

							if (!(source.getEntity() instanceof ServerPlayer player)) {
								source.sendFailure(new TextComponent("This command can only be used by players."));
								return 0;
							}

							ItemStack heldItem = player.getMainHandItem();
							if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
								source.sendFailure(new TextComponent("You must be holding a block item."));
								return 0;
							}

							Block block = blockItem.getBlock();
							if (!(block instanceof TimedKeypad)) {
								source.sendFailure(new TextComponent("The block must be a TimedKeypad."));
								return 0;
							}

							int timerValue = IntegerArgumentType.getInteger(context, "timer");
							CompoundTag tag = heldItem.getOrCreateTag();
							tag.putInt("TimerValue", timerValue);

							source.sendSuccess(new TextComponent("Timer set to " + timerValue + "."), true);
							return 1;
						})
				)
		);
	}
}
