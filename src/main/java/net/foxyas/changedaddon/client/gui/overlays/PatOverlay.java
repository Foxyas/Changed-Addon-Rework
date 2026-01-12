package net.foxyas.changedaddon.client.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.init.ChangedAddonKeyMappings;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.ComponentUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;

import java.awt.*;

import static net.foxyas.changedaddon.process.features.PatFeatureHandle.canPlayerPat;
import static net.minecraft.client.gui.GuiComponent.drawCenteredString;

@OnlyIn(Dist.CLIENT)
public class PatOverlay {

    public static final ResourceLocation TEXTURE = ResourceLocation.parse("changed_addon:textures/screens/paw_normal.png");

    private static boolean isEntityInPassiveStage(Entity lookedEntity) {
        if (lookedEntity instanceof ChangedEntity changedEntity) {
            return changedEntity.getTarget() == null;
        } else if (lookedEntity instanceof Mob mob) {
            return mob.getTarget() == null;
        } else return lookedEntity instanceof Player;
    }


    private static boolean isPatableEntity(Player player, Entity patEntity) {
        // Verifica se a entidade está dentro das tags definidas como 'patable entities'
        boolean isPatableByTag = patEntity.getType().is(ChangedAddonTags.EntityTypes.PATABLE);

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

    public static boolean isKeySet() {
        String key = ChangedAddonKeyMappings.PAT_KEY.getTranslatedKeyMessage().getString();
        return !key.isEmpty();
    }

    private static Component getPatInfo(Entity lookedEntity) {
        String key = ChangedAddonKeyMappings.PAT_KEY.getTranslatedKeyMessage().getString();
        MutableComponent patMessage = ComponentUtil.translatable("changed_addon.info.is_patable", key.isEmpty() ? "Key not set" : key, lookedEntity.getDisplayName().getString());
        patMessage.withStyle(style ->
                style.withColor(-1)
                        //.withBold(true)
                        .withItalic(true));
        return patMessage;
    }

    private static String getSimplePatInfo() {
        return ChangedAddonKeyMappings.PAT_KEY.getTranslatedKeyMessage().getString();
    }

    private static Component PatInfo2(Entity lookedEntity) {
        if (lookedEntity instanceof LivingEntity) {
            MutableComponent patMessage;

            if (lookedEntity.hasCustomName()) {
                patMessage = new TextComponent(lookedEntity.getCustomName().getString());
            } else {
                patMessage = new TextComponent(lookedEntity.getDisplayName().getString());
            }
            patMessage.withStyle(style ->
                    style.withColor(Color3.getColor("#FFFFFF").toInt())
                            .withBold(true)
                            .withItalic(true));
            return patMessage;
        } else {
            return new TextComponent("");
        }
    }

    public static void renderPatIconOverlay(ForgeIngameGui forgeIngameGui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (!ChangedAddonClientConfiguration.PAT_OVERLAY.get()) return;

        double posX = ChangedAddonClientConfiguration.PAT_OVERLAY_X.get();
        double posY = screenHeight - ChangedAddonClientConfiguration.PAT_OVERLAY_Y.get();

        float floatPosX = (float) posX;
        float floatPosY = (float) posY;

        Player player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;

        if (!player.getMainHandItem().isEmpty() && !player.getOffhandItem().isEmpty()) return;

        LivingEntity lookedEntity = PlayerUtil.getEntityLookingAt(player, (float) player.getReachDistance(), PlayerUtil.BLOCK_COLLISION, LivingEntity.class);
        if (lookedEntity == null || !isPatableEntity(player, lookedEntity) || !isEntityInPassiveStage(lookedEntity) || !isKeySet())
            return;

        if (lookedEntity.isInvisible() || !canPlayerPat(player)) return;

        if (!ChangedAddonClientConfiguration.PAW_STYLE_PAT_OVERLAY.get()) {
            drawCenteredString(poseStack, Minecraft.getInstance().font, getPatInfo(lookedEntity), (int) floatPosX, (int) floatPosY, -1);
            return;
        }

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        Minecraft mc = Minecraft.getInstance();

        int x = ((int) posX); // Posição X na tela
        int y = ((int) posY); // Posição Y na tela
        int largura = 19; // Largura da imagem
        int altura = 19; // Altura da imagem
        float troubleShotXValue = floatPosX + 9;
        float troubleShotYValue = floatPosY + 20;

        if (instance == null) {
            // Renderiza a imagem na tela
            RenderSystem.setShaderTexture(0, TEXTURE);
            GuiComponent.blit(poseStack, x, y, 0, 0, largura, altura, largura, altura);
            drawCenteredString(poseStack, mc.font,
                    getSimplePatInfo(), (int) troubleShotXValue, (int) troubleShotYValue, Color3.getColor("#ffabab").toInt());
            return;
        }

        if (instance.getChangedEntity() instanceof IDynamicPawColor iDynamicPawColor) {
            Color pawColor = iDynamicPawColor.getPawBeansColor();
            RenderSystem.setShaderColor(pawColor.getRed() / 255f, pawColor.getGreen() / 255f, pawColor.getBlue() / 255f, pawColor.getAlpha() / 255f);
            // Renderiza a imagem na tela
            RenderSystem.setShaderTexture(0, TEXTURE);
            GuiComponent.blit(poseStack, x, y, 0, 0, largura, altura, largura, altura);
            drawCenteredString(poseStack, mc.font,
                    getSimplePatInfo(), (int) troubleShotXValue, (int) troubleShotYValue, pawColor.getRGB());
        } else {
            // Renderiza a imagem na tela
            RenderSystem.setShaderTexture(0, TEXTURE);
            GuiComponent.blit(poseStack, x, y, 0, 0, largura, altura, largura, altura);
            drawCenteredString(poseStack, mc.font,
                    getSimplePatInfo(), (int) troubleShotXValue, (int) troubleShotYValue, Color3.getColor("#ffabab").toInt());
        }
    }
}
