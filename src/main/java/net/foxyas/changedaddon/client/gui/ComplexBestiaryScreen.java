package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.foxyas.changedaddon.util.GuiUtils;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ComplexBestiaryScreen extends AbstractBestiaryScreen {
    private static final int BASE_W = 420;
    private static final int BASE_H = 230;

    // =============================================================
    // DISPLAY MODES
    // =============================================================
    public enum DisplayMode {
        NORMAL,       // Split view: 3D Model on left, Details on right
        HIDE_MODEL,   // Lore / Details takes full width (Model hidden)
        MODEL_ONLY    // 3D Model viewport takes full width (Details hidden)
    }

    private DisplayMode displayMode = DisplayMode.NORMAL;

    // =============================================================
    // ENTITY GRID ICON CUSTOMIZATION
    // =============================================================
    private static final int ICON_Y_OFFSET = 1;
    private static final float ICON_YAW = -22.5f;
    private static final float ICON_PITCH = 0.0f;
    // =============================================================

    private int dialogW;
    private int dialogH;
    private int left;
    private int top;

    private int activeTab = 0;

    // Entity Selector & Filtering
    private final List<TransfurVariant<?>> allVariants = new ArrayList<>();
    private final List<TransfurVariant<?>> filteredVariants = new ArrayList<>();
    private EditBox searchBox;
    private static final int GRID_COLS = 5;
    private int gridRowScroll = 0;

    // Scrollbar Drag States
    private boolean isDraggingListScroll = false;
    private double listScrollGrabOffset = 0.0;
    private boolean isDraggingDetailsScroll = false;
    private double detailsScrollGrabOffset = 0.0;

    // Selected Variant & Cached Entity
    protected TransfurVariant<?> selected;
    private ChangedEntity currentEntity;
    private boolean isUnlocked = true;

    // Viewport (3D Model)
    private float modelYaw = 25.0f;
    private float modelPitch = 0.0f;
    private float modelZoom = 52.0f;
    private float modelOffsetX = 0.0f;
    private float modelOffsetY = 0.0f;
    private boolean isDraggingModel = false;
    private boolean isDraggingModelOffset = false;

    // Details Bar Chart & Lore Data
    private int detailsScrollOffset = 0;
    private int maxDetailsScroll = 0;
    private final List<AttributeBarItem> attributeBars = new ArrayList<>();
    private final List<AttributeBarItem> playerAttributeBars = new ArrayList<>();
    private final List<FormattedCharSequence> loreLines = new ArrayList<>();
    private String classificationText = "";

    // Radar Chart Interactive State
    private float chartScaleFactor = 1.0f;
    private boolean showPlayerChart = false;
    private int lastChartCenterX = -1;
    private int lastChartCenterY = -1;
    private int lastChartRadius = 32;

    // Interactive Button Bounds
    private int lastPlayerBtnX = -1;
    private int lastPlayerBtnY = -1;
    private int lastPlayerBtnW = 92;
    private int lastPlayerBtnH = 12;

    private int lastExpandModelBtnX = -1;
    private int lastExpandModelBtnY = -1;
    private int lastExpandModelBtnW = 14;
    private int lastExpandModelBtnH = 12;

    private int lastToggleLoreExpandBtnX = -1;
    private int lastToggleLoreExpandBtnY = -1;
    private int lastToggleLoreExpandBtnW = 85;
    private int lastToggleLoreExpandBtnH = 12;

    public ComplexBestiaryScreen() {
        super(Component.translatable("gui.changed_addon.bestiary.title"));
    }

    private int getAvailableInnerWidth() {
        return this.dialogW - 22;
    }

    private int getEffectiveVpW() {
        int availableInnerW = getAvailableInnerWidth();
        if (displayMode == DisplayMode.MODEL_ONLY) {
            return availableInnerW + 6;
        } else if (displayMode == DisplayMode.HIDE_MODEL) {
            return 0;
        }
        return Math.max(140, Math.min(175, availableInnerW * 42 / 100));
    }

    private int getEffectiveDetW() {
        int availableInnerW = getAvailableInnerWidth();
        if (displayMode == DisplayMode.HIDE_MODEL) {
            return availableInnerW + 6;
        } else if (displayMode == DisplayMode.MODEL_ONLY) {
            return 0;
        }
        return availableInnerW - getEffectiveVpW();
    }

    @Override
    protected void init() {
        super.init();

        this.dialogW = Math.min(BASE_W, this.width - 12);
        this.dialogH = Math.min(BASE_H, this.height - 12);
        this.left = (this.width - this.dialogW) / 2;
        this.top = (this.height - this.dialogH) / 2;

        if (allVariants.isEmpty()) {
            List<TransfurVariant<?>> variants = TransfurVariant.getPublicTransfurVariants()
                    .sorted(Comparator.comparing(var -> var.getFormId().toString()))
                    .toList();
            allVariants.addAll(variants);
        }

        // Search Box Configuration
        this.searchBox = new EditBox(this.font, 0, 0, 10, 14, Component.translatable("gui.changed_addon.bestiary.search"));
        this.searchBox.setHint(Component.translatable("gui.changed_addon.bestiary.search.hint"));
        this.searchBox.setResponder(s -> updateFilteredVariants());
        this.searchBox.setMaxLength(32);
        this.searchBox.setFocused(false);
        this.addRenderableWidget(this.searchBox);

        updateSearchBoxBounds();
        updateFilteredVariants();
        updateSearchBoxVisibility();

        if (this.selected == null && !filteredVariants.isEmpty()) {
            selectTf(ChangedTransfurVariants.GAS_WOLF_MALE.get());
        } else if (this.selected != null) {
            refreshSelectedEntity();
        }
    }

    private void updateSearchBoxBounds() {
        if (this.searchBox != null) {
            int detW = getEffectiveDetW();
            int detX = (displayMode == DisplayMode.HIDE_MODEL)
                    ? (this.left + 8)
                    : (this.left + 8 + getEffectiveVpW() + 6);
            int detY = this.top + 22;
            int contentY = detY + 16;

            this.searchBox.setX(detX + 3);
            this.searchBox.setY(contentY + 2);
            this.searchBox.setWidth(Math.max(10, detW - 6));
        }
    }

    private void updateSearchBoxVisibility() {
        if (this.searchBox != null) {
            boolean isSelectorTab = (this.activeTab == 0) && (displayMode != DisplayMode.MODEL_ONLY);
            this.searchBox.setVisible(isSelectorTab);
            if (!isSelectorTab) {
                this.searchBox.setFocused(false);
            }
        }
    }

    private void updateFilteredVariants() {
        filteredVariants.clear();
        String query = searchBox != null ? searchBox.getValue().toLowerCase(Locale.ROOT).trim() : "";

        for (TransfurVariant<?> var : allVariants) {
            if (query.isEmpty()) {
                filteredVariants.add(var);
            } else {
                String name = getVariantDisplayName(var).toLowerCase(Locale.ROOT);
                String id = var.getFormId().toString().toLowerCase(Locale.ROOT);
                if (name.contains(query) || id.contains(query)) {
                    filteredVariants.add(var);
                }
            }
        }
        this.gridRowScroll = 0;
    }

    private Component getVariantDisplayNameComponent(TransfurVariant<?> tf) {
        if (tf == null) return Component.translatable("gui.changed_addon.bestiary.unknown");
        EntityType<?> type = tf.getEntityType();
        if (type != null) {
            return Component.translatable(type.getDescriptionId());
        }
        return Component.literal(tf.getFormId().getPath());
    }

    private String getVariantDisplayName(TransfurVariant<?> tf) {
        return getVariantDisplayNameComponent(tf).getString();
    }

    @Override
    public void tick() {
        super.tick();
        if (currentEntity != null && this.minecraft != null && this.minecraft.player != null) {
            currentEntity.tickCount = this.minecraft.player.tickCount;
        }
    }

    protected void selectTf(TransfurVariant<?> tf) {
        if (tf == null) return;
        this.selected = tf;
        refreshSelectedEntity();
    }

    private void refreshSelectedEntity() {
        if (this.selected == null || this.minecraft == null || this.minecraft.level == null) {
            return;
        }

        Level level = this.minecraft.level;
        Player player = this.minecraft.player;
        ChangedEntity entity = (ChangedEntity) ChangedEntities.getCachedEntity(level, this.selected.getEntityType());

        if (entity instanceof IBestiaryEntityData data) {
            EntityType<?> refType = data.getReferencedEntityType();
            if (refType != null && refType != entity.getType()) {
                Entity cached = ChangedEntities.getCachedEntity(level, refType);
                if (cached instanceof ChangedEntity ce) {
                    entity = ce;
                }
            }
            this.isUnlocked = player == null || data.isUnlocked(player);
        } else {
            this.isUnlocked = true;
        }

        this.currentEntity = entity;
        this.detailsScrollOffset = 0;
        this.chartScaleFactor = 1.0f;
        this.buildDetailsAndBarChart(entity);
    }

    private void setDisplayMode(DisplayMode mode) {
        this.displayMode = mode;
        updateSearchBoxBounds();
        updateSearchBoxVisibility();
        if (this.currentEntity != null) {
            buildDetailsAndBarChart(this.currentEntity);
        }
    }

    private void buildDetailsAndBarChart(ChangedEntity entity) {
        loreLines.clear();
        attributeBars.clear();
        playerAttributeBars.clear();
        classificationText = "";

        if (entity == null) return;

        int detW = getEffectiveDetW();
        int textWrapWidth = Math.max(100, detW - 16);

        if (entity instanceof IBestiaryEntityData data) {
            List<IBestiaryEntityData.BestiaryInfo> infos = data.getBestiaryInfo().stream()
                    .sorted(Comparator.comparingInt(IBestiaryEntityData.BestiaryInfo::order))
                    .toList();
            for (IBestiaryEntityData.BestiaryInfo info : infos) {
                String titleStr = info.title().getString();
                if (titleStr.equalsIgnoreCase("Attributes") || titleStr.equalsIgnoreCase(Component.translatable("gui.changed_addon.bestiary.attributes").getString()))
                    continue;

                if (titleStr.equalsIgnoreCase("Classification") || titleStr.toLowerCase().contains("class") || titleStr.equalsIgnoreCase(Component.translatable("gui.changed_addon.bestiary.classification").getString())) {
                    classificationText = info.description().getString();
                } else {
                    loreLines.addAll(this.font.split(
                            Component.literal("§e").append(info.title()).append(": ").append(info.description()), textWrapWidth));
                }
            }
        }

        List<Component> subtitles = ChangedEntityUtil.getEntitySubtitle(entity);
        if (!subtitles.isEmpty()) {
            for (int i = 0; i < subtitles.size(); i++) {
                Component sub = subtitles.get(i);
                String subStr = sub.getString();
                if (subStr.contains("Classification:")) {
                    if (classificationText.isEmpty() && i + 1 < subtitles.size()) {
                        classificationText = subtitles.get(i + 1).getString().replace("§f", "").trim();
                        i++;
                        continue;
                    }
                }
                loreLines.addAll(this.font.split(sub, textWrapWidth));
            }
        }

        if (loreLines.isEmpty()) {
            loreLines.addAll(this.font.split(Component.translatable("gui.changed_addon.bestiary.lore.fallback"), textWrapWidth));
        }

        AttributeSupplier playerDefaults = DefaultAttributes.getSupplier(EntityType.PLAYER);
        AttributeMap transformedMap = entity.getAttributes();

        checkAndAddBar(transformedMap, playerDefaults, Attributes.MAX_HEALTH, Component.translatable(Attributes.MAX_HEALTH.getDescriptionId()), 60.0f, 0xFFFF4444, 0xFFFF7777);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ARMOR, Component.translatable(Attributes.ARMOR.getDescriptionId()), 20.0f, 0xFF55FFFF, 0xFFAAFFFF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.MOVEMENT_SPEED, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()), 0.20f, 0xFF55FF55, 0xFFAAFFAA);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ATTACK_DAMAGE, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()), 16.0f, 0xFFFFAA00, 0xFFFFDD55);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.ARMOR_TOUGHNESS, Component.translatable(Attributes.ARMOR_TOUGHNESS.getDescriptionId()), 12.0f, 0xFF88AAFF, 0xFFBBDDFF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.KNOCKBACK_RESISTANCE, Component.translatable(Attributes.KNOCKBACK_RESISTANCE.getDescriptionId()), 1.0f, 0xFFCC66FF, 0xFFEE99FF);
        checkAndAddBar(transformedMap, playerDefaults, Attributes.FOLLOW_RANGE, Component.translatable(Attributes.FOLLOW_RANGE.getDescriptionId()), 48.0f, 0xFFBBAA88, 0xFFDDCCAA);

        recalculateMaxScroll();
    }

    private void recalculateMaxScroll() {
        int contentHeight;
        int viewH = this.dialogH - 46;

        if (activeTab == 1) {
            contentHeight = 24 + (loreLines.size() * 10);
        } else if (activeTab == 2) {
            int radarHeight = attributeBars.isEmpty() ? 0 : 115;
            contentHeight = 24 + (attributeBars.size() * 22) + radarHeight;
        } else {
            contentHeight = 0;
        }

        this.maxDetailsScroll = Math.max(0, contentHeight - viewH);
    }

    private void checkAndAddBar(AttributeMap map, AttributeSupplier playerDefaults,
                                Attribute attr, Component label, float maxScale, int fillColor, int highlightColor) {
        if (!map.hasAttribute(attr)) return;

        double value = map.getValue(attr);
        double base = playerDefaults.hasAttribute(attr) ? playerDefaults.getBaseValue(attr) : 0.0;

        if (attr == Attributes.MOVEMENT_SPEED && value > 0.5) {
            value *= 0.1;
        }

        double diff = value - base;
        String valText;
        if (attr == Attributes.MOVEMENT_SPEED) {
            double percent = (base > 0) ? (value / base - 1.0) * 100.0 : 0.0;
            valText = String.format(Locale.ROOT, "%+.0f%%", percent);
        } else if (diff != 0.0) {
            valText = String.format(Locale.ROOT, "%.1f (%+.1f)", value, diff);
        } else {
            valText = String.format(Locale.ROOT, "%.1f", value);
        }

        float ratio = clamp((float) (value / maxScale), 0.05f, 1.0f);
        attributeBars.add(new AttributeBarItem(label, valText, ratio, fillColor, highlightColor));

        if (playerDefaults.hasAttribute(attr)) {
            double pVal = playerDefaults.getBaseValue(attr);
            String pValText = (attr == Attributes.MOVEMENT_SPEED)
                    ? String.format(Locale.ROOT, "%.2f", pVal)
                    : String.format(Locale.ROOT, "%.1f", pVal);
            float pRatio = clamp((float) (pVal / maxScale), 0.05f, 1.0f);
            playerAttributeBars.add(new AttributeBarItem(label, pValText, pRatio, 0xFF35A2FF, 0xFF70C5FF));
        } else {
            playerAttributeBars.add(new AttributeBarItem(label, "0.0", 0.05f, 0xFF35A2FF, 0xFF70C5FF));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, 0x70000000);

        renderVanillaContainerFrame(graphics, this.left, this.top, this.dialogW, this.dialogH);
        graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.header"), this.left + 10, this.top + 7, 0xFFFFFF);

        Component escHint = Component.translatable("gui.changed_addon.bestiary.esc_close");
        int escW = this.font.width(escHint);
        int escX = this.left + this.dialogW - escW - 10;
        int escY = this.top + 7;
        boolean isEscHovered = mouseX >= escX - 2 && mouseX <= escX + escW + 2 && mouseY >= escY - 2 && mouseY <= escY + 11;
        graphics.drawString(this.font, escHint, escX, escY, isEscHovered ? 0xFFFFAA : 0x888888, false);

        int vpW = getEffectiveVpW();
        int detW = getEffectiveDetW();

        int vpX = this.left + 8;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = (displayMode == DisplayMode.HIDE_MODEL) ? vpX : vpX + vpW + 6;
        int detY = this.top + 22;
        int detH = this.dialogH - 30;

        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0) {
            renderModelViewport(graphics, vpX, vpY, vpW, vpH, mouseX, mouseY, partialTick);
        }

        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            renderDetailsTabs(graphics, detX, detY, detW, mouseX, mouseY);

            int contentY = detY + 16;
            int contentH = detH - 16;
            if (activeTab == 0) {
                renderVariantGrid(graphics, detX, contentY, detW, contentH, mouseX, mouseY);
            } else if (activeTab == 1) {
                renderLoreTab(graphics, detX, contentY, detW, contentH, mouseX, mouseY);
            } else {
                renderAttributesGridTab(graphics, detX, contentY, detW, contentH, mouseX, mouseY);
            }
        }

        super.render(graphics, mouseX, mouseY, partialTick);

        String screenWm = "§8© ParkaBird 2026";
        int swmW = this.font.width("© ParkaBird 2026");
        graphics.drawString(this.font, screenWm, this.width - swmW - 8, this.height - 13, 0x888888, false);
    }

    private void renderVanillaContainerFrame(GuiGraphics graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + h, 0xF0141416);
        graphics.fill(x, y, x + w, y + 1, 0xFF4C4C54);
        graphics.fill(x, y, x + 1, y + h, 0xFF4C4C54);
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF18181A);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF18181A);
        graphics.renderOutline(x + 1, y + 1, w - 2, h - 2, 0xFF2A2A30);
        graphics.fill(x + 4, y + 19, x + w - 4, y + 20, 0xFF3E3E48);
    }

    private void renderInsetBox(GuiGraphics graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + h, 0xFF0D0D10);
        graphics.fill(x, y, x + w, y + 1, 0xFF16161A);
        graphics.fill(x, y, x + 1, y + h, 0xFF16161A);
        graphics.fill(x, y + h - 1, x + w, y + h, 0xFF35353D);
        graphics.fill(x + w - 1, y, x + w, y + h, 0xFF35353D);
    }

    private void renderDetailsTabs(GuiGraphics graphics, int x, int y, int w, int mouseX, int mouseY) {
        int numTabs = 3;
        int tabW = (w - (numTabs - 1) * 2) / numTabs;
        int tabH = 14;

        Component[] titles = {
                Component.translatable("gui.changed_addon.bestiary.tab.entities"),
                Component.translatable("gui.changed_addon.bestiary.tab.lore"),
                Component.translatable("gui.changed_addon.bestiary.tab.stats")
        };

        for (int i = 0; i < numTabs; i++) {
            int tabX = x + (i * (tabW + 2));
            boolean isActive = (activeTab == i);
            boolean isHovered = mouseX >= tabX && mouseX < tabX + tabW && mouseY >= y && mouseY < y + tabH;

            int bg = isActive ? 0xFF282834 : (isHovered ? 0xFF1E1E26 : 0xFF121218);
            int border = isActive ? 0xFFFFAA00 : (isHovered ? 0xFF585868 : 0xFF282832);

            graphics.fill(tabX, y, tabX + tabW, y + tabH, bg);
            graphics.renderOutline(tabX, y, tabW, tabH, border);

            Component title = titles[i];
            int titleW = this.font.width(title);
            int textColor = isActive ? 0xFFFFAA : (isHovered ? 0xFFE0E0E0 : 0x888888);
            graphics.drawString(this.font, title, tabX + (tabW - titleW) / 2, y + 3, textColor, false);
        }
    }

    private void renderVariantGrid(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);

        int gridY = y + 18;
        int gridH = h - 18;

        int totalCount = filteredVariants.size();
        int cellSize = (w - 8) / GRID_COLS;
        int rows = (int) Math.ceil(totalCount / (double) GRID_COLS);
        int visibleRows = Math.max(1, (gridH - 4) / cellSize);

        int maxScroll = Math.max(0, rows - visibleRows);
        gridRowScroll = clamp(gridRowScroll, 0, maxScroll);

        boolean scissored = false;
        if (w > 4 && gridH > 4) {
            graphics.enableScissor(x + 2, gridY + 2, x + w - 2, gridY + gridH - 2);
            scissored = true;
        }

        TransfurVariant<?> hoveredTf = null;

        try {
            int firstIndex = gridRowScroll * GRID_COLS;
            for (int i = firstIndex; i < totalCount; i++) {
                int row = i / GRID_COLS - gridRowScroll;
                int col = i % GRID_COLS;
                if (row > visibleRows) break;

                int cellX = x + 3 + col * cellSize;
                int cellY = gridY + 3 + row * cellSize;
                int cellW = cellSize - 2;
                int cellH = cellSize - 2;

                TransfurVariant<?> tf = filteredVariants.get(i);
                boolean isSelected = (this.selected == tf);
                boolean isHovered = mouseX >= cellX && mouseX < cellX + cellW && mouseY >= cellY && mouseY < cellY + cellH;
                if (isHovered) hoveredTf = tf;

                int btnBg = isSelected ? 0xFF384458 : (isHovered ? 0xFF282830 : 0xFF1A1A20);
                int btnBorder = isSelected ? 0xFFFFAA00 : (isHovered ? 0xFF585868 : 0xFF2A2A32);

                graphics.fill(cellX, cellY, cellX + cellW, cellY + cellH, btnBg);
                graphics.renderOutline(cellX, cellY, cellW, cellH, btnBorder);

                renderVariantIcon(graphics, tf, cellX + cellW / 2, cellY + cellH / 2, cellW - 6);
            }
        } finally {
            if (scissored) {
                graphics.disableScissor();
            }
        }

        if (rows > visibleRows && maxScroll > 0) {
            int barX = x + w - 7;
            int trackY = gridY + 3;
            int trackH = gridH - 6;
            int trackW = 4;
            int thumbH = Math.max(14, (visibleRows * trackH) / rows);
            int travel = trackH - thumbH;
            int thumbY = trackY + (int) ((float) gridRowScroll / maxScroll * travel);

            graphics.fill(barX, trackY, barX + trackW, trackY + trackH, 0xFF101014);

            boolean isHovered = mouseX >= barX - 2 && mouseX <= barX + trackW + 2 && mouseY >= thumbY && mouseY <= thumbY + thumbH;
            int thumbBg = isDraggingListScroll ? 0xFF8E8EA0 : (isHovered ? 0xFF767688 : 0xFF4E4E5C);
            int thumbBorder = isDraggingListScroll ? 0xFFBCBCCE : (isHovered ? 0xFF9E9EB2 : 0xFF646476);

            graphics.fill(barX, thumbY, barX + trackW, thumbY + thumbH, thumbBg);
            graphics.fill(barX, thumbY, barX + trackW, thumbY + 1, thumbBorder);
            graphics.fill(barX, thumbY, barX + 1, thumbY + thumbH, thumbBorder);
        }

        if (hoveredTf != null) {
            graphics.renderTooltip(this.font, getVariantDisplayNameComponent(hoveredTf), mouseX, mouseY);
        }
    }

    private void renderVariantIcon(GuiGraphics graphics, TransfurVariant<?> tf, int centerX, int centerY, float size) {
        if (this.minecraft == null || this.minecraft.level == null) {
            renderInitialsFallback(graphics, tf, centerX, centerY);
            return;
        }

        Entity entity = ChangedEntities.getCachedEntity(this.minecraft.level, tf.getEntityType());
        if (!(entity instanceof ChangedEntity changedEntity)) {
            renderInitialsFallback(graphics, tf, centerX, centerY);
            return;
        }

        float oldYBodyRot = changedEntity.yBodyRot;
        float oldYRot = changedEntity.getYRot();
        float oldXRot = changedEntity.getXRot();
        float oldYHeadRot = changedEntity.yHeadRot;
        float oldYHeadRotO = changedEntity.yHeadRotO;

        float targetYaw = 180.0f + ICON_YAW;

        changedEntity.yBodyRot = targetYaw;
        changedEntity.setYRot(targetYaw);
        changedEntity.setXRot(ICON_PITCH);
        changedEntity.yHeadRot = targetYaw;
        changedEntity.yHeadRotO = targetYaw;

        Quaternionf pose = new Quaternionf().rotateZ((float) Math.PI);

        float height = Math.max(0.1f, changedEntity.getBbHeight());
        float width = Math.max(0.1f, changedEntity.getBbWidth());
        float maxDimension = Math.max(height, width);

        float referenceSize = 1.8f;
        float fitModifier = Math.min(1.0f, referenceSize / maxDimension);
        float entityScale = changedEntity.getScale();

        float baseScale = size * 0.48f;
        float adjustedScale = Math.max(4.0f, baseScale * fitModifier * entityScale);
        float renderY = centerY + (size / 2.0f) + ICON_Y_OFFSET;

        try {
            GuiUtils.renderEntityInInventory(graphics, centerX, renderY, adjustedScale, pose, null, changedEntity);
        } catch (Exception e) {
            renderInitialsFallback(graphics, tf, centerX, centerY);
        } finally {
            changedEntity.yBodyRot = oldYBodyRot;
            changedEntity.setYRot(oldYRot);
            changedEntity.setXRot(oldXRot);
            changedEntity.yHeadRot = oldYHeadRot;
            changedEntity.yHeadRotO = oldYHeadRotO;
        }
    }

    private void renderInitialsFallback(GuiGraphics graphics, TransfurVariant<?> tf, int centerX, int centerY) {
        String name = getVariantDisplayName(tf);
        String initials = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase(Locale.ROOT);
        int color = (name.hashCode() & 0x00FFFFFF) | 0xFF000000;
        graphics.drawCenteredString(this.font, initials, centerX, centerY - 4, color);
    }

    private void renderModelViewport(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY, float partialTick) {
        renderInsetBox(graphics, x, y, w, h);

        // Expand / Restore Model Toggle Button
        int btnW = 14;
        int btnH = 12;
        int btnX = x + w - btnW - 3;
        int btnY = y + 3;

        this.lastExpandModelBtnX = btnX;
        this.lastExpandModelBtnY = btnY;
        this.lastExpandModelBtnW = btnW;
        this.lastExpandModelBtnH = btnH;

        boolean isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        boolean isModelOnly = (displayMode == DisplayMode.MODEL_ONLY);

        int btnBg = isModelOnly ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20);
        int btnBorder = isModelOnly ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32);

        graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, btnBg);
        graphics.renderOutline(btnX, btnY, btnW, btnH, btnBorder);

        String btnSymbol = isModelOnly ? "◀" : "⛶";
        int symW = this.font.width(btnSymbol);
        graphics.drawString(this.font, btnSymbol, btnX + (btnW - symW) / 2, btnY + 2, isModelOnly ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

        if (isBtnHovered) {
            Component tooltip = Component.translatable(isModelOnly ? "gui.changed_addon.bestiary.button.restore_split" : "gui.changed_addon.bestiary.button.expand_model");
            graphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }

        if (this.currentEntity != null) {
            int centerX = x + w / 2;
            int centerY = y + h - 26;

            graphics.fill(centerX - 24, centerY - 2, centerX + 24, centerY + 3, 0x44000000);
            graphics.fill(centerX - 18, centerY - 3, centerX + 18, centerY + 4, 0x33000000);

            if (!this.isUnlocked) {
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.locked"), centerX - 24, y + 8, 0xFF5555, false);
            }

            float oldYBodyRot = this.currentEntity.yBodyRot;
            float oldYRot = this.currentEntity.getYRot();
            float oldXRot = this.currentEntity.getXRot();
            float oldYHeadRot = this.currentEntity.yHeadRot;
            float oldYHeadRotO = this.currentEntity.yHeadRotO;

            this.currentEntity.yBodyRot = 180.0f;
            this.currentEntity.setYRot(180.0f);
            this.currentEntity.setXRot(0.0f);
            this.currentEntity.yHeadRot = 180.0f;
            this.currentEntity.yHeadRotO = 180.0f;

            // Upright Quaternion: rotateZ by PI flips GUI inverted Y right-side up!
            Quaternionf pose = new Quaternionf()
                    .rotateZ((float) Math.PI)
                    .rotateX(modelPitch * ((float) Math.PI / 180.0f))
                    .rotateY(modelYaw * ((float) Math.PI / 180.0f));

            boolean scissored = false;
            if (w > 4 && h > 4) {
                graphics.enableScissor(x + 2, y + 2, x + w - 2, y + h - 2);
                scissored = true;
            }

            try {
                int modelCenterX = centerX + (int) modelOffsetX;
                int modelCenterY = centerY + (int) modelOffsetY;
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 50);
                InventoryScreen.renderEntityInInventory(graphics, modelCenterX, modelCenterY, (int) modelZoom, pose, null, this.currentEntity);
                graphics.pose().popPose();
            } catch (Exception e) {
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.render_error"), centerX - 28, centerY - 10, 0xFF5555, false);
            } finally {
                if (scissored) {
                    graphics.disableScissor();
                }
                // Restore old rotations
                this.currentEntity.yBodyRot = oldYBodyRot;
                this.currentEntity.setYRot(oldYRot);
                this.currentEntity.setXRot(oldXRot);
                this.currentEntity.yHeadRot = oldYHeadRot;
                this.currentEntity.yHeadRotO = oldYHeadRotO;
            }
        } else {
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.no_model"), x + w / 2 - 24, y + h / 2 - 4, 0x777777, false);
        }

        Component hint = Component.translatable("gui.changed_addon.bestiary.model_hint");
        int hintW = this.font.width(hint);
        float hintScale = 0.75f;
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + (w - (int) (hintW * hintScale)) / 2.0, y + h - 9, 0.0);
        pose.scale(hintScale, hintScale, 1.0f);
        graphics.drawString(this.font, hint, 0, 0, 0x888888, false);
        pose.popPose();
    }

    private void renderLoreTab(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);
        if (this.selected == null) return;

        boolean scissored = false;
        if (w > 6 && h > 6) {
            graphics.enableScissor(x + 3, y + 3, x + w - 3, y + h - 3);
            scissored = true;
        }

        try {
            int curY = y + 5 - detailsScrollOffset;

            Component name = getVariantDisplayNameComponent(this.selected);
            graphics.drawString(this.font, Component.literal("§e§l").append(name), x + 6, curY, 0xFFFF55, false);
            curY += 12;

            if (!classificationText.isEmpty()) {
                Component classDisplay = Component.translatable("gui.changed_addon.bestiary.lore.classification", classificationText);
                graphics.drawString(this.font, classDisplay, x + 6, curY, 0xAAAAAA, false);
                curY += 11;
            }

            graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
            curY += 5;

            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.lore.dossier_header"), x + 6, curY, 0xFFAA00, false);

            // Toggle Expand Lore Button (Model Hidder)
            int btnW = 85;
            int btnH = 12;
            int btnX = x + w - btnW - 10;
            int btnY = curY - 2;

            this.lastToggleLoreExpandBtnX = btnX;
            this.lastToggleLoreExpandBtnY = btnY;
            this.lastToggleLoreExpandBtnW = btnW;
            this.lastToggleLoreExpandBtnH = btnH;

            boolean isHideModel = (displayMode == DisplayMode.HIDE_MODEL);
            boolean isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;

            int btnBg = isHideModel ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20);
            int btnBorder = isHideModel ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32);

            graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, btnBg);
            graphics.renderOutline(btnX, btnY, btnW, btnH, btnBorder);

            Component btnText = Component.translatable(isHideModel ? "gui.changed_addon.bestiary.button.show_model" : "gui.changed_addon.bestiary.button.expand_text");
            int textW = this.font.width(btnText);
            int textY = btnY + (btnH - 8) / 2;
            graphics.drawString(this.font, btnText, btnX + (btnW - textW) / 2, textY, isHideModel ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

            curY += 11;

            for (FormattedCharSequence line : loreLines) {
                graphics.drawString(this.font, line, x + 6, curY, 0xD0D0D0, false);
                curY += 10;
            }
        } finally {
            if (scissored) {
                graphics.disableScissor();
            }
        }

        renderDetailsScrollbar(graphics, x, y, w, h, mouseX, mouseY);
    }

    private void renderAttributesGridTab(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);
        if (this.selected == null) return;

        boolean scissored = false;
        if (w > 6 && h > 6) {
            graphics.enableScissor(x + 3, y + 3, x + w - 3, y + h - 3);
            scissored = true;
        }

        try {
            int curY = y + 5 - detailsScrollOffset;

            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.attribute_sheet_header"), x + 6, curY, 0x55FF55, false);
            curY += 14;

            int barW = w - 16;
            int barH = 6;

            for (AttributeBarItem bar : attributeBars) {
                graphics.drawString(this.font, bar.label, x + 6, curY, 0xE0E0E0, false);
                int valW = this.font.width(bar.valueText);
                graphics.drawString(this.font, bar.valueText, x + 6 + barW - valW, curY, 0xFFFFAA, false);
                curY += 10;

                graphics.fill(x + 6, curY, x + 6 + barW, curY + barH, 0xFF141416);
                graphics.renderOutline(x + 6, curY, barW, barH, 0xFF303038);

                int fillW = Math.max(2, Math.round((barW - 2) * bar.ratio));
                graphics.fill(x + 7, curY + 1, x + 7 + fillW, curY + barH - 1, bar.fillColor);
                graphics.fill(x + 7, curY + 1, x + 7 + fillW, curY + 2, bar.highlightColor);

                curY += barH + 6;
            }

            // Radar Chart Section Header
            if (!attributeBars.isEmpty()) {
                curY += 4;
                graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
                curY += 10;

                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.radar_chart_header"), x + 6, curY, 0x55FFFF, false);

                // Toggle Button for Player Chart Overlay
                int btnW = 92;
                int btnH = 12;
                int btnX = x + w - btnW - 10;
                int btnY = curY - 2;

                this.lastPlayerBtnX = btnX;
                this.lastPlayerBtnY = btnY;
                this.lastPlayerBtnW = btnW;
                this.lastPlayerBtnH = btnH;

                boolean isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
                int btnBg = showPlayerChart ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20);
                int btnBorder = showPlayerChart ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32);

                graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, btnBg);
                graphics.renderOutline(btnX, btnY, btnW, btnH, btnBorder);

                Component btnText = Component.translatable(showPlayerChart ? "gui.changed_addon.bestiary.button.hide_player_overlay" : "gui.changed_addon.bestiary.button.show_player_overlay");
                int textW = this.font.width(btnText);
                int textY = btnY + (btnH - 8) / 2;
                graphics.drawString(this.font, btnText, btnX + (btnW - textW) / 2, textY, showPlayerChart ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

                curY += 14;

                Component scaleStr = Component.translatable("gui.changed_addon.bestiary.scale", String.format(Locale.ROOT, "%.1f", chartScaleFactor));
                int scaleW = this.font.width(scaleStr);
                graphics.drawString(this.font, scaleStr, x + w - scaleW - 10, curY, 0x888888, false);

                curY += 10;

                int chartRadius = 32;
                int chartCenterX = x + (w / 2);
                int chartCenterY = curY + chartRadius + 4;

                renderRadialAttributeChart(graphics, chartCenterX, chartCenterY, chartRadius, attributeBars, showPlayerChart ? playerAttributeBars : null, mouseX, mouseY);
            }

        } finally {
            if (scissored) {
                graphics.disableScissor();
            }
        }

        renderDetailsScrollbar(graphics, x, y, w, h, mouseX, mouseY);
    }

    private void renderRadialAttributeChart(GuiGraphics graphics, int cx, int cy, float radius, List<AttributeBarItem> entityBars, List<AttributeBarItem> playerBars, int mouseX, int mouseY) {
        int size = entityBars.size();
        if (size < 3) return;

        this.lastChartCenterX = cx;
        this.lastChartCenterY = cy;
        this.lastChartRadius = (int) radius;

        Matrix4f matrix = graphics.pose().last().pose();
        float step = (float) (Math.PI * 2) / size;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        // 1. Background Fill Web
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(0.08f, 0.08f, 0.12f, 0.7f).endVertex();
        for (int i = 0; i <= size; i++) {
            float angle = (i % size) * step - (float) Math.PI / 2;
            float vx = cx + Mth.cos(angle) * radius;
            float vy = cy + Mth.sin(angle) * radius;
            buffer.vertex(matrix, vx, vy, 0).color(0.08f, 0.08f, 0.12f, 0.7f).endVertex();
        }
        tesselator.end();

        // 2. Player Attribute Fan
        if (playerBars != null && playerBars.size() == size) {
            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(matrix, cx, cy, 0).color(0.21f, 0.63f, 1.0f, 0.30f).endVertex();
            for (int i = 0; i <= size; i++) {
                AttributeBarItem bar = playerBars.get(i % size);
                float angle = (i % size) * step - (float) Math.PI / 2;
                float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
                float scaledR = effectiveRatio * radius;

                float vx = cx + Mth.cos(angle) * scaledR;
                float vy = cy + Mth.sin(angle) * scaledR;
                buffer.vertex(matrix, vx, vy, 0).color(0.21f, 0.63f, 1.0f, 0.45f).endVertex();
            }
            tesselator.end();

            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
            for (int i = 0; i <= size; i++) {
                AttributeBarItem bar = playerBars.get(i % size);
                float angle = (i % size) * step - (float) Math.PI / 2;
                float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
                float scaledR = effectiveRatio * radius;

                float vx = cx + Mth.cos(angle) * scaledR;
                float vy = cy + Mth.sin(angle) * scaledR;
                buffer.vertex(matrix, vx, vy, 0).color(0.44f, 0.77f, 1.0f, 0.90f).endVertex();
            }
            tesselator.end();
        }

        // 3. Entity Attribute Fan
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(0.3f, 0.8f, 0.4f, 0.35f).endVertex();
        for (int i = 0; i <= size; i++) {
            AttributeBarItem bar = entityBars.get(i % size);
            float angle = (i % size) * step - (float) Math.PI / 2;
            float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
            float scaledR = effectiveRatio * radius;

            float vx = cx + Mth.cos(angle) * scaledR;
            float vy = cy + Mth.sin(angle) * scaledR;

            int c = bar.fillColor;
            float r = ((c >> 16) & 0xFF) / 255.0f;
            float g = ((c >> 8) & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 0.5f).endVertex();
        }
        tesselator.end();

        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= size; i++) {
            AttributeBarItem bar = entityBars.get(i % size);
            float angle = (i % size) * step - (float) Math.PI / 2;
            float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
            float scaledR = effectiveRatio * radius;

            float vx = cx + Mth.cos(angle) * scaledR;
            float vy = cy + Mth.sin(angle) * scaledR;

            int c = bar.highlightColor;
            float r = ((c >> 16) & 0xFF) / 255.0f;
            float g = ((c >> 8) & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 0.95f).endVertex();
        }
        tesselator.end();

        // 4. Center Spokes
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < size; i++) {
            float angle = i * step - (float) Math.PI / 2;
            float vx = cx + Mth.cos(angle) * radius;
            float vy = cy + Mth.sin(angle) * radius;

            buffer.vertex(matrix, cx, cy, 0).color(0.25f, 0.25f, 0.32f, 0.6f).endVertex();
            buffer.vertex(matrix, vx, vy, 0).color(0.25f, 0.25f, 0.32f, 0.6f).endVertex();
        }
        tesselator.end();

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        // 5. Outer Perimeter Dots & Hover Tooltips
        for (int i = 0; i < size; i++) {
            AttributeBarItem entityBar = entityBars.get(i);
            float angle = i * step - (float) Math.PI / 2;

            int outerX = Math.round(cx + Mth.cos(angle) * radius);
            int outerY = Math.round(cy + Mth.sin(angle) * radius);

            boolean isHovered = mouseX >= outerX - 4 && mouseX <= outerX + 4 && mouseY >= outerY - 4 && mouseY <= outerY + 4;
            int dotColor = isHovered ? 0xFFFFFFFF : entityBar.highlightColor;

            graphics.fill(outerX - 2, outerY - 2, outerX + 2, outerY + 2, dotColor);

            if (isHovered) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(entityBar.label.copy().withStyle(ChatFormatting.YELLOW));
                tooltip.add(Component.translatable("gui.changed_addon.bestiary.radar.entity_val", entityBar.valueText));
                if (playerBars != null && i < playerBars.size()) {
                    tooltip.add(Component.translatable("gui.changed_addon.bestiary.radar.player_val", playerBars.get(i).valueText));
                }
                graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        }
    }

    private void renderDetailsScrollbar(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (maxDetailsScroll > 0) {
            int barX = x + w - 7;
            int trackY = y + 3;
            int trackH = h - 6;
            int trackW = 4;
            int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
            int travel = trackH - thumbH;
            int thumbY = trackY + (int) ((float) detailsScrollOffset / maxDetailsScroll * travel);

            graphics.fill(barX, trackY, barX + trackW, trackY + trackH, 0xFF101014);

            boolean isHovered = mouseX >= barX - 2 && mouseX <= barX + trackW + 2 && mouseY >= thumbY && mouseY <= thumbY + thumbH;
            int thumbBg = isDraggingDetailsScroll ? 0xFF8E8EA0 : (isHovered ? 0xFF767688 : 0xFF4E4E5C);
            int thumbBorder = isDraggingDetailsScroll ? 0xFFBCBCCE : (isHovered ? 0xFF9E9EB2 : 0xFF646476);

            graphics.fill(barX, thumbY, barX + trackW, thumbY + thumbH, thumbBg);
            graphics.fill(barX, thumbY, barX + trackW, thumbY + 1, thumbBorder);
            graphics.fill(barX, thumbY, barX + 1, thumbY + thumbH, thumbBorder);
        }
    }

    // -------------------------------------------------------------
    // INPUT HANDLING & KEY NAVIGATION
    // -------------------------------------------------------------
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.activeTab == 0 && displayMode != DisplayMode.MODEL_ONLY && !filteredVariants.isEmpty()) {
            int currentIndex = selected != null ? filteredVariants.indexOf(selected) : -1;

            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                navigateSelection(currentIndex - 1);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                navigateSelection(currentIndex + 1);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_UP) {
                navigateSelection(currentIndex - GRID_COLS);
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                navigateSelection(currentIndex + GRID_COLS);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void navigateSelection(int targetIndex) {
        if (filteredVariants.isEmpty()) return;
        int clampedIndex = clamp(targetIndex, 0, filteredVariants.size() - 1);
        selectTf(filteredVariants.get(clampedIndex));

        int targetRow = clampedIndex / GRID_COLS;
        int detW = getEffectiveDetW();
        int gridH = (this.dialogH - 30) - 16 - 18;
        int cellSize = (detW - 8) / GRID_COLS;
        int visibleRows = Math.max(1, (gridH - 4) / cellSize);

        if (targetRow < gridRowScroll) {
            gridRowScroll = targetRow;
        } else if (targetRow >= gridRowScroll + visibleRows) {
            gridRowScroll = targetRow - visibleRows + 1;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int vpW = getEffectiveVpW();
        int detW = getEffectiveDetW();

        int vpX = this.left + 8;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = (displayMode == DisplayMode.HIDE_MODEL) ? vpX : vpX + vpW + 6;
        int detY = this.top + 22;

        Component escHint = Component.translatable("gui.changed_addon.bestiary.esc_close");
        int escW = this.font.width(escHint);
        int escX = this.left + this.dialogW - escW - 10;
        int escY = this.top + 7;
        if (button == 0 && mouseX >= escX - 4 && mouseX <= escX + escW + 4 && mouseY >= escY - 3 && mouseY <= escY + 12) {
            this.onClose();
            return true;
        }

        // Check Expand Model Toggle Button
        if (displayMode != DisplayMode.HIDE_MODEL && lastExpandModelBtnX != -1) {
            if (button == 0 && mouseX >= lastExpandModelBtnX && mouseX <= lastExpandModelBtnX + lastExpandModelBtnW
                    && mouseY >= lastExpandModelBtnY && mouseY <= lastExpandModelBtnY + lastExpandModelBtnH) {
                setDisplayMode(displayMode == DisplayMode.MODEL_ONLY ? DisplayMode.NORMAL : DisplayMode.MODEL_ONLY);
                return true;
            }
        }

        // Check Details Panel interactions
        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            int numTabs = 3;
            int tabW = (detW - (numTabs - 1) * 2) / numTabs;
            int tabH = 14;
            for (int i = 0; i < numTabs; i++) {
                int tabX = detX + (i * (tabW + 2));
                if (button == 0 && mouseX >= tabX && mouseX < tabX + tabW && mouseY >= detY && mouseY < detY + tabH) {
                    this.activeTab = i;
                    this.detailsScrollOffset = 0;
                    if (displayMode == DisplayMode.HIDE_MODEL && activeTab != 1) {
                        setDisplayMode(DisplayMode.NORMAL);
                    } else {
                        updateSearchBoxVisibility();
                        recalculateMaxScroll();
                    }
                    return true;
                }
            }

            if (activeTab == 1 && lastToggleLoreExpandBtnX != -1) {
                if (button == 0 && mouseX >= lastToggleLoreExpandBtnX && mouseX <= lastToggleLoreExpandBtnX + lastToggleLoreExpandBtnW
                        && mouseY >= lastToggleLoreExpandBtnY && mouseY <= lastToggleLoreExpandBtnY + lastToggleLoreExpandBtnH) {
                    setDisplayMode(displayMode == DisplayMode.HIDE_MODEL ? DisplayMode.NORMAL : DisplayMode.HIDE_MODEL);
                    return true;
                }
            }

            if (activeTab == 2 && lastPlayerBtnX != -1) {
                if (button == 0 && mouseX >= lastPlayerBtnX && mouseX <= lastPlayerBtnX + lastPlayerBtnW
                        && mouseY >= lastPlayerBtnY && mouseY <= lastPlayerBtnY + lastPlayerBtnH) {
                    showPlayerChart = !showPlayerChart;
                    recalculateMaxScroll();
                    return true;
                }
            }
        }

        // Check Viewport Drag / Interaction
        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0) {
            if (mouseX >= vpX && mouseX <= vpX + vpW && mouseY >= vpY && mouseY <= vpY + vpH) {
                if (button == 0) {
                    isDraggingModel = true;
                    return true;
                } else if (button == 1 && currentEntity != null) {
                    currentEntity.setPose(currentEntity.getPose() == Pose.STANDING ? Pose.CROUCHING : Pose.STANDING);
                    return true;
                }
                // Todo: Need some kind of zoom rework
                /*else if (button == 2) {
                    isDraggingModelOffset = true;
                    return true;
                }*/
            }
        }

        if (displayMode != DisplayMode.MODEL_ONLY && activeTab == 0) {
            int contentY = detY + 16;
            int gridY = contentY + 18;
            int gridH = (this.dialogH - 30) - 16 - 18;

            int totalCount = filteredVariants.size();
            int cellSize = (detW - 8) / GRID_COLS;
            int rows = (int) Math.ceil(totalCount / (double) GRID_COLS);
            int visibleRows = Math.max(1, (gridH - 4) / cellSize);
            int maxScroll = Math.max(0, rows - visibleRows);

            if (rows > visibleRows && maxScroll > 0) {
                int barX = detX + detW - 7;
                int trackY = gridY + 3;
                int trackH = gridH - 6;
                int trackW = 4;

                if (button == 0 && mouseX >= barX - 4 && mouseX <= barX + trackW + 4 && mouseY >= trackY && mouseY <= trackY + trackH) {
                    this.isDraggingListScroll = true;
                    int thumbH = Math.max(14, (visibleRows * trackH) / rows);
                    int travel = trackH - thumbH;
                    int thumbY = trackY + (int) ((float) gridRowScroll / maxScroll * travel);

                    if (mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                        this.listScrollGrabOffset = mouseY - thumbY;
                    } else {
                        this.listScrollGrabOffset = thumbH / 2.0;
                        double targetThumbY = mouseY - this.listScrollGrabOffset;
                        float progress = (float) (targetThumbY - trackY) / (float) travel;
                        this.gridRowScroll = clamp(Math.round(progress * maxScroll), 0, maxScroll);
                    }
                    return true;
                }
            }

            int firstIndex = gridRowScroll * GRID_COLS;
            for (int i = firstIndex; i < totalCount; i++) {
                int row = i / GRID_COLS - gridRowScroll;
                int col = i % GRID_COLS;

                int cellX = detX + 3 + col * cellSize;
                int cellY = gridY + 3 + row * cellSize;
                int cellW = cellSize - 2;
                int cellH = cellSize - 2;

                if (mouseX >= cellX && mouseX < cellX + cellW && mouseY >= cellY && mouseY < cellY + cellH) {
                    selectTf(filteredVariants.get(i));
                    return true;
                }
            }
        } else if (displayMode != DisplayMode.MODEL_ONLY) {
            int contentY = detY + 16;
            int contentH = (this.dialogH - 30) - 16;

            if (maxDetailsScroll > 0) {
                int barX = detX + detW - 7;
                int trackY = contentY + 3;
                int trackH = contentH - 6;
                int trackW = 4;

                if (button == 0 && mouseX >= barX - 4 && mouseX <= barX + trackW + 4 && mouseY >= trackY && mouseY <= trackY + trackH) {
                    this.isDraggingDetailsScroll = true;
                    int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
                    int travel = trackH - thumbH;
                    int thumbY = trackY + (int) ((float) detailsScrollOffset / maxDetailsScroll * travel);

                    if (mouseY >= thumbY && mouseY <= thumbY + thumbH) {
                        this.detailsScrollGrabOffset = mouseY - thumbY;
                    } else {
                        this.detailsScrollGrabOffset = thumbH / 2.0;
                        double targetThumbY = mouseY - this.detailsScrollGrabOffset;
                        float progress = (float) (targetThumbY - trackY) / (float) travel;
                        this.detailsScrollOffset = clamp(Math.round(progress * maxDetailsScroll), 0, maxDetailsScroll);
                    }
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDraggingModel = false;
        isDraggingModelOffset = false;
        isDraggingListScroll = false;
        isDraggingDetailsScroll = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingModel) {
            modelYaw += (float) (dragX * 1.5);
            modelPitch = clamp(modelPitch - (float) (dragY * 1.5), -45.0f, 45.0f);
            return true;
        }

        if (isDraggingModelOffset) {
            int vpW = getEffectiveVpW();
            int vpH = this.dialogH - 30;
            float maxOffsetX = vpW / 2.0f;
            float maxOffsetY = vpH / 2.0f;
            modelOffsetX = clamp(modelOffsetX + (float) dragX, -maxOffsetX, maxOffsetX);
            modelOffsetY = clamp(modelOffsetY + (float) dragY, -maxOffsetY, maxOffsetY);
            return true;
        }

        int detW = getEffectiveDetW();

        if (isDraggingListScroll && activeTab == 0 && displayMode != DisplayMode.MODEL_ONLY) {
            int gridH = (this.dialogH - 30) - 16 - 18;
            int totalCount = filteredVariants.size();
            int cellSize = (detW - 8) / GRID_COLS;
            int rows = (int) Math.ceil(totalCount / (double) GRID_COLS);
            int visibleRows = Math.max(1, (gridH - 4) / cellSize);
            int maxScroll = Math.max(0, rows - visibleRows);

            if (maxScroll > 0) {
                int trackY = this.top + 22 + 16 + 18 + 3;
                int trackH = gridH - 6;
                int thumbH = Math.max(14, (visibleRows * trackH) / rows);
                int travel = trackH - thumbH;
                if (travel > 0) {
                    double targetThumbY = mouseY - listScrollGrabOffset;
                    float progress = (float) (targetThumbY - trackY) / (float) travel;
                    gridRowScroll = clamp(Math.round(progress * maxScroll), 0, maxScroll);
                }
            }
            return true;
        }

        if (isDraggingDetailsScroll && activeTab != 0 && displayMode != DisplayMode.MODEL_ONLY) {
            int detY = this.top + 22 + 16;
            int detH = this.dialogH - 30 - 16;
            if (maxDetailsScroll > 0) {
                int trackY = detY + 3;
                int trackH = detH - 6;
                int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
                int travel = trackH - thumbH;
                if (travel > 0) {
                    double targetThumbY = mouseY - detailsScrollGrabOffset;
                    float progress = (float) (targetThumbY - trackY) / (float) travel;
                    detailsScrollOffset = clamp(Math.round(progress * maxDetailsScroll), 0, maxDetailsScroll);
                }
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int vpW = getEffectiveVpW();
        int detW = getEffectiveDetW();

        int vpX = this.left + 8;
        int vpY = this.top + 22;
        int vpH = this.dialogH - 30;

        int detX = (displayMode == DisplayMode.HIDE_MODEL) ? vpX : vpX + vpW + 6;
        int detY = this.top + 22;
        int detH = this.dialogH - 30;

        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0) {
            if (mouseX >= vpX && mouseX <= vpX + vpW && mouseY >= vpY && mouseY <= vpY + vpH) {
                modelZoom = clamp(modelZoom + (float) (delta * 3.0), 20.0f, 95.0f);
                return true;
            }
        }

        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            if (mouseX >= detX && mouseX <= detX + detW && mouseY >= detY && mouseY <= detY + detH) {
                if (activeTab == 0) {
                    int gridH = detH - 16 - 18;
                    int rows = (int) Math.ceil(filteredVariants.size() / (double) GRID_COLS);
                    int cellSize = (detW - 8) / GRID_COLS;
                    int visibleRows = Math.max(1, (gridH - 4) / cellSize);
                    int maxScroll = Math.max(0, rows - visibleRows);
                    gridRowScroll = clamp(gridRowScroll - (int) Math.signum(delta), 0, maxScroll);
                    return true;
                } else if (activeTab == 2 && lastChartCenterX != -1) {
                    double distEntity = Math.hypot(mouseX - lastChartCenterX, mouseY - lastChartCenterY);
                    if (distEntity <= lastChartRadius + 12) {
                        chartScaleFactor = clamp(chartScaleFactor + (float) (delta * 0.15f), 0.4f, 3.0f);
                        return true;
                    }
                }

                if (maxDetailsScroll > 0) {
                    detailsScrollOffset = clamp(detailsScrollOffset - (int) Math.signum(delta) * 12, 0, maxDetailsScroll);
                    return true;
                }
            }
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class AttributeBarItem {
        final Component label;
        final String valueText;
        final float ratio;
        final int fillColor;
        final int highlightColor;

        AttributeBarItem(Component label, String valueText, float ratio, int fillColor, int highlightColor) {
            this.label = label;
            this.valueText = valueText;
            this.ratio = ratio;
            this.fillColor = fillColor;
            this.highlightColor = highlightColor;
        }
    }

    private static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}