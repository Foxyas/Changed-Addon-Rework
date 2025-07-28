package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.world.inventory.UnifuserGuiMenu;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReturnPlayerNameProcedure {
    public static String execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return "";
        Entity TEST = null;
        String no_owner = "";
        String owner = "";
        if ((entity instanceof Player _plr && _plr.containerMenu instanceof UnifuserGuiMenu)
                && (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY)
                .is(ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE)) {
            TEST = (new Object() {
                public Entity get(LevelAccessor _world, String _uuid) {
                    try {
                        Stream<Entity> _entities;
                        if (_world instanceof ClientLevel _level) {
                            _entities = StreamSupport.stream(_level.entitiesForRendering().spliterator(), false);
                        } else if (_world instanceof ServerLevel _level) {
                            _entities = StreamSupport.stream(_level.getAllEntities().spliterator(), false);
                        } else {
                            return null;
                        }
                        return _entities.filter(_e -> _e.getStringUUID().equals(_uuid)).findFirst().get();
                    } catch (Exception _e) {
                    }
                    return null;
                }
            }).get(world, ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("owner")));
        } else if ((entity instanceof Player _plr && _plr.containerMenu instanceof UnifuserGuiMenu)
                && !((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY)
                .is(ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE))) {
            TEST = null;
        }
        no_owner = new TranslatableComponent("text.changed.syringe.no_owner").getString();
        owner = new TranslatableComponent("text.changed.syringe.owner").getString();
        if (TEST == null) {
            return no_owner;
        }
        return owner.replace("%s", TEST.getDisplayName().getString());
    }
}
