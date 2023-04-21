package me.luligabi.entitymobiles.projecttablemod.client;

import me.luligabi.entitymobiles.projecttablemod.client.screen.ProjectTableScreen;
import me.luligabi.entitymobiles.projecttablemod.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ProjectTableModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, ProjectTableScreen::new);
    }
}
