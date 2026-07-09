package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonFacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.Facility;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilitySinglePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Facility.class, remap = false)
public class FacilityMixin {
    

    @Inject(method = "tryGeneratePieces", at = @At("TAIL"))
    private void tellStuff(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos blockPos, Rotation rotation, CallbackInfo ci) {
        
        var container = builder.build();
        var pieces = container.pieces();

        ChunkPos center = context.chunkPos();

        for (StructurePiece piece : pieces) {
            if (piece instanceof FacilitySinglePiece.StructureInstance facilityPieceInstance) {

                FacilitySinglePieceInstanceAccessor accessor =
                        (FacilitySinglePieceInstanceAccessor) facilityPieceInstance;

                ResourceLocation templateName = accessor.getTemplateName();

                if (ChangedAddonFacilityPieces.BOSSES_ROOMS.contains(templateName)) {
                    ChangedAddonMod.LOGGER.info("Generated facility with the {} piece, at ChunkPos {}", templateName, center);
                }
            }
        }
    }
}
