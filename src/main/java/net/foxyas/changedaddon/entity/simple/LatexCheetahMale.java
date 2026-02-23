package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LatexCheetahMale extends AbstractCheetahEntity {

    public LatexCheetahMale(EntityType<? extends LatexCheetahMale> entityType, Level level) {
        super(entityType, level);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void addSpawnConfiguration(SpawnPlacementRegisterEvent event) {
        event.register(ChangedAddonEntities.LATEX_CHEETAH_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ChangedEntity::checkEntitySpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        return interactionOrTryTame(player, hand, this.getUnderlyingPlayer());
    }

    public InteractionResult interactionOrTryTame(Player player, InteractionHand hand, Player Host) {
        ItemStack itemstack = player.getItemInHand(hand);
		/*if(Host != null){
			return super.mobInteract(player, hand);
		}*/

        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || this.isTameItem(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (!this.isTame() && this.isTameItem(itemstack)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                boolean isTransfur = ProcessTransfur.isPlayerTransfurred(player);

                if (!isTransfur && this.random.nextInt(2) == 0) { // One in 2 chance
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else if (isTransfur && this.random.nextInt(12) == 0) { //One in 12
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, hand);
        }
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }
}