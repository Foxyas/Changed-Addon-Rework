package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Iterator;

public class TransfurTotemRightclickedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        String form = "";
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                || (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                || (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == itemstack.getItem()
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
            if (!entity.isShiftKeyDown()) {
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
                    if (!(itemstack.getOrCreateTag().getString("form")).isEmpty()) {
                        SummonDripParticlesProcedure.execute(entity);
                        PlayerUtilProcedure.UnTransfurPlayer(entity);
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                        if (world.isClientSide())
                            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                        if (world instanceof Level _level) {
                            if (!_level.isClientSide()) {
                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1);
                            } else {
                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1, false);
                            }
                        }
                        if (entity instanceof ServerPlayer _player) {
                            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:transfur_totem_advancement_1"));
                            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                            if (!_ap.isDone()) {
                                Iterator _iterator = _ap.getRemainingCriteria().iterator();
                                while (_iterator.hasNext())
                                    _player.getAdvancements().award(_adv, (String) _iterator.next());
                            }
                        }
                    } else {
                        if (entity instanceof Player _player && !_player.level.isClientSide())
                            _player.displayClientMessage(new TextComponent("Any form linked please link one \u00A7e<Shift+Click>"), true);
                    }
                } else {
                    if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_puro_kind/female")) {
                        itemstack.getOrCreateTag().putString("form", "changed_addon:form_latex_puro_kind/female");
                    }
                    if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
                        if (entity instanceof Player _player && !_player.level.isClientSide())
                            _player.displayClientMessage(new TextComponent("Any form linked please link one \u00A7e<Shift+Click>"), true);
                    } else {
                        form = itemstack.getOrCreateTag().getString("form");
                        PlayerUtilProcedure.TransfurPlayer(entity, form);
                        if (world.isClientSide())
                            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                        if (entity instanceof ServerPlayer _player) {
                            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:transfur_totem_advancement_1"));
                            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                            if (!_ap.isDone()) {
                                Iterator _iterator = _ap.getRemainingCriteria().iterator();
                                while (_iterator.hasNext())
                                    _player.getAdvancements().award(_adv, (String) _iterator.next());
                            }
                        }
                    }
                }
            } else {
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
                    if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
                        if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() == false) {
                            if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
                                itemstack.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.attack_iron_door")), SoundSource.NEUTRAL, 1, 0, false);
                                    }
                                }
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 50);
                                if (entity instanceof Player _player && !_player.level.isClientSide())
                                    _player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.latex_totem.notvalid").getString())), true);
                            } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:special")) {
                                itemstack.getOrCreateTag().putString("form", "changed:form_light_latex_wolf");
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            }
                        } else {
                            if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:form")) {
                                itemstack.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form")) {
                                itemstack.getOrCreateTag().putString("form", ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm));
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            } else if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:special")) {
                                itemstack.getOrCreateTag().putString("form", "changed:form_light_latex_wolf");
                                if (world.isClientSide())
                                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                                if (entity instanceof Player _player)
                                    _player.getCooldowns().addCooldown(itemstack.getItem(), 100);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                            }
                        }
                    } else {
                        itemstack.getOrCreateTag().putString("form", "");
                        if (world.isClientSide())
                            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(itemstack.getItem(), 50);
                        if (world instanceof Level _level) {
                            if (!_level.isClientSide()) {
                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1);
                            } else {
                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1, false);
                            }
                        }
                    }
                } else {
                    if (!(itemstack.getOrCreateTag().getString("form")).isEmpty()) {
                        itemstack.getOrCreateTag().putString("form", "");
                        if (entity instanceof Player _player)
                            _player.getCooldowns().addCooldown(itemstack.getItem(), 50);
                        if (world.isClientSide())
                            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                        if (world instanceof Level _level) {
                            if (!_level.isClientSide()) {
                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1);
                            } else {
                                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.deactivate")), SoundSource.NEUTRAL, 1, 1, false);
                            }
                        }
                    }
                }
            }
        }
    }
}
