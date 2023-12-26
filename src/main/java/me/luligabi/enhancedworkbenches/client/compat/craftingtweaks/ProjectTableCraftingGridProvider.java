package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import net.blay09.mods.craftingtweaks.api.CraftingGridBuilder;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridClearHandler;
import net.minecraft.screen.ScreenHandler;

public class ProjectTableCraftingGridProvider implements CraftingGridProvider {
    public ProjectTableCraftingGridProvider() {
        CraftingTweaksAPI.registerCraftingGridProvider(this);
    }

    @Override
    public String getModId() {
        return EnhancedWorkbenches.IDENTIFIER;
    }

    @Override
    public boolean handles(ScreenHandler screenHandler) {
        return screenHandler instanceof ProjectTableScreenHandler;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder craftingGridBuilder, ScreenHandler screenHandler) {
        if (screenHandler instanceof ProjectTableScreenHandler) {
            craftingGridBuilder.addGrid(1, 9)
                    .clearHandler((craftingGrid, playerEntity, screenHandler1, forced) -> {
                        // Put items from the crafting grid to the inventory of the table and only then to inventory.
                        for (int i = 1; i < 10; i++) {
                            screenHandler.quickMove(playerEntity, i);
                        }

                        (new DefaultGridClearHandler()).clearGrid(craftingGrid, playerEntity, screenHandler1, forced);
                    });
        }
    }
}
