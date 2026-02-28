package net.foxyas.changedaddon.mixins.mods.changed;

import net.foxyas.changedaddon.init.ChangedAddonFacilityPieces;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.world.features.structures.Facility;
import net.ltxprogrammer.changed.world.features.structures.StructurePiecesBuilderExtender;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilitySinglePiece;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Facility.class, remap = false)
public class FacilityMixin {


    @Inject(method = "generatePieces", at = @At("TAIL"))
    private static void tellStuff(StructurePiecesBuilder builder,
                                  PieceGenerator.Context<NoneFeatureConfiguration> context,
                                  CallbackInfo ci) {

        var container = builder.build();
        var pieces = container.pieces();

        ChunkPos center = context.chunkPos();

        for (StructurePiece piece : pieces) {
            if (piece instanceof FacilitySinglePiece.StructureInstance facilityPieceInstance) {

                FacilitySinglePieceInstanceAccessor accessor =
                        (FacilitySinglePieceInstanceAccessor) facilityPieceInstance;

                ResourceLocation templateName = accessor.getTemplateName();

                if (ChangedAddonFacilityPieces.BOSSES_ROOMS.contains(templateName)) {
                    Changed.LOGGER.info("Generated facility with the {} piece, at ChunkPos {}", templateName, center);
                }
            }
        }
    }
}
