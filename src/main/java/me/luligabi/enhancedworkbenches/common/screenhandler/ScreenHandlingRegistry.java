package me.luligabi.enhancedworkbenches.common.screenhandler;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlingRegistry {


    public static final ScreenHandlerType<ProjectTableScreenHandler> PROJECT_TABLE_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            EnhancedWorkbenches.id("project_table"),
            new ScreenHandlerType<>(
                    ProjectTableScreenHandler::new,
                    FeatureSet.empty()
            )
    );

    public static final ScreenHandlerType<CraftingStationScreenHandler> CRAFTING_STATION_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            EnhancedWorkbenches.id("crafting_station"),
            new ScreenHandlerType<>(
                    CraftingStationScreenHandler::new,
                    FeatureSet.empty()
            )
    );

    public static void init() {
        // NO-OP
    }
}