package me.luligabi.enhancedworkbenches.common.client.screen;

import me.luligabi.enhancedworkbenches.common.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.common.block.projecttable.ProjectTableBlockEntity;
import me.luligabi.enhancedworkbenches.common.common.menu.ProjectTableMenu;
import me.luligabi.enhancedworkbenches.common.common.util.ProjectTableRecipeHistory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProjectTableScreen extends CraftingBlockScreen<ProjectTableMenu> implements RecipeUpdateListener {

    @NotNull
    private final ProjectTableBlockEntity blockEntity;
    private int recipeHistoryX;
    private int recipeHistoryY;
    private int recipeHistoryTitleX;
    private final Component recipeHistoryTitle = Component.translatable("title.enhancedworkbenches.history");
    private final ProjectTableRecipeBookComponent recipeBookComponent = new ProjectTableRecipeBookComponent();

    public ProjectTableScreen(ProjectTableMenu abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, inventory, title);
        blockEntity = (ProjectTableBlockEntity) inventory.player.level().getBlockEntity(menu.clientPos);
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return EnhancedWorkbenches.id("textures/gui/project_table/project_table.png");
    }

    @Override
    protected void init() {
        super.init();
        imageHeight = 208;
        inventoryLabelY = 114;
        setCoordinates();
        recipeHistoryX = leftPos - 60;
        recipeHistoryY = topPos + 14;
        recipeHistoryTitleX = ((-72 - font.width(recipeHistoryTitle)) / 2);
        recipeBookComponent.init(width, height, minecraft, true, menu);
        addWidget(recipeBookComponent);
        recipeBookComponent.toggleVisibility();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        super.render(gui, mouseX, mouseY, delta);
        recipeBookComponent.renderGhostRecipe(gui, leftPos, topPos, true, delta);
        recipeBookComponent.renderTooltip(gui, leftPos, topPos, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float delta, int mouseX, int mouseY) {
        super.renderBg(gui, delta, mouseX, mouseY);
        gui.blit(RECIPE_HISTORY_BG, leftPos - 68, topPos, 0, 0, 64, 79, 64, 79);
        renderRecipeHistory(gui, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        super.renderLabels(guiGraphics, i, j);
        guiGraphics.drawString(font, recipeHistoryTitle, recipeHistoryTitleX, titleLabelY, 0x404040, false);
    }

    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderTooltip(gui, mouseX, mouseY);

        for(int i = 0; i < blockEntity.recipeHistory.size(); ++i) {
            ProjectTableRecipeHistory.RecipeHistoryEntry entry = blockEntity.recipeHistory.list.get(i);
            if(entry == null) continue;
            int p = recipeHistoryX + i % 3 * 16;
            int q = recipeHistoryY + i / 3 * 18 + 2;
            if(mouseX >= p && mouseX < p + 16 && mouseY >= q && mouseY < q + 18) {
                ItemStack stack = entry.toRecipeHolder(minecraft.level).value().getResultItem(minecraft.level.registryAccess());
                List<Component> text = List.of(
                    stack.getHoverName(),
                    Component.translatable(
                        entry.isLocked() ?
                            "tooltip.enhancedworkbenches.history.unlock" :
                            "tooltip.enhancedworkbenches.history.lock"
                    ).withStyle(ChatFormatting.GRAY)
                );
                gui.renderComponentTooltip(font, text, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int j) {
        for(int i = 0; i < blockEntity.recipeHistory.size(); ++i) {
            if(blockEntity.recipeHistory.get(i) == null) continue;
            double widthRange = mouseX - (double)(recipeHistoryX + i % 3 * 16);
            double heightRange = mouseY - (double)(recipeHistoryY + i / 3 * 18);
            int index = Screen.hasControlDown() ? i + 10 : i;
            if(widthRange >= 0.0 && heightRange >= 0.0 && widthRange < 16.0 && heightRange < 18.0 && menu.clickMenuButton(minecraft.player, index)) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, index);
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                if(index >= 0 && index <= 8) {
                    recipeBookComponent.clearGhostRecipe();
                    minecraft.player.connection.send(new ServerboundPlaceRecipePacket(menu.containerId, blockEntity.recipeHistory.list.get(i).toRecipeHolder(minecraft.player.level()), Screen.hasShiftDown()));
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, j);
    }

    @Override
    protected void slotClicked(Slot slot, int i, int j, ClickType clickType) {
        super.slotClicked(slot, i, j, clickType);
        recipeBookComponent.slotClicked(slot);
    }

    private void renderRecipeHistory(GuiGraphics gui, int mouseX, int mouseY) {
        ArrayList<ProjectTableRecipeHistory.RecipeHistoryEntry> list = blockEntity.recipeHistory.list;
        for(int i = 0; i < list.size(); ++i) {
            ProjectTableRecipeHistory.RecipeHistoryEntry entry = list.get(i);
            if(entry == null) continue;
            int p = recipeHistoryX + i % 3 * 16;
            int q = i / 3;
            int r = recipeHistoryY + q * 18 + 4;
            ResourceLocation resourceLocation = RECIPE_SPRITE;
            if(mouseX >= p && mouseY >= r && mouseX < p + 16 && mouseY < r + 18) {
                resourceLocation = RECIPE_HIGHLIGHTED_SPRITE;
            }

            gui.blitSprite(resourceLocation, p, r - 1, 16, 18);
            gui.renderItem(entry.toRecipeHolder(minecraft.level).value().getResultItem(minecraft.level.registryAccess()), p, r);
            if(entry.isLocked()) {
                gui.pose().pushPose();
                gui.pose().translate(0F, 0F, 400F);
                gui.blitSprite(RECIPE_LOCKED_OVERLAY_SPRITE, p, r - 1, 16, 18);
                gui.pose().popPose();
            }
        }
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return recipeBookComponent;
    }

    @Override
    public void recipesUpdated() {
    }

    private static final ResourceLocation RECIPE_HISTORY_BG = EnhancedWorkbenches.id("textures/gui/project_table/recipe_history.png");
    private static final ResourceLocation RECIPE_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe");
    private static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("container/stonecutter/recipe_highlighted");
    private static final ResourceLocation RECIPE_LOCKED_OVERLAY_SPRITE = EnhancedWorkbenches.id("container/project_table/recipe_locked_overlay");

}