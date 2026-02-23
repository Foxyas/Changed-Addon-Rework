package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AdvancedCatalyzerBlockEntity extends CatalyzerBlockEntity {

    public AdvancedCatalyzerBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonBlockEntities.ADVANCED_CATALYZER.get(), position, state);
    }

    @Override
    public float getSpeedMultiplier() {
        return super.getSpeedMultiplier() * 4;
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.literal("advanced_catalyzer");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Advanced Catalyzer");
    }
}
