package net.zaharenko424.cmrs;

import com.mojang.blaze3d.platform.InputConstants;
import net.foxyas.changedaddon.client.gui.BestiaryGuiScreen;
import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.process.DEBUG;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.cmrs.client.gui.LayoutHelper;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT) //TODO turn this on when tweaking.
public class Test {

    @SubscribeEvent
    public static void a(InputEvent.Key event) {
        if (event.getKey() != InputConstants.KEY_I && event.getKey() != InputConstants.KEY_O) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) return;

        if (event.getKey() == InputConstants.KEY_I) {
            minecraft.setScreen(new TestScreen());
        } else {
            minecraft.setScreen(new BestiaryGuiScreen());
        }
    }

    static class TestScreen extends Screen implements MouseMoveListener {

        static final float MaxBackGroundWidth = 425f;
        static final float MaxBackGroundHeight = 256f;
        final WidgetContainer window = new WidgetContainer().setSize(MaxBackGroundWidth, MaxBackGroundHeight);
        final ScrollableContainer info = (ScrollableContainer) new ScrollableContainer().setSize(425, 200);
        final RoundedRectWidget displayBackGround = new RoundedRectWidget().setSize(1, 1).setInsideColorFunc(a -> Color.DARK_GRAY.getRGB());
        final RoundedButton button = new RoundedButton().setRoundingRadius(5).setSize(50, 25).setText(Component.literal("Text").withStyle(ChatFormatting.AQUA))
                .setOrigin(0, 0, 50).setRenderTransform(WidgetHelper.hoverAnim(.1f, 0.025f, 0.025f));
        final ChangedEntityModelWidget modelWidget = new ChangedEntityModelWidget().setSize(100, 200).setRenderTransform(WidgetHelper.hoverOrSelectedAnim(.1f, 0.025f, 0.025f));
        final InfoWidget infoWidget = new InfoWidget().setSize(200, 100).setLineSize(200, 4);
        final InfoWidget info2Widget = new InfoWidget().setSize(200, 100).setLineSize(200, 4);
//        final ImageWidget screenBackGroundWidget = new ImageWidget().setOrigin(0, 0, 0)
//                .setTex(ResourceLocation.parse("changed_addon:textures/screens/generatorgui.png"),
//                0, 0, 200, 99, 200, 99).setSize(425, 256);

        protected TestScreen() {
            super(Component.literal("A"));

            //screenBackGroundWidget.setInteractable(false);

            modelWidget.setInteractable(true);
            modelWidget.setOrigin(-100 + (width / 2f), height / 2f, 40);
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

            infoWidget.setTextInfo(Component.literal("Info/Lore"), Component.literal("N/A"));
            infoWidget.setLineColor(Color.YELLOW);
            infoWidget.setOrigin(modelWidget.getOrigin().x + 50, modelWidget.getOrigin().y - DEBUG.HeadPosY, modelWidget.getOrigin().z + 10);

            info2Widget.setTextInfo(Component.literal("Attributes"), Component.literal("???"));
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
            info.getScrollBar().setRoundingRadius(3);
            info.getScrollBar().rebuildMesh();

            //window.addWidget(screenBackGroundWidget);
            window.addWidget(info);
            window.addWidget(tfs);
            window.init();

            List<TransfurVariant<?>> l = List.of(
                    ChangedAddonTransfurVariants.PROTOTYPE.get(), ChangedAddonTransfurVariants.EXPERIMENT_009.get(), ChangedAddonTransfurVariants.PROTOGEN_0SENIA0.get(),
                    ChangedAddonTransfurVariants.BOREALIS_FEMALE.get(), ChangedAddonTransfurVariants.EXP1_MALE.get(), ChangedAddonTransfurVariants.EXP6.get(),
                    ChangedAddonTransfurVariants.LATEX_CHEETAH_FEMALE.get(), ChangedAddonTransfurVariants.EXP2_MALE.get(), ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_FEMALE.get()
            );

            List<TFEntryWidget> entries = new ArrayList<>();
            for (TransfurVariant<?> tf : l) {
                entries.add(new TFEntryWidget(tf));
            }

            LayoutHelper.listLayout(tfs, entries, 5, 5);
            tfs.init();
            tfs.getScrollBar().setRoundingRadius(4).setSizeAndUpdate(8, 50);

            float backGroundWidth = this.displayBackGround.getWidth();
            float backGroundHeight = this.displayBackGround.getHeight();

            for (Widget child : this.window.children()) {
                if (child == displayBackGround) {
                    continue;
                }

                child.setVisible(backGroundHeight >= MaxBackGroundHeight && backGroundWidth >= MaxBackGroundWidth);
            }
        }

        final ScrollableContainer tfs = (ScrollableContainer) new ScrollableContainer().setSize(100, 200).setOrigin(0, 0, 100);


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

            for (Widget child : this.window.children()) {
                if (child == displayBackGround) {
                    continue;
                }

                child.setVisible(backGroundHeight >= MaxBackGroundHeight && backGroundWidth >= MaxBackGroundWidth);
            }

            if (modelWidget.getChangedEntity() != null) {

                if (modelWidget.getChangedEntity() instanceof IBestiaryEntityData iBestiaryEntityData) {
                    infoWidget.setDescription(iBestiaryEntityData.getLore());
                }

                List<Component> attributePreview = IBestiaryEntityData.getAttributePreview(modelWidget.getChangedEntity());
                if (!attributePreview.isEmpty()) {
                    MutableComponent mutableComponent = Component.empty();
                    attributePreview.forEach((component) -> {
                        mutableComponent.append("\n").append(component);
                        info.addHeight(40);
                    });
                    info2Widget.setDescription(mutableComponent);
                }
            }

            info.setActualHeight(60f * info.children().size());
        }

        @Override
        protected void init() {
            tfs.setOrigin(-window.getWidth() / 3f, 0, 100);
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

    static class TFEntryWidget extends WidgetContainer {

        final TransfurVariant<?> tf;
        final RoundedButton button = new RoundedButton().setRoundingRadius(0);
        final RoundedTextField name = new RoundedTextField().setOrigin(0, 0, 1);

        TFEntryWidget(TransfurVariant<?> tf) {
            setSize(80, 20);

            this.tf = tf;

            button.setSize(getWidth(), getHeight());
            button.setOnClick((a, b) -> {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable(tf.getEntityType().getDescriptionId()), true);
                return true;
            });
            button.rebuildMesh();

            name.setSize(70, 12);
            name.setDefText(Component.translatable(tf.getEntityType().getDescriptionId()));
            name.setClickThrough(true);

            addWidget(button);
            addWidget(name);
            init();
        }
    }
}
