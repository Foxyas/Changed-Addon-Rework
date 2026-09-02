package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.entity.ai.LatexFavor;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.world.inventory.UpdateableMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class TamedLatexMenu extends AbstractContainerMenu implements UpdateableMenu {
    public final Player player;
    public TamableLatexEntityFavors iTamedLatex;
    public ChangedEntity tamedLatex;

    public TamedLatexMenu(int id, Inventory inventory, TamableLatexEntityFavors tamedLatex) {
        super(ChangedAddonMenus.TAMED_LATEX.get(), id);
        this.tamedLatex = tamedLatex.getSelf();
        this.iTamedLatex = tamedLatex;
        this.player = inventory.player;
    }

    public TamedLatexMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ChangedAddonMenus.TAMED_LATEX.get(), id);
        this.player = inv.player;

        if (extraData == null)
            return;

        this.tamedLatex = (ChangedEntity) inv.player.level().getEntity(extraData.readInt());
        this.iTamedLatex = tamedLatex instanceof TamableLatexEntityFavors tamableLatexEntityFavors ? tamableLatexEntityFavors : null;
    }

    @Override
    public ItemStack quickMoveStack(Player viewer, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player viewer) {
        if (this.tamedLatex.isRemoved()) {
            return false;
        } else if (this.iTamedLatex.getOwner() != viewer) {
            return false;
        } else {
            return !(viewer.distanceToSqr(this.tamedLatex) > 64.0D);
        }
    }

    @Override
    public int getId() {
        return containerId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(CompoundTag payload, LogicalSide receiver, @Nullable ServerPlayer origin) {
        if (receiver == LogicalSide.SERVER && origin == this.iTamedLatex.getOwner()) {
            switch (payload.getString("command")) {
                case "view_inventory" -> {
                    NetworkHooks.openScreen((ServerPlayer) this.player, new SimpleMenuProvider(
                            (id, inv, viewer) -> new TamedLatexInventoryMenu(id, this.player, this.iTamedLatex),
                            this.tamedLatex.getDisplayName()
                    ), extraData -> {
                        extraData.writeInt(this.tamedLatex.getId());
                    });
                }
                case "cycle_follow" -> {
                    this.iTamedLatex.setFollowOwner(!this.iTamedLatex.isFollowingOwner());
                    this.tamedLatex.setJumping(false);
                    this.tamedLatex.getNavigation().stop();
                }
                case "cycle_target_type" -> {
                    this.iTamedLatex.setTargetType(this.iTamedLatex.getTargetType().cycle());
                    this.tamedLatex.setTarget(null);
                }
                case "cycle_attack_type" -> {
                    this.iTamedLatex.setAttackType(this.iTamedLatex.getAttackType().cycle());
                    this.iTamedLatex.updateHeldItemChoice();
                }
                case "cycle_attack_condition" -> {
                    this.iTamedLatex.setAttackCondition(this.iTamedLatex.getAttackCondition().cycle());
                    this.tamedLatex.setTarget(null);
                }
                case "favor_fishing" -> {
                    this.iTamedLatex.setFavor(this.iTamedLatex.getCurrentFavor() != LatexFavor.FISHING ?
                            LatexFavor.FISHING : LatexFavor.NONE);
                }
                case "favor_caving" -> {
                    this.iTamedLatex.setFavor(this.iTamedLatex.getCurrentFavor() != LatexFavor.CAVING ?
                            LatexFavor.CAVING : LatexFavor.NONE);
                }
                case "favor_suit_owner" -> {
                    this.iTamedLatex.setFavor(this.iTamedLatex.getCurrentFavor() != LatexFavor.SUIT_OWNER ?
                            LatexFavor.SUIT_OWNER : LatexFavor.NONE);
                }
            }
        }
    }
}
