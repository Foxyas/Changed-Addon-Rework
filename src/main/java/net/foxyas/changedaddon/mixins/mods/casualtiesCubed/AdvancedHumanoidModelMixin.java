package net.foxyas.changedaddon.mixins.mods.casualtiesCubed;

import net.foxyas.changedaddon.compatibility.casualities_cubed.LimbMapper;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.client.animations.ModelPartIdentifier;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.EntityShape;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = AdvancedHumanoidModel.class, remap = false)
@RequiredMods("casualties_cubed")
public abstract class AdvancedHumanoidModelMixin<T extends ChangedEntity> extends PlayerModel<T> {

    @Unique
    private static final List<Limb> LIMBS_TO_SEARCH = List.of(
            Limb.UPPER_LEFT_ARM,
            Limb.UPPER_RIGHT_ARM,
            Limb.UPPER_LEFT_LEG,
            Limb.UPPER_RIGHT_LEG,
            Limb.LOWER_LEFT_LEG,
            Limb.LOWER_RIGHT_LEG,
            Limb.LEFT_FOOT,
            Limb.RIGHT_FOOT,
            Limb.ABDOMEN,
            Limb.HEAD
    );

    @Shadow
    public abstract @NotNull ModelPart getArm(@NotNull HumanoidArm humanoidArm);

    @Shadow
    public abstract ModelPart getLeg(HumanoidArm humanoidArm);

    @Shadow
    public abstract HumanoidAnimator<T, ?> getAnimator(T t);

    public AdvancedHumanoidModelMixin(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
    }

    @Inject(method = "setupAnim(Lnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFF)V", at = @At("TAIL"))
    private void prototypeAmputationSetupAnimHook(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        AdvancedHumanoidModel<?> self = (AdvancedHumanoidModel<?>) (Object) this;
        if (self instanceof LatexHumanoidArmorModel<?, ?>) return;

        Player player = entity.getUnderlyingPlayer();
        if (player instanceof AbstractClientPlayer clientPlayer) {
            if (entity.getEntityShape() == EntityShape.FERAL) {
                return;
            }
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant == null || transfurVariant.isTemporaryFromSuit()) return;

            Minecraft minecraft = Minecraft.getInstance();
            EntityRenderer<?> renderer =
                    minecraft.getEntityRenderDispatcher().getRenderer(clientPlayer);

            if (renderer instanceof PlayerRenderer playerRenderer) {
                // PlayerModel<AbstractClientPlayer> playerModel = playerRenderer.getModel();

                player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                    for (Limb limb : LIMBS_TO_SEARCH) {
                        boolean isVisible = data.getLimb(limb).isAmputated();
                        @Nullable ModelPartIdentifier changedLimb = LimbMapper.getChangedLimb(limb);
                        if (changedLimb == null) continue;
                        ModelPart modelPart = changedLimb.getModelPart(self, entity);
                        if (modelPart != null) {
                            if (changedLimb.limb() == net.ltxprogrammer.changed.client.animations.Limb.HEAD) {
                                modelPart.visible = !isVisible;
                                if (minecraft.player == player) {
                                    if (minecraft.player.isSleeping()) {
                                        if (modelPart.visible && minecraft.options.getCameraType().isFirstPerson()) {
                                            modelPart.visible = false;
                                        }
                                    }
                                }
                            } else {
                                modelPart.visible = !isVisible;
                            }
                        }
                    }
                });

            }

        }
    }
}
