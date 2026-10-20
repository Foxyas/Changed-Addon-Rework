package net.foxyas.changedaddon.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.*;
import net.foxyas.changedaddon.client.gui.util.BestiaryModelViewportRenderer;
import net.foxyas.changedaddon.client.gui.util.BestiaryRadarChartRenderer;
import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntriesManager;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntry;
import net.foxyas.changedaddon.util.ChangedEntityUtil;
import net.foxyas.changedaddon.util.GuiUtils;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ComplexBestiaryScreen extends AbstractBestiaryScreen {
    private static final int BASE_W = 420;
    private static final int BASE_H = 230;

    public enum DisplayMode {
        NORMAL, HIDE_MODEL, MODEL_ONLY
    }

    private DisplayMode displayMode = DisplayMode.NORMAL;
    private boolean showPlayerEntry = true;

    private static final int ICON_Y_OFFSET = 1;
    private static final float ICON_YAW = -22.5f;
    private static final float ICON_PITCH = 0.0f;

    private int dialogW, dialogH, left, top;
    private int activeTab = 0;

    private boolean isPlayerSelected = false;
    private RemotePlayer fakePlayer = null;

    private final List<TransfurVariant<?>> allVariants = new ArrayList<>();
    private final List<TransfurVariant<?>> filteredVariants = new ArrayList<>();
    private EditBox searchBox;
    private static final int GRID_COLS = 5;
    private int gridRowScroll = 0;

    private boolean isDraggingListScroll = false;
    private double listScrollGrabOffset = 0.0;
    private boolean isDraggingDetailsScroll = false;
    private double detailsScrollGrabOffset = 0.0;

    protected TransfurVariant<?> selected;
    private LivingEntity currentEntity;
    private boolean isUnlocked = true;

    private float modelYaw = 25.0f;
    private float modelPitch = 0.0f;
    private float modelZoom = 52.0f;
    private float modelOffsetX = 0.0f;
    private float modelOffsetY = 0.0f;
    private boolean isDraggingModel = false;
    private boolean isDraggingModelOffset = false;

    private int detailsScrollOffset = 0;
    private int maxDetailsScroll = 0;
    private final List<AttributeBarItem> attributeBars = new ArrayList<>();
    private final List<AttributeBarItem> playerAttributeBars = new ArrayList<>();
    private final List<FormattedCharSequence> loreLines = new ArrayList<>();
    private String classificationText = "";
    private final List<Component> hoveredRadarTooltip = new ArrayList<>();

    private float chartScaleFactor = 1.0f;
    private boolean showPlayerChart = false;
    private int lastChartCenterX = -1;
    private int lastChartCenterY = -1;
    private int lastChartRadius = 32;

    private int lastPlayerBtnX = -1, lastPlayerBtnY = -1, lastPlayerBtnW = 92, lastPlayerBtnH = 12;
    private int lastExpandModelBtnX = -1, lastExpandModelBtnY = -1, lastExpandModelBtnW = 14, lastExpandModelBtnH = 12;
    private int lastToggleLoreExpandBtnX = -1, lastToggleLoreExpandBtnY = -1, lastToggleLoreExpandBtnW = 85, lastToggleLoreExpandBtnH = 12;

    public ComplexBestiaryScreen() {
        super(Component.translatable("gui.changed_addon.bestiary.title"));
    }

    private int getAvailableInnerWidth() { return this.dialogW - 22; }

    private int getEffectiveVpW() {
        int availableInnerW = getAvailableInnerWidth();
        if (displayMode == DisplayMode.MODEL_ONLY) return availableInnerW + 6;
        if (displayMode == DisplayMode.HIDE_MODEL) return 0;
        return Math.max(140, Math.min(175, availableInnerW * 42 / 100));
    }

    private int getEffectiveDetW() {
        int availableInnerW = getAvailableInnerWidth();
        if (displayMode == DisplayMode.HIDE_MODEL) return availableInnerW + 6;
        if (displayMode == DisplayMode.MODEL_ONLY) return 0;
        return availableInnerW - getEffectiveVpW();
    }

    public List<TransfurVariant<?>> getAllVariants() { return allVariants; }

    @Override
    protected void init() {
        super.init();
        this.dialogW = Math.min(BASE_W, this.width - 12);
        this.dialogH = Math.min(BASE_H, this.height - 12);
        this.left = (this.width - this.dialogW) / 2;
        this.top = (this.height - this.dialogH) / 2;

        if (this.minecraft != null && this.minecraft.level != null && this.minecraft.player != null) {
            this.fakePlayer = new RemotePlayer(this.minecraft.level, this.minecraft.player.getGameProfile());
            this.fakePlayer.noPhysics = true;
        }

        if (allVariants.isEmpty()) {
            allVariants.addAll(TransfurVariant.getPublicTransfurVariants()
                    .sorted(Comparator.comparing(var -> var.getFormId().toString()))
                    .toList());
        }

        this.searchBox = new EditBox(this.font, 0, 0, 10, 14, Component.translatable("gui.changed_addon.bestiary.search"));
        this.searchBox.setHint(Component.translatable("gui.changed_addon.bestiary.search.hint"));
        this.searchBox.setResponder(s -> updateFilteredVariants());
        this.searchBox.setMaxLength(32);
        this.searchBox.setFocused(false);
        this.addRenderableWidget(this.searchBox);

        updateSearchBoxBounds();
        updateFilteredVariants();
        updateSearchBoxVisibility();

        if (this.selected == null && !this.isPlayerSelected) {
            if (showPlayerEntry) selectPlayer();
            else if (!filteredVariants.isEmpty()) selectTf(filteredVariants.get(0));
        } else {
            refreshSelectedEntity();
        }
    }

    private boolean matchesPlayerSearch(String query) {
        if (!showPlayerEntry) return false;
        if (query.isEmpty()) return true;
        return getEntityDisplayNameComponent(fakePlayer).getString().toLowerCase(Locale.ROOT).contains(query) || "player".contains(query) || "human".contains(query);
    }

    private boolean shouldShowPlayerInGrid() {
        if (!showPlayerEntry) return false;
        String query = searchBox != null ? searchBox.getValue().toLowerCase(Locale.ROOT).trim() : "";
        return matchesPlayerSearch(query);
    }

    private int getTotalGridCount() { return filteredVariants.size() + (shouldShowPlayerInGrid() ? 1 : 0); }

    private void updateSearchBoxBounds() {
        if (this.searchBox != null) {
            int detW = getEffectiveDetW();
            int detX = (displayMode == DisplayMode.HIDE_MODEL) ? (this.left + 8) : (this.left + 8 + getEffectiveVpW() + 6);
            this.searchBox.setX(detX + 3);
            this.searchBox.setY(this.top + 22 + 16 + 2);
            this.searchBox.setWidth(Math.max(10, detW - 6));
        }
    }

    private void updateSearchBoxVisibility() {
        if (this.searchBox != null) {
            boolean isSelectorTab = (this.activeTab == 0) && (displayMode != DisplayMode.MODEL_ONLY);
            this.searchBox.setVisible(isSelectorTab);
            if (!isSelectorTab) this.searchBox.setFocused(false);
        }
    }

    private void updateFilteredVariants() {
        filteredVariants.clear();
        String query = searchBox != null ? searchBox.getValue().toLowerCase(Locale.ROOT).trim() : "";
        for (TransfurVariant<?> var : allVariants) {
            if (query.isEmpty() || getVariantDisplayName(var).toLowerCase(Locale.ROOT).contains(query) || var.getFormId().toString().toLowerCase(Locale.ROOT).contains(query)) {
                filteredVariants.add(var);
            }
        }
        this.gridRowScroll = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (currentEntity != null && this.minecraft != null && this.minecraft.player != null) {
            currentEntity.tickCount = this.minecraft.player.tickCount;
        }
    }

    private void selectPlayer() {
        if (!showPlayerEntry) return;
        this.selected = null;
        this.isPlayerSelected = true;
        refreshSelectedEntity();
    }

    protected void selectTf(TransfurVariant<?> tf) {
        if (tf == null) {
            if (showPlayerEntry) selectPlayer();
            else if (!filteredVariants.isEmpty()) selectTf(filteredVariants.get(0));
            return;
        }
        this.selected = tf;
        this.isPlayerSelected = false;
        refreshSelectedEntity();
    }

    private void refreshSelectedEntity() {
        if (this.minecraft == null || this.minecraft.level == null) return;

        if (!showPlayerEntry && this.isPlayerSelected) {
            this.isPlayerSelected = false;
            if (!filteredVariants.isEmpty()) this.selected = filteredVariants.get(0);
        }

        if (this.isPlayerSelected || (this.selected == null && showPlayerEntry)) {
            this.isPlayerSelected = true;
            this.selected = null;
            if (this.fakePlayer == null) {
                this.fakePlayer = new RemotePlayer(this.minecraft.level, this.minecraft.player.getGameProfile());
            }
            this.currentEntity = this.fakePlayer;
            this.isUnlocked = true;
            this.detailsScrollOffset = 0;
            this.chartScaleFactor = 1.0f;
            buildDetailsAndBarChartForPlayer(this.fakePlayer);
            return;
        }

        if (this.selected == null && !filteredVariants.isEmpty()) this.selected = filteredVariants.get(0);
        if (this.selected == null) return;

        ChangedEntity entity = (ChangedEntity) ChangedEntities.getCachedEntity(this.minecraft.level, this.selected.getEntityType());
        if (entity == null) {
            if (showPlayerEntry) selectPlayer();
            else if (!filteredVariants.isEmpty()) selectTf(filteredVariants.get(0));
            return;
        }

        this.isUnlocked = isVariantUnlocked(this.selected, this.minecraft.player);
        if (entity instanceof IBestiaryEntityData data) {
            EntityType<?> refType = data.getReferencedEntityType();
            if (refType != null && refType != entity.getType()) {
                Entity cached = ChangedEntities.getCachedEntity(this.minecraft.level, refType);
                if (cached instanceof ChangedEntity ce) entity = ce;
            }
        }

        this.currentEntity = entity;
        this.detailsScrollOffset = 0;
        this.chartScaleFactor = 1.0f;
        buildDetailsAndBarChart(entity);
    }

    private void setDisplayMode(DisplayMode mode) {
        this.displayMode = mode;
        updateSearchBoxBounds();
        updateSearchBoxVisibility();
        if (this.currentEntity instanceof ChangedEntity ce) buildDetailsAndBarChart(ce);
        else if (this.isPlayerSelected && this.fakePlayer != null) buildDetailsAndBarChartForPlayer(this.fakePlayer);
    }

    private void buildDetailsAndBarChartForPlayer(Player player) {
        loreLines.clear();
        attributeBars.clear();
        playerAttributeBars.clear();
        classificationText = "Human";

        int textWrapWidth = Math.max(100, getEffectiveDetW() - 16);
        loreLines.addAll(this.font.split(Component.literal("§e§lHuman Survivor"), textWrapWidth));
        loreLines.addAll(this.font.split(Component.literal("> The baseline standard human specimen prior to any latex assimilation or transfurmation effects."), textWrapWidth));

        AttributeSupplier playerDefaults = DefaultAttributes.getSupplier(EntityType.PLAYER);
        AttributeMap playerMap = player != null ? player.getAttributes() : new AttributeMap(playerDefaults);

        checkAndAddBar(playerMap, playerDefaults, Attributes.MAX_HEALTH, Component.translatable(Attributes.MAX_HEALTH.getDescriptionId()), 60.0f, 0xFFFF4444, 0xFFFF7777);
        checkAndAddBar(playerMap, playerDefaults, Attributes.ARMOR, Component.translatable(Attributes.ARMOR.getDescriptionId()), 20.0f, 0xFF55FFFF, 0xFFAAFFFF);
        checkAndAddBar(playerMap, playerDefaults, Attributes.MOVEMENT_SPEED, Component.translatable(Attributes.MOVEMENT_SPEED.getDescriptionId()), 0.20f, 0xFF55FF55, 0xFFAAFFAA);
        checkAndAddBar(playerMap, playerDefaults, Attributes.ATTACK_DAMAGE, Component.translatable(Attributes.ATTACK_DAMAGE.getDescriptionId()), 16.0f, 0xFFFFAA00, 0xFFFFDD55);
        checkAndAddBar(playerMap, playerDefaults, Attributes.ARMOR_TOUGHNESS, Component.translatable(Attributes.ARMOR_TOUGHNESS.getDescriptionId()), 12.0f, 0xFF88AAFF, 0xFFBBDDFF);
        checkAndAddBar(playerMap, playerDefaults, Attributes.KNOCKBACK_RESISTANCE, Component.translatable(Attributes.KNOCKBACK_RESISTANCE.getDescriptionId()), 1.0f, 0xFFCC66FF, 0xFFEE99FF);
        checkAndAddBar(playerMap, playerDefaults, Attributes.FOLLOW_RANGE, Component.translatable(Attributes.FOLLOW_RANGE.getDescriptionId()), 48.0f, 0xFFBBAA88, 0xFFDDCCAA);

        recalculateMaxScroll();
    }

    private void buildDetailsAndBarChart(ChangedEntity entity) {
        loreLines.clear();
        attributeBars.clear();
        playerAttributeBars.clear();
        classificationText = "";
        if (entity == null) return;

        int textWrapWidth = Math.max(100, getEffectiveDetW() - 16);

        if (!this.isUnlocked) {
            loreLines.addAll(this.font.split(Component.literal("§k" + Component.translatable("gui.changed_addon.bestiary.locked_description").getString()), textWrapWidth));
            AttributeSupplier playerDefaults = DefaultAttributes.getSupplier(EntityType.PLAYER);
            AttributeMap transformedMap = new AttributeMap(DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) entity.getType()));
            checkAndAddBar(transformedMap, playerDefaults, Attributes.MAX_HEALTH, Component.literal("???"), 0, 0xFFFF4444, 0xFF666666);
            checkAndAddBar(transformedMap, playerDefaults, Attributes.ATTACK_DAMAGE, Component.literal("???"), 0, 0xFF444444, 0xFF666666);
            checkAndAddBar(transformedMap, playerDefaults, Attributes.MOVEMENT_SPEED, Component.literal("???"), 0, 0xFF444444, 0xFF666666);
            this.maxDetailsScroll = Math.max(0, (24 + (loreLines.size() * 10) + 16 + (attributeBars.size() * 20)) - (this.dialogH - 40));
            return;
        }

        Player player = this.minecraft != null ? this.minecraft.player : null;
        if (player != null && this.selected != null) {
            for (Holder.Reference<BestiaryEntry> bestiaryHolderRaw : BestiaryEntriesManager.getHoldersForVariant(this.selected, player)) {
                Optional.ofNullable(bestiaryHolderRaw).ifPresent(holder -> {
                    BestiaryEntry entry = holder.value();
                    if (entry.title() != null && !entry.title().getString().isEmpty()) {
                        loreLines.addAll(this.font.split(Component.literal("§e").append(entry.title()), textWrapWidth));
                    }
                    if (entry.description() != null && !entry.description().getString().isEmpty()) {
                        String rawDesc = entry.description().getString();
                        if (rawDesc.startsWith("> ")) {
                            loreLines.addAll(this.font.split(Component.literal("§e┃ ").append(Component.literal(rawDesc.substring(2))), textWrapWidth - 8));
                        } else {
                            loreLines.addAll(this.font.split(entry.description(), textWrapWidth));
                        }
                    }
                });
            }
        }

        if (entity instanceof IBestiaryEntityData data) {
            for (IBestiaryEntityData.BestiaryInfo info : data.getBestiaryInfo().stream().sorted(Comparator.comparingInt(IBestiaryEntityData.BestiaryInfo::order)).toList()) {
                String titleStr = info.title().getString();
                if (titleStr.equalsIgnoreCase("Attributes") || titleStr.equalsIgnoreCase(Component.translatable("gui.changed_addon.bestiary.attributes").getString())) continue;
                if (titleStr.equalsIgnoreCase("Classification") || titleStr.toLowerCase().contains("class")) {
                    classificationText = info.description().getString();
                } else {
                    loreLines.addAll(this.font.split(Component.literal("§e").append(info.title()).append(": ").append(info.description()), textWrapWidth));
                }
            }
        }

        List<Component> subtitles = ChangedEntityUtil.getEntitySubtitle(entity);
        for (int i = 0; i < subtitles.size(); i++) {
            Component sub = subtitles.get(i);
            if (sub.getString().contains("Classification:") || sub.getString().contains(Component.translatable("gui.changed_addon.bestiary.classification_prefix").getString())) {
                if (classificationText.isEmpty() && i + 1 < subtitles.size()) {
                    classificationText = subtitles.get(i + 1).getString().replace("§f", "").trim();
                    i++;
                    continue;
                }
            }
            loreLines.addAll(this.font.split(sub, textWrapWidth));
        }

        if (loreLines.isEmpty()) loreLines.addAll(this.font.split(Component.translatable("gui.changed_addon.bestiary.lore.fallback"), textWrapWidth));

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
        int contentHeight = activeTab == 1 ? (24 + (loreLines.size() * 10)) : (activeTab == 2 ? (24 + (attributeBars.size() * 22) + (attributeBars.isEmpty() ? 0 : 115)) : 0);
        this.maxDetailsScroll = Math.max(0, contentHeight - (this.dialogH - 46));
    }

    private void checkAndAddBar(AttributeMap map, AttributeSupplier playerDefaults, Attribute attr, Component label, float maxScale, int fillColor, int highlightColor) {
        if (!map.hasAttribute(attr)) return;
        double value = map.getValue(attr);
        double base = playerDefaults.hasAttribute(attr) ? playerDefaults.getBaseValue(attr) : 0.0;
        if (attr == Attributes.MOVEMENT_SPEED && value > 0.5) value *= 0.1;

        double diff = value - base;
        String valText = attr == Attributes.MOVEMENT_SPEED ? String.format(Locale.ROOT, "%+.0f%%", base > 0 ? (value / base - 1.0) * 100.0 : 0.0) : (diff != 0.0 ? String.format(Locale.ROOT, "%.1f (%+.1f)", value, diff) : String.format(Locale.ROOT, "%.1f", value));

        float ratio = clamp((float) (value / maxScale), 0.05f, 1.0f);
        attributeBars.add(new AttributeBarItem(label, valText, ratio, fillColor, highlightColor));

        if (playerDefaults.hasAttribute(attr)) {
            double pVal = playerDefaults.getBaseValue(attr);
            playerAttributeBars.add(new AttributeBarItem(label, attr == Attributes.MOVEMENT_SPEED ? String.format(Locale.ROOT, "%.2f", pVal) : String.format(Locale.ROOT, "%.1f", pVal), clamp((float) (pVal / maxScale), 0.05f, 1.0f), 0xFF35A2FF, 0xFF70C5FF));
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
        boolean isEscHovered = mouseX >= escX - 2 && mouseX <= escX + escW + 2 && mouseY >= this.top + 5 && mouseY <= this.top + 18;
        graphics.drawString(this.font, escHint, escX, this.top + 7, isEscHovered ? 0xFFFFAA : 0x888888, false);

        int vpW = getEffectiveVpW(), detW = getEffectiveDetW();
        int vpX = this.left + 8, vpY = this.top + 22, vpH = this.dialogH - 30;
        int detX = (displayMode == DisplayMode.HIDE_MODEL) ? vpX : vpX + vpW + 6;
        int detY = this.top + 22, detH = this.dialogH - 30;

        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0) {
            int[] btnBounds = new int[4];
            BestiaryModelViewportRenderer.render(graphics, this.font, vpX, vpY, vpW, vpH, mouseX, mouseY, currentEntity, isUnlocked, modelYaw, modelPitch, modelZoom, modelOffsetX, modelOffsetY, displayMode == DisplayMode.MODEL_ONLY, btnBounds, () -> setUnlockedColor(graphics), () -> resetColor(graphics));
            lastExpandModelBtnX = btnBounds[0]; lastExpandModelBtnY = btnBounds[1]; lastExpandModelBtnW = btnBounds[2]; lastExpandModelBtnH = btnBounds[3];
        }

        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            renderDetailsTabs(graphics, detX, detY, detW, mouseX, mouseY);
            if (activeTab == 0) renderVariantGrid(graphics, detX, detY + 16, detW, detH - 16, mouseX, mouseY);
            else if (activeTab == 1) renderLoreTab(graphics, detX, detY + 16, detW, detH - 16, mouseX, mouseY);
            else renderAttributesGridTab(graphics, detX, detY + 16, detW, detH - 16, mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
        String screenWm = "§8© ParkaBird 2026";
        graphics.drawString(this.font, screenWm, this.width - this.font.width("© ParkaBird 2026") - 8, this.height - 13, 0x888888, false);
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
        int tabW = (w - 4) / 3, tabH = 14;
        Component[] titles = {
                Component.translatable("gui.changed_addon.bestiary.tab.entities"),
                Component.translatable("gui.changed_addon.bestiary.tab.lore"),
                Component.translatable("gui.changed_addon.bestiary.tab.stats")
        };
        for (int i = 0; i < 3; i++) {
            int tabX = x + (i * (tabW + 2));
            boolean isActive = (activeTab == i), isHovered = mouseX >= tabX && mouseX < tabX + tabW && mouseY >= y && mouseY < y + tabH;
            graphics.fill(tabX, y, tabX + tabW, y + tabH, isActive ? 0xFF282834 : (isHovered ? 0xFF1E1E26 : 0xFF121218));
            graphics.renderOutline(tabX, y, tabW, tabH, isActive ? 0xFFFFAA00 : (isHovered ? 0xFF585868 : 0xFF282832));
            graphics.drawString(this.font, titles[i], tabX + (tabW - this.font.width(titles[i])) / 2, y + 3, isActive ? 0xFFFFAA : (isHovered ? 0xFFE0E0E0 : 0x888888), false);
        }
    }

    private void renderVariantGrid(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);
        int gridY = y + 18, gridH = h - 18;
        boolean showPlayer = shouldShowPlayerInGrid();
        int totalCount = getTotalGridCount(), cellSize = (w - 8) / GRID_COLS;
        int rows = (int) Math.ceil(totalCount / (double) GRID_COLS), visibleRows = Math.max(1, (gridH - 4) / cellSize);

        gridRowScroll = clamp(gridRowScroll, 0, Math.max(0, rows - visibleRows));
        boolean scissored = false;
        if (w > 4 && gridH > 4) {
            graphics.enableScissor(x + 2, gridY + 2, x + w - 2, gridY + gridH - 2);
            scissored = true;
        }

        Component hoveredTooltip = null;
        try {
            int firstIndex = gridRowScroll * GRID_COLS;
            for (int i = firstIndex; i < totalCount; i++) {
                int row = i / GRID_COLS - gridRowScroll, col = i % GRID_COLS;
                if (row > visibleRows) break;
                int cellX = x + 3 + col * cellSize, cellY = gridY + 3 + row * cellSize, cellW = cellSize - 2, cellH = cellSize - 2;
                boolean isPlayerSlot = showPlayer && (i == 0);
                TransfurVariant<?> tf = isPlayerSlot ? null : filteredVariants.get(showPlayer ? i - 1 : i);
                boolean isSelected = isPlayerSlot ? this.isPlayerSelected : (!this.isPlayerSelected && this.selected == tf);
                boolean isHovered = mouseX >= cellX && mouseX < cellX + cellW && mouseY >= cellY && mouseY < cellY + cellH;

                if (isHovered) hoveredTooltip = isPlayerSlot ? getEntityDisplayNameComponent(fakePlayer) : getVariantDisplayNameComponent(tf);

                graphics.fill(cellX, cellY, cellX + cellW, cellY + cellH, isSelected ? 0xFF384458 : (isHovered ? 0xFF282830 : 0xFF1A1A20));
                graphics.renderOutline(cellX, cellY, cellW, cellH, isSelected ? 0xFFFFAA00 : (isHovered ? 0xFF585868 : 0xFF2A2A32));
                renderVariantIcon(graphics, tf, cellX + cellW / 2, cellY + cellH / 2, cellW - 6);
            }
        } finally {
            if (scissored) graphics.disableScissor();
        }

        if (rows > visibleRows && Math.max(0, rows - visibleRows) > 0) {
            int barX = x + w - 7, trackY = gridY + 3, trackH = gridH - 6;
            int thumbH = Math.max(14, (visibleRows * trackH) / rows);
            int thumbY = trackY + (int) ((float) gridRowScroll / Math.max(1, rows - visibleRows) * (trackH - thumbH));
            graphics.fill(barX, trackY, barX + 4, trackY + trackH, 0xFF101014);
            graphics.fill(barX, thumbY, barX + 4, thumbY + thumbH, isDraggingListScroll ? 0xFF8E8EA0 : 0xFF4E4E5C);
        }
        if (hoveredTooltip != null) graphics.renderTooltip(this.font, hoveredTooltip, mouseX, mouseY);
    }

    private void renderVariantIcon(GuiGraphics graphics, TransfurVariant<?> tf, int centerX, int centerY, float size) {
        if (this.minecraft == null || this.minecraft.level == null) {
            if (tf != null) renderInitialsFallback(graphics, tf, centerX, centerY);
            return;
        }

        if (tf == null) {
            Player renderPlayer = this.fakePlayer != null ? this.fakePlayer : this.minecraft.player;
            if (renderPlayer == null) return;
            float oldYBodyRot = renderPlayer.yBodyRot, oldYRot = renderPlayer.getYRot(), oldXRot = renderPlayer.getXRot(), oldYHeadRot = renderPlayer.yHeadRot, oldYHeadRotO = renderPlayer.yHeadRotO;
            float targetYaw = 180.0f + ICON_YAW;
            renderPlayer.yBodyRot = targetYaw; renderPlayer.setYRot(targetYaw); renderPlayer.setXRot(ICON_PITCH); renderPlayer.yHeadRot = targetYaw; renderPlayer.yHeadRotO = targetYaw;

            try {
                GuiUtils.renderEntityInInventory(graphics, centerX, centerY + (size / 2.0f) + ICON_Y_OFFSET + (renderPlayer.getPose() == Pose.CROUCHING ? -3.0f : 0.0f), Math.max(4.0f, (size * 0.48f) * 0.95f), new Quaternionf().rotateZ((float) Math.PI), null, renderPlayer);
            } catch (Exception ignored) {
            } finally {
                renderPlayer.yBodyRot = oldYBodyRot; renderPlayer.setYRot(oldYRot); renderPlayer.setXRot(oldXRot); renderPlayer.yHeadRot = oldYHeadRot; renderPlayer.yHeadRotO = oldYHeadRotO;
            }
            return;
        }

        Entity entity = ChangedEntities.getCachedEntity(this.minecraft.level, tf.getEntityType());
        if (!(entity instanceof ChangedEntity changedEntity)) {
            renderInitialsFallback(graphics, tf, centerX, centerY);
            return;
        }

        float oldYBodyRot = changedEntity.yBodyRot, oldYRot = changedEntity.getYRot(), oldXRot = changedEntity.getXRot(), oldYHeadRot = changedEntity.yHeadRot, oldYHeadRotO = changedEntity.yHeadRotO;
        float targetYaw = 180.0f + ICON_YAW;
        changedEntity.yBodyRot = targetYaw; changedEntity.setYRot(targetYaw); changedEntity.setXRot(ICON_PITCH); changedEntity.yHeadRot = targetYaw; changedEntity.yHeadRotO = targetYaw;

        boolean itemUnlocked = isVariantUnlocked(tf, this.minecraft.player);
        try {
            if (!itemUnlocked) setUnlockedColor(graphics);
            GuiUtils.renderEntityInInventory(graphics, centerX, centerY + (size / 2.0f) + ICON_Y_OFFSET, Math.max(4.0f, (size * 0.48f) * Math.min(1.0f, 1.8f / Math.max(0.1f, Math.max(changedEntity.getBbHeight(), changedEntity.getBbWidth()))) * changedEntity.getScale()), new Quaternionf().rotateZ((float) Math.PI), null, changedEntity);
            if (!itemUnlocked) resetColor(graphics);
        } catch (Exception e) {
            renderInitialsFallback(graphics, tf, centerX, centerY);
        } finally {
            changedEntity.yBodyRot = oldYBodyRot; changedEntity.setYRot(oldYRot); changedEntity.setXRot(oldXRot); changedEntity.yHeadRot = oldYHeadRot; changedEntity.yHeadRotO = oldYHeadRotO;
        }
    }

    private void renderInitialsFallback(GuiGraphics graphics, TransfurVariant<?> tf, int centerX, int centerY) {
        String name = getVariantDisplayName(tf);
        graphics.drawCenteredString(this.font, name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase(Locale.ROOT), centerX, centerY - 4, (name.hashCode() & 0x00FFFFFF) | 0xFF000000);
    }

    private void renderLoreTab(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);
        if (!this.isPlayerSelected && this.selected == null) return;

        boolean scissored = false;
        if (w > 6 && h > 6) {
            graphics.enableScissor(x + 3, y + 3, x + w - 3, y + h - 3);
            scissored = true;
        }
        try {
            int curY = y + 5 - detailsScrollOffset;
            graphics.drawString(this.font, Component.literal("§e§l").append(this.isPlayerSelected ? getEntityDisplayNameComponent(fakePlayer) : getVariantDisplayNameComponent(this.selected)), x + 6, curY, 0xFFFF55, false);
            curY += 12;

            if (this.isUnlocked && !classificationText.isEmpty()) {
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.lore.classification", classificationText), x + 6, curY, 0xAAAAAA, false);
                curY += 11;
            }

            graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
            curY += 5;
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.lore.dossier_header"), x + 6, curY, 0xFFAA00, false);

            int btnW = 85, btnH = 12, btnX = x + w - btnW - 10, btnY = curY - 2;
            lastToggleLoreExpandBtnX = btnX; lastToggleLoreExpandBtnY = btnY; lastToggleLoreExpandBtnW = btnW; lastToggleLoreExpandBtnH = btnH;
            boolean isHideModel = (displayMode == DisplayMode.HIDE_MODEL), isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;

            graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, isHideModel ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20));
            graphics.renderOutline(btnX, btnY, btnW, btnH, isHideModel ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32));
            Component btnText = Component.translatable(isHideModel ? "gui.changed_addon.bestiary.button.show_model" : "gui.changed_addon.bestiary.button.expand_text");
            graphics.drawString(this.font, btnText, btnX + (btnW - this.font.width(btnText)) / 2, btnY + 2, isHideModel ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

            curY += 11;
            for (FormattedCharSequence line : loreLines) {
                graphics.drawString(this.font, line, x + 6, curY, 0xD0D0D0, false);
                curY += 10;
            }
        } finally {
            if (scissored) graphics.disableScissor();
        }
        renderDetailsScrollbar(graphics, x, y, w, h, mouseX, mouseY);
    }

    private void renderAttributesGridTab(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        renderInsetBox(graphics, x, y, w, h);
        if (!this.isPlayerSelected && this.selected == null) return;

        boolean scissored = false;
        if (w > 6 && h > 6) {
            graphics.enableScissor(x + 3, y + 3, x + w - 3, y + h - 3);
            scissored = true;
        }
        try {
            int curY = y + 5 - detailsScrollOffset;
            graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.attribute_sheet_header"), x + 6, curY, 0x55FF55, false);
            curY += 14;

            int barW = w - 16, barH = 6;
            for (AttributeBarItem bar : attributeBars) {
                graphics.drawString(this.font, bar.label, x + 6, curY, 0xE0E0E0, false);
                graphics.drawString(this.font, bar.valueText, x + 6 + barW - this.font.width(bar.valueText), curY, 0xFFFFAA, false);
                curY += 10;

                graphics.fill(x + 6, curY, x + 6 + barW, curY + barH, 0xFF141416);
                graphics.renderOutline(x + 6, curY, barW, barH, 0xFF303038);
                int fillW = Math.max(2, Math.round((barW - 2) * bar.ratio));
                graphics.fill(x + 7, curY + 1, x + 7 + fillW, curY + barH - 1, bar.fillColor);
                curY += barH + 6;
            }

            if (!attributeBars.isEmpty()) {
                curY += 4;
                graphics.fill(x + 5, curY, x + w - 5, curY + 1, 0xFF353540);
                curY += 10;
                graphics.drawString(this.font, Component.translatable("gui.changed_addon.bestiary.radar_chart_header"), x + 6, curY, 0x55FFFF, false);

                int btnW = 92, btnH = 12, btnX = x + w - btnW - 10, btnY = curY - 2;
                lastPlayerBtnX = btnX; lastPlayerBtnY = btnY; lastPlayerBtnW = btnW; lastPlayerBtnH = btnH;
                boolean isBtnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
                graphics.fill(btnX, btnY, btnX + btnW, btnY + btnH, showPlayerChart ? 0xFF2A4438 : (isBtnHovered ? 0xFF282830 : 0xFF1A1A20));
                graphics.renderOutline(btnX, btnY, btnW, btnH, showPlayerChart ? 0xFF55FF55 : (isBtnHovered ? 0xFF585868 : 0xFF2A2A32));
                Component btnText = Component.translatable(showPlayerChart ? "gui.changed_addon.bestiary.button.hide_player_overlay" : "gui.changed_addon.bestiary.button.show_player_overlay");
                graphics.drawString(this.font, btnText, btnX + (btnW - this.font.width(btnText)) / 2, btnY + 2, showPlayerChart ? 0x55FF55 : (isBtnHovered ? 0xFFFFFF : 0xAAAAAA), false);

                curY += 14;
                Component scaleStr = Component.translatable("gui.changed_addon.bestiary.scale", String.format(Locale.ROOT, "%.1f", chartScaleFactor));
                graphics.drawString(this.font, scaleStr, x + w - this.font.width(scaleStr) - 10, curY, 0x888888, false);
                curY += 10;

                int[] bounds = new int[3];
                BestiaryRadarChartRenderer.render(graphics, this.font, x + (w / 2), curY + 36, 32, attributeBars, showPlayerChart ? playerAttributeBars : null, chartScaleFactor, mouseX, mouseY, this.hoveredRadarTooltip, bounds);
                lastChartCenterX = bounds[0]; lastChartCenterY = bounds[1]; lastChartRadius = bounds[2];
            }
        } finally {
            if (scissored) graphics.disableScissor();
        }
        renderDetailsScrollbar(graphics, x, y, w, h, mouseX, mouseY);
        if (!this.hoveredRadarTooltip.isEmpty()) graphics.renderComponentTooltip(this.font, this.hoveredRadarTooltip, mouseX, mouseY);
    }

    private void renderDetailsScrollbar(GuiGraphics graphics, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (maxDetailsScroll > 0) {
            int barX = x + w - 7, trackY = y + 3, trackH = h - 6;
            int thumbH = Math.max(14, (trackH * trackH) / (trackH + maxDetailsScroll));
            int thumbY = trackY + (int) ((float) detailsScrollOffset / maxDetailsScroll * (trackH - thumbH));
            graphics.fill(barX, trackY, barX + 4, trackY + trackH, 0xFF101014);
            graphics.fill(barX, thumbY, barX + 4, thumbY + thumbH, isDraggingDetailsScroll ? 0xFF8E8EA0 : 0xFF4E4E5C);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int totalCount = getTotalGridCount();
        if (this.activeTab == 0 && displayMode != DisplayMode.MODEL_ONLY && totalCount > 0) {
            boolean showPlayer = shouldShowPlayerInGrid();
            int currentIndex = this.isPlayerSelected ? 0 : (selected != null ? filteredVariants.indexOf(selected) + (showPlayer ? 1 : 0) : -1);
            if (keyCode == GLFW.GLFW_KEY_LEFT) { navigateSelection(currentIndex - 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_RIGHT) { navigateSelection(currentIndex + 1); return true; }
            if (keyCode == GLFW.GLFW_KEY_UP) { navigateSelection(currentIndex - GRID_COLS); return true; }
            if (keyCode == GLFW.GLFW_KEY_DOWN) { navigateSelection(currentIndex + GRID_COLS); return true; }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void navigateSelection(int targetIndex) {
        int totalCount = getTotalGridCount();
        if (totalCount == 0) return;
        int clampedIndex = clamp(targetIndex, 0, totalCount - 1);
        boolean showPlayer = shouldShowPlayerInGrid();
        if (showPlayer && clampedIndex == 0) selectPlayer();
        else selectTf(filteredVariants.get(showPlayer ? clampedIndex - 1 : clampedIndex));

        int targetRow = clampedIndex / GRID_COLS, visibleRows = Math.max(1, ((this.dialogH - 30) - 34) / ((getEffectiveDetW() - 8) / GRID_COLS));
        if (targetRow < gridRowScroll) gridRowScroll = targetRow;
        else if (targetRow >= gridRowScroll + visibleRows) gridRowScroll = targetRow - visibleRows + 1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int vpW = getEffectiveVpW(), detW = getEffectiveDetW();
        int vpX = this.left + 8, vpY = this.top + 22, vpH = this.dialogH - 30;
        int detX = (displayMode == DisplayMode.HIDE_MODEL) ? vpX : vpX + vpW + 6, detY = this.top + 22;

        Component escHint = Component.translatable("gui.changed_addon.bestiary.esc_close");
        int escW = this.font.width(escHint), escX = this.left + this.dialogW - escW - 10;
        if (button == 0 && mouseX >= escX - 4 && mouseX <= escX + escW + 4 && mouseY >= this.top + 4 && mouseY <= this.top + 16) {
            this.onClose();
            return true;
        }

        if (displayMode != DisplayMode.HIDE_MODEL && lastExpandModelBtnX != -1 && button == 0 && mouseX >= lastExpandModelBtnX && mouseX <= lastExpandModelBtnX + lastExpandModelBtnW && mouseY >= lastExpandModelBtnY && mouseY <= lastExpandModelBtnY + lastExpandModelBtnH) {
            setDisplayMode(displayMode == DisplayMode.MODEL_ONLY ? DisplayMode.NORMAL : DisplayMode.MODEL_ONLY);
            return true;
        }

        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            int tabW = (detW - 4) / 3, tabH = 14;
            for (int i = 0; i < 3; i++) {
                int tabX = detX + (i * (tabW + 2));
                if (button == 0 && mouseX >= tabX && mouseX < tabX + tabW && mouseY >= detY && mouseY < detY + tabH) {
                    this.activeTab = i;
                    this.detailsScrollOffset = 0;
                    if (displayMode == DisplayMode.HIDE_MODEL && activeTab != 1) setDisplayMode(DisplayMode.NORMAL);
                    else { updateSearchBoxVisibility(); recalculateMaxScroll(); }
                    return true;
                }
            }
            if (activeTab == 1 && lastToggleLoreExpandBtnX != -1 && button == 0 && mouseX >= lastToggleLoreExpandBtnX && mouseX <= lastToggleLoreExpandBtnX + lastToggleLoreExpandBtnW && mouseY >= lastToggleLoreExpandBtnY && mouseY <= lastToggleLoreExpandBtnY + lastToggleLoreExpandBtnH) {
                setDisplayMode(displayMode == DisplayMode.HIDE_MODEL ? DisplayMode.NORMAL : DisplayMode.HIDE_MODEL);
                return true;
            }
            if (activeTab == 2 && lastPlayerBtnX != -1 && button == 0 && mouseX >= lastPlayerBtnX && mouseX <= lastPlayerBtnX + lastPlayerBtnW && mouseY >= lastPlayerBtnY && mouseY <= lastPlayerBtnY + lastPlayerBtnH) {
                showPlayerChart = !showPlayerChart;
                recalculateMaxScroll();
                return true;
            }
        }

        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0 && mouseX >= vpX && mouseX <= vpX + vpW && mouseY >= vpY && mouseY <= vpY + vpH) {
            if (button == 0) { isDraggingModel = true; return true; }
            if (button == 1 && currentEntity != null) { currentEntity.setPose(currentEntity.getPose() == Pose.STANDING ? Pose.CROUCHING : Pose.STANDING); return true; }
        }

        if (displayMode != DisplayMode.MODEL_ONLY && activeTab == 0 && mouseX >= detX && mouseX < detX + detW && mouseY >= detY + 16 && mouseY < detY + (this.dialogH - 30)) {
            int cellSize = (detW - 8) / GRID_COLS, firstIndex = gridRowScroll * GRID_COLS, totalCount = getTotalGridCount();
            for (int i = firstIndex; i < totalCount; i++) {
                int row = i / GRID_COLS - gridRowScroll, col = i % GRID_COLS;
                int cellX = detX + 3 + col * cellSize, cellY = detY + 16 + 18 + 3 + row * cellSize;
                if (mouseX >= cellX && mouseX < cellX + cellSize - 2 && mouseY >= cellY && mouseY < cellY + cellSize - 2) {
                    if (shouldShowPlayerInGrid() && i == 0) selectPlayer();
                    else selectTf(filteredVariants.get(shouldShowPlayerInGrid() ? i - 1 : i));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDraggingModel = false; isDraggingModelOffset = false; isDraggingListScroll = false; isDraggingDetailsScroll = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingModel) {
            modelYaw += (float) (dragX * 1.5);
            modelPitch = clamp(modelPitch - (float) (dragY * 1.5), -45.0f, 45.0f);
            return true;
        }
        if (maxDetailsScroll > 0 && isDraggingDetailsScroll && activeTab != 0 && displayMode != DisplayMode.MODEL_ONLY) {
            detailsScrollOffset = clamp((int) ((mouseY - (this.top + 38)) / ((this.dialogH - 36)) * maxDetailsScroll), 0, maxDetailsScroll);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int vpW = getEffectiveVpW(), detW = getEffectiveDetW();
        if (displayMode != DisplayMode.HIDE_MODEL && vpW > 0 && mouseX >= this.left + 8 && mouseX <= this.left + 8 + vpW && mouseY >= this.top + 22 && mouseY <= this.top + 22 + (this.dialogH - 30)) {
            modelZoom = clamp(modelZoom + (float) (delta * 3.0), 20.0f, 95.0f);
            return true;
        }
        if (displayMode != DisplayMode.MODEL_ONLY && detW > 0) {
            if (activeTab == 0) {
                gridRowScroll = clamp(gridRowScroll - (int) Math.signum(delta), 0, Math.max(0, (int) Math.ceil(getTotalGridCount() / (double) GRID_COLS) - Math.max(1, ((this.dialogH - 30) - 34) / ((detW - 8) / GRID_COLS))));
                return true;
            }
            if (activeTab == 2 && lastChartCenterX != -1 && Math.hypot(mouseX - lastChartCenterX, mouseY - lastChartCenterY) <= lastChartRadius + 12) {
                chartScaleFactor = clamp(chartScaleFactor + (float) (delta * 0.15f), 0.4f, 3.0f);
                return true;
            }
            if (maxDetailsScroll > 0) {
                detailsScrollOffset = clamp(detailsScrollOffset - (int) Math.signum(delta) * 12, 0, maxDetailsScroll);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}