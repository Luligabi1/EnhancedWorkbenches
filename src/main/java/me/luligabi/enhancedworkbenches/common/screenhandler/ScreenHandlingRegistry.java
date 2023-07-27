package me.luligabi.enhancedworkbenches.common.screenhandler;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class ScreenHandlingRegistry {

    public static final ScreenHandlerType<ProjectTableScreenHandler> PROJECT_TABLE_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER,
            EnhancedWorkbenches.id("project_table"),
            new ScreenHandlerType<>(
                    ProjectTableScreenHandler::new
            )
    );

    public static final ScreenHandlerType<CraftingStationScreenHandler> CRAFTING_STATION_SCREEN_HANDLER = Registry.register(
            Registry.SCREEN_HANDLER,
            EnhancedWorkbenches.id("crafting_station"),
            new ScreenHandlerType<>(
                    CraftingStationScreenHandler::new
            )
    );

    public static void init() {
        // NO-OP
    }

    private ScreenHandlingRegistry() {
        // NO-OP
    }
}