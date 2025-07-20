package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TransfurTotemEntityRightClickProcedure {
    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != event.getPlayer().getUsedItemHand())
            return;
        execute(event, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getTarget(), event.getPlayer());
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        execute(null, world, x, y, z, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        ItemStack a = ItemStack.EMPTY;
        ItemStack b = ItemStack.EMPTY;
        String string = "";
        a = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
        b = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY);
        if (!(sourceentity instanceof Player _player && _player.getCooldowns().isOnCooldown(ChangedAddonItems.TRANSFUR_TOTEM.get()))) {
            if (a.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get() || b.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get()) {
                if (sourceentity.isShiftKeyDown()) {
                    if (entity instanceof Player) {
                        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
                            if (a.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
                                if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() == false) {
                                    if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
                                        if (sourceentity instanceof Player _player)
                                            _player.getCooldowns().addCooldown(a.getItem(), 20);
                                        if (world.isClientSide())
                                            Minecraft.getInstance().gameRenderer.displayItemActivation(a);
                                        a.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                            }
                                        }
                                    } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
                                        if (sourceentity instanceof Player _player)
                                            _player.getCooldowns().addCooldown(a.getItem(), 50);
                                        if (world.isClientSide())
                                            Minecraft.getInstance().gameRenderer.displayItemActivation(a);
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0);
                                            } else {
                                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0, false);
                                            }
                                        }
                                        if (entity instanceof Player _player && !_player.level.isClientSide())
                                            _player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.latex_totem.notvalid").getString())), true);
                                    }
                                } else {
                                    if (sourceentity instanceof Player _player)
                                        _player.getCooldowns().addCooldown(a.getItem(), 20);
                                    if (world.isClientSide())
                                        Minecraft.getInstance().gameRenderer.displayItemActivation(a);
                                    a.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                    if (world instanceof Level _level) {
                                        if (!_level.isClientSide()) {
                                            _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                        } else {
                                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                        }
                                    }
                                }
                            } else if (b.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
                                if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() == false) {
                                    if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
                                        if (sourceentity instanceof Player _player)
                                            _player.getCooldowns().addCooldown(b.getItem(), 20);
                                        if (world.isClientSide())
                                            Minecraft.getInstance().gameRenderer.displayItemActivation(b);
                                        b.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                            }
                                        }
                                    } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
                                        if (sourceentity instanceof Player _player)
                                            _player.getCooldowns().addCooldown(b.getItem(), 50);
                                        if (world.isClientSide())
                                            Minecraft.getInstance().gameRenderer.displayItemActivation(b);
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0);
                                            } else {
                                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0, false);
                                            }
                                        }
                                        if (entity instanceof Player _player && !_player.level.isClientSide())
                                            _player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.latex_totem.notvalid").getString())), true);
                                    }
                                } else {
                                    if (sourceentity instanceof Player _player)
                                        _player.getCooldowns().addCooldown(b.getItem(), 20);
                                    if (world.isClientSide())
                                        Minecraft.getInstance().gameRenderer.displayItemActivation(b);
                                    b.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                    if (world instanceof Level _level) {
                                        if (!_level.isClientSide()) {
                                            _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                        } else {
                                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (entity instanceof net.ltxprogrammer.changed.entity.ChangedEntity changedEntity) {
                            string = changedEntity.getSelfVariant() != null ? changedEntity.getSelfVariant().getFormId().toString() : "";
                            if (a.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get() && (a.getOrCreateTag().getString("form")).isEmpty()) {
                                if (sourceentity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(a.getItem(), 20);
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(a);
                                a.getOrCreateTag().putString("form", string);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            } else if (b.getItem() == ChangedAddonItems.TRANSFUR_TOTEM.get() && (b.getOrCreateTag().getString("form")).isEmpty()) {
                                if (sourceentity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(b.getItem(), 20);
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(b);
                                b.getOrCreateTag().putString("form", (string));
                            }
                        }
                    }
                }
            }
        }
    }
}
