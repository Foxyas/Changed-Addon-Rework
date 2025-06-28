package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.process.util.FoxyasUtils;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.changedaddon.init.ChangedAddonModKeyMappings.PAT_KEY;
import static net.foxyas.changedaddon.procedures.PatFeatureHandleProcedure.isPossibleToPat;
import static net.ltxprogrammer.changed.client.gui.GrabOverlay.blit;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class PatOverlay {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse("changed_additions:textures/screens/paw_normal.png");

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiOverlayEvent.Pre event) {

        if (!ChangedAddonClientConfigsConfiguration.PAT_OVERLAY.get()) {
            return;
        }

        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        double posX = ChangedAddonClientConfigsConfiguration.PAT_OVERLAY_X.get();
        double posY = h - ChangedAddonClientConfigsConfiguration.PAT_OVERLAY_Y.get();

        float floatPosX = (float) posX;
        float floatPosY = (float) posY;

        Player entity = Minecraft.getInstance().player;

        if (entity != null && !entity.isSpectator()) {
            if (entity.getMainHandItem().isEmpty() || entity.getOffhandItem().isEmpty()) {
                Entity lookedEntity = PlayerUtilProcedure.getEntityLookingAt(entity, 3);
                if (lookedEntity != null && isPatableEntity(entity, lookedEntity) && isEntityInPassiveStage(lookedEntity) && isKeySet()) {
                    if (!getPatInfo(entity).getString().isEmpty()) {
                        if (!lookedEntity.isInvisible() && isPossibleToPat(entity)) {
                            if (!ChangedAddonClientConfigsConfiguration.PAW_STYLE_PAT_OVERLAY.get()) {
                                FoxyasUtils.drawCenteredString(event.getGuiGraphics(), Minecraft.getInstance().font,
                                        getPatInfo(lookedEntity), (int) floatPosX, (int) floatPosY, -1);
                            } else {
                                Minecraft mc = Minecraft.getInstance();
                                GuiGraphics poseStack = event.getGuiGraphics();
                                RenderSystem.setShader(GameRenderer::getPositionTexShader);

                                int x = ((int) posX); // Posição X na tela
                                int y = ((int) posY); // Posição Y na tela
                                int largura = (int) 19; // Largura da imagem
                                int altura = (int) 19; // Altura da imagem
                                float troubleShotXValue = floatPosX + 9;
                                float troubleShotYValue = floatPosY + 20;

                                // Renderiza a imagem na tela
                                blit(poseStack, TEXTURE, x, y, 0, 0, largura, altura, largura, altura);
                                FoxyasUtils.drawCenteredString(poseStack, mc.font,
                                        getSimplePatInfo(), (int) troubleShotXValue, (int) troubleShotYValue, Color3.getColor("#ffabab").toInt());
                            }
                        }
                    }
                }
            }
        }
    }



    private static boolean isEntityInPassiveStage(Entity lookedEntity) {
        if (lookedEntity instanceof ChangedEntity changedEntity) {
            return changedEntity.getTarget() == null;
        } else if (lookedEntity instanceof Mob mob) {
            return mob.getTarget() == null;
        } else return lookedEntity instanceof Player;
    }


    private static boolean isPatableEntity(Player player, Entity patEntity) {
        // Verifica se a entidade está dentro das tags definidas como 'patable entities'
        boolean isPatableByTag = patEntity.getType().is(TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.parse("changed_addon:patable_entitys")));

        // Verifica se a visão do jogador está limpa até a entidade
        if (!isLineOfSightClear(player, patEntity)) {
            return false;
        }

        if (patEntity instanceof Player patPlayer) {
            boolean isPatPlayerTransfur = ProcessTransfur.getPlayerTransfurVariant(patPlayer) != null;
            boolean isPlayerTransfur = ProcessTransfur.getPlayerTransfurVariant(player) != null;
            if (isPatPlayerTransfur) {
                return true;
            } else if (isPlayerTransfur) {
                return true;
            }
        } else if (patEntity instanceof ChangedEntity) {
            return true;
        }
        return isPatableByTag;
    }


    private static boolean isLineOfSightClear(Player player, Entity entity) {
        var level = player.level();
        var playerEyePos = player.getEyePosition(1.0F); // Posição dos olhos do jogador
        var entityEyePos = entity.getBoundingBox().getCenter(); // Centro da entidade

        // Realiza o traçado de linha
        var result = level.clip(new ClipContext(
                playerEyePos,
                entityEyePos,
                ClipContext.Block.VISUAL, // Apenas blocos visuais são considerados
                ClipContext.Fluid.NONE, // Ignorar fluidos
                player
        ));

        // Retorna true se o resultado for MISS (nenhum bloco obstruindo)
        return result.getType() == HitResult.Type.MISS;
    }


    public static boolean isKeySet() {
        String key = PAT_KEY.getTranslatedKeyMessage().getString();
        return !key.isEmpty();
    }


    private static Component getPatInfo(Entity lookedEntity) {
        String key = PAT_KEY.getTranslatedKeyMessage().getString();
        if (lookedEntity instanceof LivingEntity) {
            //.withBold(true)
            return Component.translatable("changed_additions.info.is_patable", key.isEmpty() ? "Not Key Set" : key, lookedEntity.getDisplayName().getString()).withStyle(style ->
                    style.withColor(Color3.getColor("#FFFFFF").toInt())
                            //.withBold(true)
                            .withItalic(true));
        } else {
            return Component.translatable("block.minecraft.air");
        }
    }

    private static Component getSimplePatInfo() {
        return PAT_KEY.getTranslatedKeyMessage();
    }

    private static Component PatInfo2(Entity lookedEntity) {
        if (lookedEntity instanceof LivingEntity) {
            Component patMessage;
            if (lookedEntity.hasCustomName()) {
                patMessage = Component.literal(lookedEntity.getCustomName().getString()).withStyle(style ->
                        style.withColor(Color3.getColor("#FFFFFF").toInt())
                                .withBold(true)
                                .withItalic(true));
            } else {
                patMessage = Component.literal(lookedEntity.getDisplayName().getString()).withStyle(style ->
                        style.withColor(Color3.getColor("#FFFFFF").toInt())
                                .withBold(true)
                                .withItalic(true));
            }
            return patMessage;
        } else {
            return Component.literal("");
        }
    }
}
