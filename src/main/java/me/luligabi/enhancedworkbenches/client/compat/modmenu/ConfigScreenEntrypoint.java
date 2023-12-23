package me.luligabi.enhancedworkbenches.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.luligabi.enhancedworkbenches.client.ClientConfig;
import net.minecraft.client.gui.screen.Screen;

public class ConfigScreenEntrypoint implements ModMenuApi {

    private Screen screen;

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> {
            if (screen == null)
                screen = ClientConfig.HANDLER.generateGui().generateScreen(null);

            return screen;
        };
    }
}
