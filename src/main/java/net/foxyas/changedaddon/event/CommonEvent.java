package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvent {

    @SubscribeEvent
    public static void persistAttributes(PlayerEvent.Clone event) {
        Player oldP = event.getOriginal();
        Player newP = (Player) event.getEntity();
        newP.getAttribute(ChangedAddonAttributes.LATEX_RESISTANCE.get()).setBaseValue(oldP.getAttribute(ChangedAddonAttributes.LATEX_RESISTANCE.get()).getBaseValue());
        newP.getAttribute(ChangedAddonAttributes.LATEX_INFECTION.get()).setBaseValue(oldP.getAttribute(ChangedAddonAttributes.LATEX_INFECTION.get()).getBaseValue());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if(!player.isAlive()) return;

        maskTransfur(player, player.level);

        tickInfectionAndRes(player);

        tickUntransfur(player);
    }

    private static void maskTransfur(Player player, Level level){
        int doTransfur = level.getLevelData().getGameRules().getInt(ChangedAddonGameRules.DO_DARK_LATEX_MASK_TRANSFUR);
        if(doTransfur <= 0) return;

        int maskHeldTimer = player.getPersistentData().getInt("HoldingDarkLatexMask");
        if(ProcessTransfur.isPlayerTransfurred(player)){
            if (maskHeldTimer > 0) {
                player.getPersistentData().putInt("HoldingDarkLatexMask", maskHeldTimer - 1);
            }
            return;
        }

        InteractionHand maskHand = null;
        if(player.getMainHandItem().is(ChangedItems.DARK_LATEX_MASK.get())) maskHand = InteractionHand.MAIN_HAND;
        if(maskHand == null && player.getOffhandItem().is(ChangedItems.DARK_LATEX_MASK.get())) maskHand = InteractionHand.OFF_HAND;

        if(maskHand == null) {
            if (maskHeldTimer > 0) {
                player.getPersistentData().putDouble("HoldingDarkLatexMask", maskHeldTimer - 1);
            }
            return;
        }

        if (maskHeldTimer < doTransfur) {
            player.getPersistentData().putInt("HoldingDarkLatexMask", maskHeldTimer + 1);
            return;
        }

        ItemStack stack = player.getItemInHand(maskHand);
        stack.shrink(1);
        player.setItemInHand(maskHand, stack);//TODO test whether this is necessary
        player.getInventory().setChanged();

        ProcessTransfur.progressTransfur(player, Float.MAX_VALUE, player.getRandom().nextInt(3) < 2
                ? ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getRandomVariant(player.getRandom())
                : ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get(), TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));

        player.getPersistentData().putInt("HoldingDarkLatexMask", 0);
    }

    private static void tickInfectionAndRes(Player player){
        if(ProcessTransfur.isPlayerTransfurred(player)) return;

        float progress = ProcessTransfur.getPlayerTransfurProgress(player);
        float newProgress = progress;

        float latexRes = (float) player.getAttributeValue(ChangedAddonAttributes.LATEX_RESISTANCE.get());
        if(latexRes > 0) newProgress -= .5f * latexRes;

        if(!player.isCreative() && !player.isSpectator()) {
            float TransfurInfectionAttribute = (float) player.getAttributeValue(ChangedAddonAttributes.LATEX_INFECTION.get());
            if (TransfurInfectionAttribute > 0) {
                newProgress += progress * TransfurInfectionAttribute / 100;
                newProgress = Mth.clamp(newProgress, 0f, (float) ProcessTransfur.getEntityTransfurTolerance(player) * 0.995f);
            }
        }

        if(progress != newProgress) {
            ProcessTransfur.setPlayerTransfurProgress(player, newProgress);
        }
    }

    private static void tickUntransfur(Player player) {
        ChangedAddonModVariables.PlayerVariables vars = ChangedAddonModVariables.PlayerVariables.of(player);
        if(vars == null) return;

        if(!player.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())){
            if (vars.untransfurProgress > 0) {
                vars.untransfurProgress -= .1f;
                vars.syncPlayerVariables(player);
            }
            return;
        }

        if(!ProcessTransfur.isPlayerTransfurred(player)) return;

        if (vars.untransfurProgress < 0) {
            vars.untransfurProgress = 0;
        } else {
            vars.untransfurProgress += (ProcessTransfur.isPlayerNotLatex(player) ? 0.1 : 0.2);

            if (player.isSleeping()) vars.untransfurProgress += .5f;
        }
        vars.syncPlayerVariables(player);
    }
}
