package net.foxyas.changedaddon.world.features.facilities.events;

import net.ltxprogrammer.changed.world.data.ActiveFacilityInstance;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceEvent;
import net.ltxprogrammer.changed.world.features.structures.facility.Zone;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TestFacilityEvent extends FacilityPieceEvent {

    public TestFacilityEvent() {

    }

    @Override
    public void onPlayerEnterPiece(ServerLevel serverLevel, ServerPlayer serverPlayer, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable runnable) {

    }

    @Override
    public void onPlayerLeavePiece(ServerLevel serverLevel, ServerPlayer serverPlayer, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable runnable) {

    }

    @Override
    public void onPieceTick(ServerLevel serverLevel, ActiveFacilityInstance.PieceInfo pieceInfo, Zone zone, Runnable runnable) {

    }
}
