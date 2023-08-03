package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.screenhandler.CraftingStationScreenHandler;
import net.blay09.mods.craftingtweaks.api.*;
import net.minecraft.screen.ScreenHandler;

public class CraftingStationCraftingGridProvider implements CraftingGridProvider {
    public CraftingStationCraftingGridProvider() {
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
        return handler instanceof CraftingStationScreenHandler;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder craftingGridBuilder, ScreenHandler screenHandler) {
        if (screenHandler instanceof CraftingStationScreenHandler) {
            craftingGridBuilder.addGrid("crafting", 1, 9).setButtonAlignment(ButtonAlignment.LEFT);
        }
    }
}
