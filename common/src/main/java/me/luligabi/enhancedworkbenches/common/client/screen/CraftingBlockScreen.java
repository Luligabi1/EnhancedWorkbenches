package me.luligabi.enhancedworkbenches.common.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class CraftingBlockScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public CraftingBlockScreen(T abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, inventory, title);
    }

    @Override
    protected void init() {
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    protected final void setCoordinates() {
        leftPos = width / 2 - imageWidth / 2;
        topPos = height / 2 - imageHeight / 2;
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        super.render(gui, mouseX, mouseY, delta);
        renderTooltip(gui, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int i, int j) {
        gui.blit(getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    protected abstract ResourceLocation getBackgroundTexture();
}