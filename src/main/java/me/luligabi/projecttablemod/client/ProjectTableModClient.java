package me.luligabi.projecttablemod.client;

import me.luligabi.projecttablemod.client.renderer.CraftingStationBlockEntityRenderer;
import me.luligabi.projecttablemod.client.renderer.ProjectTableBlockEntityRenderer;
import me.luligabi.projecttablemod.client.screen.CraftingStationScreen;
import me.luligabi.projecttablemod.client.screen.ProjectTableScreen;
import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.luligabi.projecttablemod.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ProjectTableModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, ProjectTableScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.CRAFTING_STATION_SCREEN_HANDLER, CraftingStationScreen::new);

        BlockEntityRendererFactories.register(BlockRegistry.CRAFTING_STATION_ENTITY_TYPE, CraftingStationBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockRegistry.PROJECT_TABLE_ENTITY_TYPE, ProjectTableBlockEntityRenderer::new);
    }
}
