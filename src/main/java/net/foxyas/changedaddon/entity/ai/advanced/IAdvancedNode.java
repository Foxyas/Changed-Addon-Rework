package net.foxyas.changedaddon.entity.ai.advanced;

import net.minecraft.world.level.pathfinder.Node;

public interface IAdvancedNode {
    void setJumpNode(boolean jumpNode);
    boolean isJumpNode();
}
