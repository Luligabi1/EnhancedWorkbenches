package me.luligabi.entitymobiles.projecttablemod.common.screenhandler;

import me.luligabi.entitymobiles.projecttablemod.common.ProjectTableMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

@SuppressWarnings("deprecation")
public class ScreenHandlingRegistry {

    public static final ScreenHandlerType<ProjectTableScreenHandler> PROJECT_TABLE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ProjectTableMod.id("project_table"), ProjectTableScreenHandler::new);


    public static void init() {
        // NO-OP
    }

    private ScreenHandlingRegistry() {
        // NO-OP
    }
}