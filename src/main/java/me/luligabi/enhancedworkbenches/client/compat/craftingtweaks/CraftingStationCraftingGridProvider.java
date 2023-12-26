package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.screenhandler.CraftingStationScreenHandler;
import net.blay09.mods.craftingtweaks.api.CraftingGridBuilder;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.minecraft.screen.ScreenHandler;

public class CraftingStationCraftingGridProvider implements CraftingGridProvider {
    public CraftingStationCraftingGridProvider() {
        CraftingTweaksAPI.registerCraftingGridProvider(this);
    }

    @Override
    public String getModId() {
        return EnhancedWorkbenches.IDENTIFIER;
    }

    @Override
    public boolean handles(ScreenHandler screenHandler) {
        return screenHandler instanceof CraftingStationScreenHandler;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder craftingGridBuilder, ScreenHandler screenHandler) {
        if (screenHandler instanceof CraftingStationScreenHandler) {
            craftingGridBuilder.addGrid(1, 9);
        }
    }
}
