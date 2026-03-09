package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.zaharenko424.cmrs.client.gui.LayoutHelper;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.*;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BestiaryGuiScreen extends Screen implements MouseMoveListener {

    static final float MaxBackGroundWidth = 425f;
    static final float MaxBackGroundHeight = 256f;

    final WidgetContainer window = new WidgetContainer().setSize(MaxBackGroundWidth, MaxBackGroundHeight);

    final RoundedRectWidget displayBackGround =
            new RoundedRectWidget().setSize(1, 1).setInsideColorFunc(a -> Color.DARK_GRAY.getRGB());

    final ChangedEntityModelWidget modelWidget =
            new ChangedEntityModelWidget().setSize(120, 200)
                    .setRenderTransform(WidgetHelper.hoverOrSelectedAnim(.1f, 0.025f, 0.025f));

    final ScrollableContainer loreScroll =
            (ScrollableContainer) new ScrollableContainer().setSize(325, 200);

    final ScrollableContainer tfs =
            (ScrollableContainer) new ScrollableContainer().setSize(100, 200).setOrigin(0, 0, 100);

    public BestiaryGuiScreen() {
        super(Component.literal("Bestiary"));

        displayBackGround.rebuildMesh();

        /* MODEL */

        modelWidget.setInteractable(true);
        modelWidget.setOrigin((width / 2f) + -30, height / 2f, 40);

        modelWidget.setOnClick((modelRectWidget, integer) -> {
            if (modelRectWidget instanceof ChangedEntityModelWidget changedEntityModelWidget) {
                ChangedEntity entity = changedEntityModelWidget.getChangedEntity();
                if (entity == null) return false;

                entity.setPose(switch (entity.getPose()) {
                    case STANDING -> Pose.CROUCHING;
                    case CROUCHING -> Pose.STANDING;
                    default -> Pose.STANDING;
                });

                return true;
            }
            return false;
        });


        Vector3f modelOrigin = modelWidget.getOrigin();
        loreScroll.setOrigin(modelOrigin.x + 50, modelOrigin.y, 10);

        /* TF LIST */
        List<TransfurVariant<?>> variants = List.of(
                ChangedAddonTransfurVariants.PROTOTYPE.get(),
                ChangedAddonTransfurVariants.EXPERIMENT_009.get(),
                ChangedAddonTransfurVariants.PROTOGEN_0SENIA0.get(),
                ChangedAddonTransfurVariants.BOREALIS_FEMALE.get(),
                ChangedAddonTransfurVariants.EXP1_MALE.get(),
                ChangedAddonTransfurVariants.EXP6.get(),
                ChangedAddonTransfurVariants.LATEX_CHEETAH_FEMALE.get(),
                ChangedAddonTransfurVariants.EXP2_MALE.get(),
                ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_FEMALE.get()
        );

        List<TFEntryWidget> entries = getTfEntryWidgets(variants);

        LayoutHelper.listLayout(tfs, entries, 5, 5);
        tfs.init();
        tfs.getScrollBar().setRoundingRadius(4).setSizeAndUpdate(8, 50);

        /* WINDOW */

        window.addWidget(displayBackGround);
        window.addWidget(modelWidget);
        window.addWidget(loreScroll);
        window.addWidget(tfs);

        window.init();

        float backGroundWidth = displayBackGround.getWidth();
        float backGroundHeight = displayBackGround.getHeight();
        for (Widget child : this.window.children()) {
            if (child == displayBackGround) {
                continue;
            } else if (child == loreScroll) {
                continue;
            }

            child.setVisible(backGroundHeight >= MaxBackGroundHeight && backGroundWidth >= MaxBackGroundWidth);
        }

        this.loreScroll.setVisible(false);
        for (Widget child : this.loreScroll.children()) {
            child.setVisible(false);
        }
    }

    private @NotNull List<TFEntryWidget> getTfEntryWidgets(List<TransfurVariant<?>> variants) {
        List<TFEntryWidget> entries = new ArrayList<>();

        for (TransfurVariant<?> tf : variants) {

            TFEntryWidget entry = new TFEntryWidget(tf);

            entry.setOnClickFunction((button, key) -> {

                if (minecraft == null || minecraft.level == null) return false;

                ChangedEntity entity = ChangedEntities.getCachedEntity(
                        minecraft.level,
                        tf.getEntityType()
                );

                if (entity instanceof IBestiaryEntityData entityData) {
                    Entity cachedEntity = ChangedEntities.getCachedEntity(minecraft.level, entityData.getReferencedEntityType());
                    if (cachedEntity instanceof ChangedEntity changedEntity) {
                        entity = changedEntity;
                    }
                }

                modelWidget.setChangedEntity(entity);

                return true;
            });

            entries.add(entry);
        }
        return entries;
    }

    @Override
    protected void init() {
        tfs.setOrigin(-window.getWidth() / 3f, 0, 100);
        window.setOrigin(width / 2f, height / 2f, 0);

        if (minecraft != null) {
            modelWidget.setChangedEntity(
                    ChangedEntities.getCachedEntity(
                            minecraft.level,
                            ChangedEntities.GAS_WOLF_MALE.get()
                    )
            );
        }

        addRenderableWidget(window);
    }

    @Override
    public void tick() {

        super.tick();

        float backGroundWidth = displayBackGround.getWidth();
        float backGroundHeight = displayBackGround.getHeight();

        if (backGroundWidth < MaxBackGroundWidth) {

            float dynamicWidth = Math.min(backGroundWidth + 50, MaxBackGroundWidth);
            displayBackGround.setSizeAndUpdate(dynamicWidth, backGroundHeight);

        } else if (backGroundHeight < MaxBackGroundHeight) {

            float dynamicHeight = Math.min(backGroundHeight + 50, MaxBackGroundHeight);
            displayBackGround.setSizeAndUpdate(backGroundWidth, dynamicHeight);
        }


        for (Widget child : this.window.children()) {
            if (child == displayBackGround) {
                continue;
            }

            child.setVisible(backGroundHeight >= MaxBackGroundHeight && backGroundWidth >= MaxBackGroundWidth);
        }

        if (modelWidget.getChangedEntity() != null) {

            List<Widget> children = new ArrayList<>(loreScroll.children());

            for (Widget child : children) {
                loreScroll.removeWidget(child);
            }

            ChangedEntity entity = modelWidget.getChangedEntity();
            List<InfoWidget> infoWidgetList = new ArrayList<>();
            InfoWidget loreWidget = null;
            InfoWidget attributeWidget = null;

            if (entity instanceof IBestiaryEntityData data) {
                for (IBestiaryEntityData.BestiaryInfo bestiaryInfo : data.getBestiaryInfo().stream().sorted(Comparator.comparingInt(IBestiaryEntityData.BestiaryInfo::order)).toList()) {
                    Component tittle = bestiaryInfo.title();
                    Component description = bestiaryInfo.description();
                    int lineCount = this.minecraft.font.split(description, 200).size();
                    int lineBreaks = description.getString().split("\n", -1).length - 1;

                    int i = lineCount + lineBreaks;

                    int heightByLines = minecraft.font.lineHeight * i;
                    InfoWidget infoWidget = new InfoWidget().setSize(200, Math.max(40, heightByLines)).setLineSize(200, 4);
                    infoWidget.setTextInfo(tittle, description);
                    infoWidgetList.add(infoWidget);
                }
            } else {
                /* LORE */
                loreWidget = new InfoWidget().setSize(200, 40).setLineSize(200, 4);
                loreWidget.setTextInfo(Component.literal("Lore").withStyle(ChatFormatting.YELLOW), Component.literal("N/A"));
                /* ATTRIBUTES */
                attributeWidget = new InfoWidget().setSize(200, 40).setLineSize(200, 4);
                attributeWidget.setTextInfo(Component.literal("Attributes").withStyle(ChatFormatting.GREEN), Component.literal("???"));

                infoWidgetList.addAll(List.of(loreWidget, attributeWidget));
            }

            List<Component> attributes = IBestiaryEntityData.getAttributePreview(entity);

            if (!attributes.isEmpty() && attributeWidget != null) {

                MutableComponent text = Component.empty();

                attributes.forEach(c -> text.append("\n").append(c));

                attributeWidget.setDescription(text);
            } // Simple Fail Safe Check;


            LayoutHelper.listLayout(loreScroll, infoWidgetList, 5, 1);
            loreScroll.init();
            RoundedRectWidget scrollBar = loreScroll.getScrollBar();
            scrollBar.setRoundingRadius(3);
            scrollBar.rebuildMesh();
            loreScroll.setActualHeight(60f * loreScroll.children().size());
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /* ------------------------------------------------ */
    /* TF ENTRY                                          */
    /* ------------------------------------------------ */

    public static class TFEntryWidget extends WidgetContainer {

        protected final TransfurVariant<?> tf;

        protected final RoundedButton button =
                new RoundedButton().setRoundingRadius(0);

        protected final RoundedTextField name =
                new RoundedTextField().setOrigin(0, 0, 1);

        public TFEntryWidget(TransfurVariant<?> tf) {

            setSize(80, 20);

            this.tf = tf;

            button.setSize(getWidth(), getHeight());
            button.rebuildMesh();

            name.setSize(70, 12);
            name.setDefText(Component.translatable(tf.getEntityType().getDescriptionId()));
            name.setClickThrough(true);

            addWidget(button);
            addWidget(name);

            init();
        }

        public void setOnClickFunction(@Nullable ToBooleanBiFunction<RoundedButton, Integer> onClick) {
            button.setOnClick(onClick);
        }
    }
}