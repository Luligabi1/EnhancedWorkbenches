package me.luligabi.enhancedworkbenches.client;

import me.luligabi.enhancedworkbenches.client.renderer.CraftingStationBlockEntityRenderer;
import me.luligabi.enhancedworkbenches.client.renderer.ProjectTableBlockEntityRenderer;
import me.luligabi.enhancedworkbenches.client.screen.CraftingStationScreen;
import me.luligabi.enhancedworkbenches.client.screen.ProjectTableScreen;
import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedWorkbenchesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, ProjectTableScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.CRAFTING_STATION_SCREEN_HANDLER, CraftingStationScreen::new);

        BlockEntityRendererFactories.register(BlockRegistry.CRAFTING_STATION_ENTITY_TYPE, CraftingStationBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockRegistry.PROJECT_TABLE_ENTITY_TYPE, ProjectTableBlockEntityRenderer::new);
    }

    public static final Logger LOGGER;

    public static final ClientConfig CLIENT_CONFIG;

    static {
        LOGGER = LoggerFactory.getLogger("Enhanced Workbenches");

        ClientConfig.HANDLER.load();
        CLIENT_CONFIG = ClientConfig.HANDLER.instance();

        if (FabricLoader.getInstance().isModLoaded("craftingtweaks")) {
            try {
                Class.forName("me.luligabi.enhancedworkbenches.client.compat.craftingtweaks.ProjectTableCraftingGridProvider").getConstructor().newInstance();
                Class.forName("me.luligabi.enhancedworkbenches.client.compat.craftingtweaks.CraftingStationCraftingGridProvider").getConstructor().newInstance();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
