package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DarkLatexMaskTransfurHandleProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player.level, event.player);
        }
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        if ((world.getLevelData().getGameRules().getInt(ChangedAddonGameRules.DO_DARK_LATEX_MASK_TRANSFUR)) > 0) {
            if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
                if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:dark_latex_mask"))) {
                    if (entity.getPersistentData().getDouble("HoldingDarkLatexMask") < (world.getLevelData().getGameRules().getInt(ChangedAddonGameRules.DO_DARK_LATEX_MASK_TRANSFUR))) {
                        entity.getPersistentData().putDouble("HoldingDarkLatexMask", (entity.getPersistentData().getDouble("HoldingDarkLatexMask") + 1));
                    } else {
                        if (entity instanceof LivingEntity _entity) {
                            ItemStack _setstack = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                            _setstack.setCount(((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)).getCount() - 1);
                            _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                            if (_entity instanceof Player _player)
                                _player.getInventory().setChanged();
                        }
                        AddTransfurProgressProcedure.addDarkLatex(entity, 20);
                        entity.getPersistentData().putDouble("HoldingDarkLatexMask", 0);
                    }
                } else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:dark_latex_mask"))) {
                    if (entity.getPersistentData().getDouble("HoldingDarkLatexMask") < (world.getLevelData().getGameRules().getInt(ChangedAddonGameRules.DO_DARK_LATEX_MASK_TRANSFUR))) {
                        entity.getPersistentData().putDouble("HoldingDarkLatexMask", (entity.getPersistentData().getDouble("HoldingDarkLatexMask") + 1));
                    } else {
                        if (entity instanceof LivingEntity _entity) {
                            ItemStack _setstack = (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
                            _setstack.setCount(((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY)).getCount() - 1);
                            _entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                            if (_entity instanceof Player _player)
                                _player.getInventory().setChanged();
                        }
                        AddTransfurProgressProcedure.addDarkLatex(entity, 20);
                        entity.getPersistentData().putDouble("HoldingDarkLatexMask", 0);
                    }
                } else {
                    if (entity.getPersistentData().getDouble("HoldingDarkLatexMask") > 0) {
                        entity.getPersistentData().putDouble("HoldingDarkLatexMask", (entity.getPersistentData().getDouble("HoldingDarkLatexMask") - 1));
                    }
                }
            } else {
                if (entity.getPersistentData().getDouble("HoldingDarkLatexMask") > 0) {
                    entity.getPersistentData().putDouble("HoldingDarkLatexMask", (entity.getPersistentData().getDouble("HoldingDarkLatexMask") - 1));
                }
            }
        }
    }
}
