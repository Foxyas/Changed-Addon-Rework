package net.foxyas.changedaddon.event;

import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.Event;

/**
 * Events fired when a PLAYER interacts with a LatexType.
 */
public abstract class LatexTypePlayerEvent extends Event {

    protected final Player player;
    protected final Level level;
    protected final LatexType latexType;
    protected final LatexCoverState latexState;
    protected final RandomSource random;

    protected LatexTypePlayerEvent(
            Player player,
            Level level,
            LatexType latexType,
            LatexCoverState latexState,
            RandomSource random
    ) {
        this.player = player;
        this.level = level;
        this.latexType = latexType;
        this.latexState = latexState;
        this.random = random;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public LatexType getLatexType() {
        return latexType;
    }

    public LatexCoverState getLatexState() {
        return latexState;
    }

    public RandomSource getRandom() {
        return random;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    /* ------------------------------------------------------------ */
    /* Right click                                                   */
    /* ------------------------------------------------------------ */

    public static class RightClick extends LatexTypePlayerEvent {

        private final InteractionHand hand;
        private final BlockHitResult hitResult;
        private InteractionResult result;
        private InteractionResult cancellationResult = InteractionResult.PASS;

        public RightClick(
                Player player,
                Level level,
                LatexType latexType,
                LatexCoverState latexState,
                InteractionHand hand,
                BlockHitResult hitResult,
                InteractionResult defaultResult,
                RandomSource random
        ) {
            super(player, level, latexType, latexState, random);
            this.hand = hand;
            this.hitResult = hitResult;
            this.result = defaultResult;
        }

        public InteractionHand getHand() {
            return hand;
        }

        public BlockHitResult getHitResult() {
            return hitResult;
        }

        public InteractionResult getInteractionResult() {
            return result;
        }

        public void setResult(InteractionResult result) {
            this.result = result;
        }

        public InteractionResult getCancellationResult() {
            return cancellationResult;
        }

        public void setCancellationResult(InteractionResult interactionResult) {
            this.cancellationResult = interactionResult;
        }
    }
}
