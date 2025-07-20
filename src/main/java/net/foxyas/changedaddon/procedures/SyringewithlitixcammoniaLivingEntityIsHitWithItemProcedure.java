package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class SyringewithlitixcammoniaLivingEntityIsHitWithItemProcedure {
    public static void execute(Entity entity, Entity sourceentity, ItemStack itemstack) {
        if (entity == null || sourceentity == null)
            return;
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true) {
            if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == true) {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 640, 0, false, false));
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
                    if (entity instanceof Player _player && !_player.level.isClientSide())
                        _player.displayClientMessage(new TextComponent("for some reason this seems to have slowed effect"), true);
                }
            } else {
                if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                    _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 400, 0, false, false));
            }
        }
        if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity")))) {
            if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
                _entity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 400, 0, false, false));
        }
        if (!(new Object() {
            public boolean checkGamemode(Entity _ent) {
                if (_ent instanceof ServerPlayer _serverPlayer) {
                    return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
                } else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
                    return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
                }
                return false;
            }
        }.checkGamemode(sourceentity))) {
            if ((itemstack).getDamageValue() == (itemstack).getMaxDamage() - 1) {
                if (sourceentity instanceof Player _player) {
                    ItemStack _setstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:syringe")));
                    _setstack.setCount(1);
                    ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
                }
                {
                    Entity _ent = sourceentity;
                    if (!_ent.level.isClientSide() && _ent.getServer() != null)
                        _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:sword1 player @a ~ ~ ~ 1");
                }
                if (sourceentity instanceof Player _player) {
                    ItemStack _stktoremove = itemstack;
                    _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
                }
            } else {
                {
                    ItemStack _ist = itemstack;
                    if (_ist.hurt(1, new Random(), null)) {
                        _ist.shrink(1);
                        _ist.setDamageValue(0);
                    }
                }
                {
                    Entity _ent = sourceentity;
                    if (!_ent.level.isClientSide() && _ent.getServer() != null)
                        _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:sword1 player @a ~ ~ ~ 1");
                }
            }
        } else {
            {
                Entity _ent = sourceentity;
                if (!_ent.level.isClientSide() && _ent.getServer() != null)
                    _ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "playsound changed:sword1 player @a ~ ~ ~ 1");
            }
        }
    }
}
