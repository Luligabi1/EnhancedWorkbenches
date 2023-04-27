package me.luligabi.projecttablemod.common.screenhandler;

import me.luligabi.projecttablemod.common.ProjectTableMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

@SuppressWarnings("deprecation")
public class ScreenHandlingRegistry {

    public static final ScreenHandlerType<ProjectTableScreenHandler> PROJECT_TABLE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ProjectTableMod.id("project_table"), ProjectTableScreenHandler::new);

    public static final ScreenHandlerType<CraftingStationScreenHandler> CRAFTING_STATION_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ProjectTableMod.id("crafting_station"), CraftingStationScreenHandler::new);

    public static void init() {
        // NO-OP
    }

    private ScreenHandlingRegistry() {
        // NO-OP
    }
}