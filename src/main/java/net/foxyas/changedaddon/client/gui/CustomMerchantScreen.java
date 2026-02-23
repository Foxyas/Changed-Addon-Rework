package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.menu.CustomMerchantMenu;
import net.foxyas.changedaddon.menu.CustomMerchantOffer;
import net.foxyas.changedaddon.menu.CustomMerchantOffers;
import net.foxyas.changedaddon.network.packet.ServerboundCustomSelectTradePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomMerchantScreen extends AbstractContainerScreen<CustomMerchantMenu> {

    /**
     * The GUI texture for the villager merchant GUI.
     */
    private static final ResourceLocation VILLAGER_LOCATION = ResourceLocation.parse("textures/gui/container/villager2.png");
    private static final Component TRADES_LABEL = Component.translatable("merchant.trades");
    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");
    private static final int MS_PER_ITEM = 1000;
    private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[7];
    /**
     * The integer value corresponding to the currently selected merchant recipe.
     */
    private int shopItem;
    private int scrollOff;
    private boolean isDragging;

    public CustomMerchantScreen(CustomMerchantMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        imageWidth = 276;
        inventoryLabelX = 107;
    }

    private static ItemStack currentStack(Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getItems();
        if (stacks.length == 0) {
            return ItemStack.EMPTY;
        }
        int time = Math.toIntExact(System.currentTimeMillis() % ((long) stacks.length * MS_PER_ITEM));
        return stacks[time / MS_PER_ITEM];
    }

    protected void init() {
        super.init();
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        int k = j + 16 + 2;

        for (int l = 0; l < 7; ++l) {
            tradeOfferButtons[l] = addRenderableWidget(new TradeOfferButton(i + 5, k, l, (button) -> {
                if (button instanceof TradeOfferButton) {
                    shopItem = ((TradeOfferButton) button).getIndex() + scrollOff;
                    menu.setSelectionHint(shopItem);
                    menu.tryMoveItems(shopItem);
                    ChangedAddonMod.PACKET_HANDLER.sendToServer(new ServerboundCustomSelectTradePacket(shopItem));
                }
            }));
            k += 20;
        }
    }

    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.drawString(font, title, 49 + imageWidth / 2 - font.width(title) / 2, 6, 4210752, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752, false);
        int l = font.width(TRADES_LABEL);
        guiGraphics.drawString(font, TRADES_LABEL, (5 - l / 2 + 48), 6, 4210752, false);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(VILLAGER_LOCATION, i, j, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
        CustomMerchantOffers merchantoffers = this.menu.getOffers();
        if (!merchantoffers.isEmpty()) {
            int k = this.shopItem;
            if (k < 0 || k >= merchantoffers.size()) {
                return;
            }

            CustomMerchantOffer merchantoffer = merchantoffers.get(k);
            if (merchantoffer.isOutOfStock()) {
                pGuiGraphics.blit(VILLAGER_LOCATION, this.leftPos + 83 + 99, this.topPos + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
            }
        }

    }

    private void renderScroller(GuiGraphics pGuiGraphics, int pPosX, int pPosY, CustomMerchantOffers pMerchantOffers) {
        int i = pMerchantOffers.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }

            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 94, pPosY + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 94, pPosY + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }

    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
        CustomMerchantOffers offers = menu.getOffers();
        if (offers.isEmpty()) {
            renderTooltip(pGuiGraphics, mouseX, mouseY);
            return;
        }

        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        int k = j + 16 + 1;
        int l = i + 5 + 5;
        renderScroller(pGuiGraphics, i, j, offers);
        int i1 = 0;

        Ingredient ingredient;
        ItemStack cost, result;
        for (CustomMerchantOffer offer : offers) {
            if (!canScroll(offers.size()) || (i1 >= scrollOff && i1 < 7 + scrollOff)) {
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);

                int j1 = k + 2;

                ingredient = offer.getCostA();
                cost = currentStack(ingredient);
                pGuiGraphics.renderFakeItem(cost, l, j1);
                pGuiGraphics.renderItemDecorations(font, cost, l, j1);

                ingredient = offer.getCostB();
                if (!ingredient.isEmpty()) {
                    cost = currentStack(ingredient);
                    pGuiGraphics.renderFakeItem(cost, i + 5 + 35, j1);
                    pGuiGraphics.renderItemDecorations(font, cost, i + 5 + 35, j1);
                }

                result = offer.getResult();
                renderButtonArrows(pGuiGraphics, offer, i, j1);

                String str = String.valueOf(offer.getUsesLeft());
                pGuiGraphics.drawString(font, str, i + 5 + (46 + 5) + 19 - 2 - font.width(str), (float) (j1 + 6 + 3), 16777215, false);

                pGuiGraphics.renderFakeItem(result, i + 5 + 68, j1);
                pGuiGraphics.renderItemDecorations(this.font, result, i + 5 + 68, j1);

                pGuiGraphics.pose().popPose();
                k += 20;
                ++i1;
            }

            ++i1;
        }

        int k1 = shopItem;
        CustomMerchantOffer merchantoffer1 = offers.get(k1);

        if (merchantoffer1.isOutOfStock() && isHovering(186, 35, 22, 21, mouseX, mouseY) && menu.canRestock()) {
            pGuiGraphics.renderTooltip(this.font, DEPRECATED_TOOLTIP, mouseX, mouseY);
        }

        for (TradeOfferButton button : tradeOfferButtons) {
            if (button.isHoveredOrFocused()) {
                button.renderToolTip(pGuiGraphics, mouseX, mouseY);
            }
            button.visible = button.index < menu.getOffers().size();
        }

        RenderSystem.enableDepthTest();

        renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    private void renderButtonArrows(GuiGraphics pGuiGraphics, CustomMerchantOffer pMerchantOffers, int pPosX, int pPosY) {
        RenderSystem.enableBlend();
        if (pMerchantOffers.isOutOfStock()) {
            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 5 + 35 + 20, pPosY + 3, 0, 25.0F, 171.0F, 10, 9, 512, 256);
        } else {
            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 5 + 35 + 20, pPosY + 3, 0, 15.0F, 171.0F, 10, 9, 512, 256);
        }

    }

    private boolean canScroll(int numOffers) {
        return numOffers > 7;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = menu.getOffers().size();
        if (canScroll(i)) {
            int j = i - 7;
            scrollOff = Mth.clamp((int) ((double) scrollOff - delta), 0, j);
        }

        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            int j = topPos + 18;
            int k = j + 139;
            int l = menu.getOffers().size() - 7;
            float f = ((float) mouseY - (float) j - 13.5F) / ((float) (k - j) - 27.0F);
            f = f * (float) l + 0.5F;
            scrollOff = Mth.clamp((int) f, 0, l);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        isDragging = false;
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;
        if (canScroll(menu.getOffers().size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
            isDragging = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    class TradeOfferButton extends Button {

        final int index;

        public TradeOfferButton(int x, int y, int index, OnPress onPress) {
            super(x, y, 89, 20, Component.empty(), onPress, Supplier::get);
            this.index = index;
            visible = false;
        }

        public int getIndex() {
            return index;
        }

        public void renderToolTip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            if (this.isHovered && CustomMerchantScreen.this.menu.getOffers().size() > this.index + CustomMerchantScreen.this.scrollOff) {
                if (pMouseX < this.getX() + 20) {
                    ItemStack itemstack = currentStack(CustomMerchantScreen.this.menu.getOffers().get(this.index + CustomMerchantScreen.this.scrollOff).getCostA());
                    pGuiGraphics.renderTooltip(CustomMerchantScreen.this.font, itemstack, pMouseX, pMouseY);
                } else if (pMouseX < this.getX() + 50 && pMouseX > this.getX() + 30) {
                    ItemStack itemstack2 = currentStack(CustomMerchantScreen.this.menu.getOffers().get(this.index + CustomMerchantScreen.this.scrollOff).getCostB());
                    if (!itemstack2.isEmpty()) {
                        pGuiGraphics.renderTooltip(CustomMerchantScreen.this.font, itemstack2, pMouseX, pMouseY);
                    }
                } else if (pMouseX > this.getX() + 65) {
                    ItemStack itemstack1 = CustomMerchantScreen.this.menu.getOffers().get(this.index + CustomMerchantScreen.this.scrollOff).getResult();
                    pGuiGraphics.renderTooltip(CustomMerchantScreen.this.font, itemstack1, pMouseX, pMouseY);
                }
            }

        }
    }
}
