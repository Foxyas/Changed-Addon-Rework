package net.zaharenko424.cmrs;

import com.mojang.blaze3d.platform.InputConstants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.*;

import java.awt.*;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Test {

    @SubscribeEvent
    public static void a(InputEvent.Key event) {
        if (event.getKey() != InputConstants.KEY_I) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) return;

        minecraft.setScreen(new TestScreen());
    }

    static class TestScreen extends Screen implements MouseMoveListener {

        final RoundedButton TEXT = new RoundedButton().setRoundingRadius(5).setSize(50, 25).setText(Component.literal("Text").withStyle(ChatFormatting.AQUA)).setRenderTransform(WidgetHelper.hoverAnim(.1f, 0.025f, 0.025f));
        final WidgetContainer window = new WidgetContainer().setSize(200, 100);
        final ScrollableContainer info = (ScrollableContainer) new ScrollableContainer().setSize(300, 100);
        final RoundedRectWidget displayBackGround = new RoundedRectWidget().setSize(1, 1).setInsideColorFunc(a -> Color.DARK_GRAY.getRGB());
        final RoundedButton button = TEXT
                .setOrigin(0, 0, 50).setRenderTransform(WidgetHelper.hoverAnim(.1f, 0.025f, 0.025f));
        final ChangedEntityModelWidget modelWidget = new ChangedEntityModelWidget().setSize(100, 200).setRenderTransform(WidgetHelper.hoverOrSelectedAnim(.1f, 0.025f, 0.025f));
        final InfoWidget infoWidget = new InfoWidget().setSize(200, 50).setLineSize(200, 4);
        final InfoWidget info2Widget = new InfoWidget().setSize(200, 50).setLineSize(200, 4);
//        final ImageWidget screenBackGroundWidget = new ImageWidget().setOrigin(0, 0, 0)
//                .setTex(ResourceLocation.parse("changed_addon:textures/screens/generatorgui.png"),
//                0, 0, 200, 99, 200, 99).setSize(425, 256);

        protected TestScreen() {
            super(Component.literal("A"));

            //screenBackGroundWidget.setInteractable(false);

            info.setActualHeight(150);

            modelWidget.setInteractable(true);
            modelWidget.setOrigin(-70 + (width / 2f), height / 2f, 40);
            modelWidget.setOnClick((modelRectWidget, integer) -> {
                if (modelRectWidget instanceof ChangedEntityModelWidget changedEntityModelWidget) {
                    ChangedEntity changedEntity = changedEntityModelWidget.getChangedEntity();
                    if (changedEntity == null) return false;

                    changedEntity.setPose(switch (changedEntity.getPose()) {
                        case STANDING -> Pose.CROUCHING;
                        case CROUCHING -> Pose.STANDING;
                        default -> Pose.STANDING;
                    });
                    return true;
                }

                return false;
            });

            displayBackGround.rebuildMesh();

            button.setOrigin(modelWidget.getOrigin().x, modelWidget.getOrigin().y + 70, modelWidget.getOrigin().z + 5);
            List<String> list = List.of("Hi", "Hello", "Hai", "hi");
            button.setOnClick((b, key) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return false;

                player.displayClientMessage(Component.literal(list.get(player.getRandom().nextInt(4))), true);
                return true;
            });
            button.rebuildMesh();

            infoWidget.setTextInfo(Component.literal("Some Cool Title"), Component.literal("Some Cool Description"));
            infoWidget.setLineColor(Color.GREEN);
            infoWidget.setOrigin(modelWidget.getOrigin().x + 50, modelWidget.getOrigin().y, modelWidget.getOrigin().z + 10);

            info2Widget.setTextInfo(Component.literal("Some Cool Title2"), Component.literal("Some Cool Description2"));
            info2Widget.setLineColor(Color.GREEN);
            info2Widget.setOrigin(infoWidget.getOrigin().x, infoWidget.getOrigin().y + 40, infoWidget.getOrigin().z);

            window.addWidget(displayBackGround);
            window.addWidget(button);
            window.addWidget(modelWidget);
            info.addWidget(infoWidget);
            info.addWidget(info2Widget);
            info.setOrigin(width / 2f, height / 2f, 10);
            info.setInteractable(false);
            info.setClickThrough(true);
            info.init();
            //window.addWidget(screenBackGroundWidget);
            window.addWidget(info);


            TEXT.setOrigin(-100 + (width / 2f), height / 2f, 100);
            window.addWidget(TEXT);
            window.init();
        }

        static final float MaxBackGroundWidth = 425f;
        static final float MaxBackGroundHeight = 256f;

        @Override
        public void tick() {
            super.tick();
            float backGroundWidth = this.displayBackGround.getWidth();
            float backGroundHeight = this.displayBackGround.getHeight();
            if (backGroundWidth < MaxBackGroundWidth) {
                float dynamicWidth = Math.min(backGroundWidth + 50, MaxBackGroundWidth);
                this.displayBackGround.setSizeAndUpdate(dynamicWidth, backGroundHeight);
            } else if (backGroundHeight < MaxBackGroundHeight) {
                float dynamicHeight = Math.min(backGroundHeight + 50, MaxBackGroundHeight);
                this.displayBackGround.setSizeAndUpdate(backGroundWidth, dynamicHeight);
            }
        }

        @Override
        protected void init() {
            window.setOrigin(width / 2f, height / 2f, 0);

            if (this.minecraft != null) {
                modelWidget.setChangedEntity(ChangedEntities.getCachedEntity(this.minecraft.level, ChangedEntities.GAS_WOLF_MALE.get()));
            }
            addRenderableWidget(window);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
