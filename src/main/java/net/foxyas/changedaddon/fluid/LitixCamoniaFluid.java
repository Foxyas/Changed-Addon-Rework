package net.foxyas.changedaddon.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Consumer;

public abstract class LitixCamoniaFluid extends ForgeFlowingFluid {

    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ChangedAddonFluids.LITIX_CAMONIA_FLUID_TYPE,
            ChangedAddonFluids.LITIX_CAMONIA_FLUID,
            ChangedAddonFluids.FLOWING_LITIX_CAMONIA_FLUID)
            .explosionResistance(100f)
            .slopeFindDistance(2)
            .bucket(ChangedAddonItems.LITIX_CAMONIA_FLUID_BUCKET)
            .block(ChangedAddonBlocks.LITIX_CAMONIA_FLUID);

    private LitixCamoniaFluid() {
        super(PROPERTIES);
    }

    @Override
    public void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull FluidState pState) {
        super.tick(pLevel, pPos, pState);
        if (pLevel.isClientSide()) return;
        for (Direction value : Direction.values()) {
            BlockPos relative = pPos.relative(value);
            LatexCoverState coverState = LatexCoverState.getAt(pLevel, relative);
            if (!coverState.isAir()) {
                LatexCoverState.setAtAndUpdate(pLevel, relative, ChangedLatexTypes.NONE.get().defaultCoverState());
            }
        }
    }

    public static class FluidType extends net.minecraftforge.fluids.FluidType {

        public FluidType() {
            super(Properties.create()
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                    .canExtinguish(true)
                    .supportsBoating(true)
                    .canDrown(true)
                    .canSwim(true)
                    .fallDistanceModifier(0)
                    .descriptionId("fluid." + ChangedAddonMod.MODID + "." + ChangedAddonFluids.LITIX_CAMONIA_FLUID.getId().getPath())
            );
        }

        @Override
        public void setItemMovement(ItemEntity entity) {
            super.setItemMovement(entity);
        }

        @Override
        public @Nullable BlockPathTypes getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
            if (mob instanceof ChangedEntity changedEntity) {
                if (changedEntity.getType().is(ChangedTags.EntityTypes.LATEX)) {
                    return BlockPathTypes.DAMAGE_CAUTIOUS;
                }
            }

            return super.getBlockPathType(state, level, pos, mob, canFluidLog);
        }

        @Override
        public @Nullable BlockPathTypes getAdjacentBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
            return super.getAdjacentBlockPathType(state, level, pos, mob, originalType);
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                private static final ResourceLocation FLUID_STILL = ChangedAddonMod.resourceLoc("block/litix_camonia_fluid/litix_camonia_fluid_still");
                private static final ResourceLocation FLUID_FLOWING = ChangedAddonMod.resourceLoc("block/litix_camonia_fluid/litix_camonia_fluid_flowing");

                public ResourceLocation getStillTexture() {
                    return FLUID_STILL;
                }

                public ResourceLocation getFlowingTexture() {
                    return FLUID_FLOWING;
                }

                @Override
                public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    return new Vector3f(1, 1, 1);
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                    IClientFluidTypeExtensions.super.modifyFogRender(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);
                }
            });
        }

        @Mod.EventBusSubscriber
        public static class FogHandle {
            @OnlyIn(Dist.CLIENT)
            @SubscribeEvent
            public void onRenderFog(ViewportEvent.RenderFog event) {
                if (!(event.getCamera().getBlockAtCamera().getFluidState().getType() instanceof LitixCamoniaFluid))
                    return;

                event.setNearPlaneDistance(0.25F);
                event.setFarPlaneDistance(1.0F);
                event.setCanceled(true);
            }
        }
    }

    public static class Source extends LitixCamoniaFluid {

        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    public static class Flowing extends LitixCamoniaFluid {

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }
}
