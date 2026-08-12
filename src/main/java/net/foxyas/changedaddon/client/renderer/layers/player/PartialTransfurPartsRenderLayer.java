package net.foxyas.changedaddon.client.renderer.layers.player;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.LatexInfection;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class PartialTransfurPartsRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    // Usamos WeakHashMap para a chave (Player) ser removida da memória automaticamente quando o jogador deslogar/descarregar.
    private static final Map<Player, ChangedEntity> ENTITY_CACHE = new WeakHashMap<>();

    public PartialTransfurPartsRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @ApiStatus.Internal
    public static ChangedEntity getOrCreateDisplayEntity(Player player, TransfurVariant<?> variant) {
        if (variant == null || Minecraft.getInstance().level == null) {
            return null;
        }

        return ENTITY_CACHE.compute(player, (p, existingEntity) -> {
            // Se a entidade no cache não existir ou a sua variante mudou, recria a entidade
            if (existingEntity == null || existingEntity.getSelfVariant() != variant) {
                ChangedEntity newEntity = variant.getEntityType().create(Minecraft.getInstance().level);
                if (newEntity == null) return null;

                newEntity.setNoAi(true);
                return newEntity;
            }
            return existingEntity;
        });
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBuffer,
                       int pPackedLight,
                       @NotNull T pLivingEntity,
                       float pLimbSwing,
                       float pLimbSwingAmount,
                       float pPartialTick,
                       float pAgeInTicks,
                       float pNetHeadYaw,
                       float pHeadPitch) {

        // Apenas processa se for um Jogador
        if (!(pLivingEntity instanceof Player player)) {
            return;
        }

        // 1. Obter a infecção ativa / variante
        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(player);
        LatexInfection latexInfection = playerVariables.latexInfection;
        if (!latexInfection.isActive()) {
            return;
        }

        TransfurVariant<?> variant = latexInfection.getInfectionVariant();
        if (variant == null) {
            return;
        }

        // 2. Obter ou criar a entidade associada no Cache vinculada ao Jogador
        ChangedEntity displayEntity = getOrCreateDisplayEntity(player, variant);
        if (displayEntity == null) {
            return;
        }

        // 3. Sincronizar dados do jogador para a entidade do cache
        syncEntityWithPlayer(displayEntity, player, pAgeInTicks, pPartialTick);

        displayEntity.variantTick(pLivingEntity.level());

        // 4. Validar se o Renderer é um AdvancedHumanoidRenderer
        if (!(Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(displayEntity) instanceof AdvancedHumanoidRenderer<?, ?> rawRenderer)) {
            return;
        }

        @SuppressWarnings("unchecked")
        AdvancedHumanoidRenderer<ChangedEntity, ?> renderer = (AdvancedHumanoidRenderer<ChangedEntity, ?>) rawRenderer;
        AdvancedHumanoidModel<ChangedEntity> model = renderer.getModel(displayEntity);

        // 5. Renderizar partes da entidade
        pPoseStack.pushPose();

        model.prepareMobModel(displayEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
        model.setupAnim(displayEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        ModelPart torso = model.getTorso();
        ModelPart tail = null;
        try {
            if (torso.hasChild("Tail")) {
                tail = torso.getChild("Tail");
            } else if (torso.hasChild("tail")) {
                tail = torso.getChild("tail");
            }
        } catch (Exception ignored) {}

        if (tail != null) {
            int overlay = LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F);
            tail.render(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(renderer.getTextureLocation(displayEntity))), pPackedLight, overlay);
        }

        //renderer.render(displayEntity, pLivingEntity.getYRot(), pPartialTick, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.popPose();
    }

    private void syncEntityWithPlayer(ChangedEntity entity, Player player, float ageInTicks, float partialTick) {
        // Vincula o jogador base na entidade do Changed
        entity.setUnderlyingPlayer(player);

        // Copia transformações e rotações
        entity.tickCount = player.tickCount;
        entity.setPos(player.getX(), player.getY(), player.getZ());
        entity.xo = player.xo;
        entity.yo = player.yo;
        entity.zo = player.zo;

        entity.yBodyRot = player.yBodyRot;
        entity.yBodyRotO = player.yBodyRotO;
        entity.yHeadRot = player.yHeadRot;
        entity.yHeadRotO = player.yHeadRotO;

        entity.setXRot(player.getXRot());
        entity.xRotO = player.xRotO;

        entity.swinging = player.swinging;
        entity.swingTime = player.swingTime;
        entity.oAttackAnim = player.oAttackAnim;
        entity.attackAnim = player.getAttackAnim(partialTick);

        entity.setPose(player.getPose());
        entity.setSprinting(player.isSprinting());
        entity.setShiftKeyDown(player.isShiftKeyDown());
        entity.setSwimming(player.isSwimming());
    }
}