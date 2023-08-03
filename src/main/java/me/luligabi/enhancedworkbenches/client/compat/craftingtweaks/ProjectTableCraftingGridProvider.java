package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import net.blay09.mods.craftingtweaks.api.*;
import net.minecraft.screen.ScreenHandler;

public class ProjectTableCraftingGridProvider implements CraftingGridProvider {
    public ProjectTableCraftingGridProvider() {
        CraftingTweaksAPI.registerCraftingGridProvider(this);
    }

    @Override
    public String getModId() {
        return "enhancedworkbenches";
    }

    @Override
    public boolean requiresServerSide() {
        return false;
    }

    @Override
    public boolean handles(ScreenHandler handler) {
        return handler instanceof ProjectTableScreenHandler;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder craftingGridBuilder, ScreenHandler screenHandler) {
        if (screenHandler instanceof ProjectTableScreenHandler) {
            craftingGridBuilder.addGrid("crafting", 1, 9).setButtonAlignment(ButtonAlignment.LEFT);
        }
    }
}
