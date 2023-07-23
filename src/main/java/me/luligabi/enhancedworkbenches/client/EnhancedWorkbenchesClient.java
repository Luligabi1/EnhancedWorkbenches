package me.luligabi.enhancedworkbenches.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class EnhancedWorkbenchesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, ProjectTableScreen::new);
        HandledScreens.register(ScreenHandlingRegistry.CRAFTING_STATION_SCREEN_HANDLER, CraftingStationScreen::new);

        BlockEntityRendererFactories.register(BlockRegistry.CRAFTING_STATION_ENTITY_TYPE, CraftingStationBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockRegistry.PROJECT_TABLE_ENTITY_TYPE, ProjectTableBlockEntityRenderer::new);
    }

    private static ClientConfig createConfig() {
        ClientConfig finalConfig;
        LOGGER.info("Trying to read config file...");
        try {
            if(CONFIG_FILE.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...");
                writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(new ClientConfig()))));
                finalConfig = new ClientConfig();
                LOGGER.info("Successfully created default config file.");
            } else {
                LOGGER.info("A config file was found, loading it..");
                finalConfig = GSON.fromJson(new String(Files.readAllBytes(CONFIG_FILE.toPath())), ClientConfig.class);
                if(finalConfig == null) {
                    throw new NullPointerException("The config file was empty.");
                } else {
                    LOGGER.info("Successfully loaded config file.");
                }
            }
        } catch(Exception e) {
            LOGGER.error("There was an error creating/loading the config file!", e);
            finalConfig = new ClientConfig();
            LOGGER.warn("Defaulting to original config.");
        }
        return finalConfig;
    }

    public static void saveConfig(ClientConfig modConfig) {
        try {
            writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(modConfig))));
            LOGGER.info("Saved new config file.");
        } catch(Exception e) {
            LOGGER.error("There was an error saving the config file!", e);
        }
    }

    private static void writeConfig(String json) {
        try(PrintWriter printWriter = new PrintWriter(CONFIG_FILE)) {
            printWriter.write(json);
            printWriter.flush();
        } catch(IOException e) {
            LOGGER.error("Failed to write config file", e);
        }
    }

    public static final Logger LOGGER;

    private static final Gson GSON;
    private static final File CONFIG_FILE;
    public static final ClientConfig CLIENT_CONFIG;

    static {
        LOGGER = LoggerFactory.getLogger("Project Table Mod");

        GSON = new GsonBuilder().setPrettyPrinting().create();
        CONFIG_FILE = new File(String.format("%s%sprojecttablemod-client.json", FabricLoader.getInstance().getConfigDir(), File.separator));
        CLIENT_CONFIG = createConfig();
    }
}
