package me.luligabi.enhancedworkbenches.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ProjectTableScreen extends HandledScreen<ProjectTableScreenHandler> {

    public ProjectTableScreen(ProjectTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        backgroundHeight = 208;
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        playerInventoryTitleY = 114;
        x = width / 2 - backgroundWidth / 2;
        y = height / 2 - backgroundHeight / 2;
    }

    @Override
    public void render(MatrixStack ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        drawMouseoverTooltip(ctx, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack ctx, float delta, int mouseX, int mouseY) {
        renderBackground(ctx);
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        ctx.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
//        port to 1.19.2
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(ctx, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    private static final Identifier TEXTURE = EnhancedWorkbenches.id("textures/gui/project_table.png");
}
