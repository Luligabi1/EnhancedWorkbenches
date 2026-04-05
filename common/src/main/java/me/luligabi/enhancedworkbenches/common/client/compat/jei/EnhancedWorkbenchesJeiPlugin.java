package me.luligabi.enhancedworkbenches.common.client.compat.jei;

import me.luligabi.enhancedworkbenches.common.client.screen.CraftingStationScreen;
import me.luligabi.enhancedworkbenches.common.client.screen.ProjectTableScreen;
import me.luligabi.enhancedworkbenches.common.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.common.menu.CraftingStationMenu;
import me.luligabi.enhancedworkbenches.common.common.menu.MenuTypeRegistry;
import me.luligabi.enhancedworkbenches.common.common.menu.ProjectTableMenu;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class EnhancedWorkbenchesJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = EnhancedWorkbenches.id("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
                RecipeTypes.CRAFTING,
                BlockRegistry.PROJECT_TABLE.get(),
                BlockRegistry.CRAFTING_STATION.get()
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                CraftingStationScreen.class,
                88, 32, 28, 23,
                RecipeTypes.CRAFTING
        );
        registration.addRecipeClickArea(
                ProjectTableScreen.class,
                88, 32, 28, 23,
                RecipeTypes.CRAFTING
        );

        registration.addGuiContainerHandler(
                ProjectTableScreen.class,
                new ProjectTableGuiHandler()
        );
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                CraftingStationMenu.class,
                MenuTypeRegistry.CRAFTING_STATION.get(),
                RecipeTypes.CRAFTING,
                1, 9, 10, 36
        );
        registration.addRecipeTransferHandler(
                ProjectTableMenu.class,
                MenuTypeRegistry.PROJECT_TABLE.get(),
                RecipeTypes.CRAFTING,
                1, 9, 10, 54
        );
    }
}
