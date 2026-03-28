package net.foxyas.changedaddon.mixins.level.pathfinder;

import net.foxyas.changedaddon.entity.ai.advanced.IAdvancedNode;
import net.minecraft.world.level.pathfinder.Node;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Node.class)
public class NodeMixin implements IAdvancedNode {

    protected boolean isToJumpNode = false;

    @Override
    public void setJumpNode(boolean jumpNode) {
        this.isToJumpNode = jumpNode;
    }

    @Override
    public boolean isJumpNode() {
        return this.isToJumpNode;
    }
}
