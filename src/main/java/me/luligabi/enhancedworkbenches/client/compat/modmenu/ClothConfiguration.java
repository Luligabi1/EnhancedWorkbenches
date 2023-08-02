package me.luligabi.enhancedworkbenches.client.compat.modmenu;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "enhancedworkbenches-client")
public class ClothConfiguration implements ConfigData {
    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.RequiresRestart
    public boolean renderInput = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Gui.RequiresRestart
    public int renderInputDistance = 3;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.RequiresRestart
    public boolean renderInputRequireFancy = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Gui.RequiresRestart
    public boolean renderInputOnProjectTable = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Gui.RequiresRestart
    public boolean renderInputOnCraftingStation = true;
}
