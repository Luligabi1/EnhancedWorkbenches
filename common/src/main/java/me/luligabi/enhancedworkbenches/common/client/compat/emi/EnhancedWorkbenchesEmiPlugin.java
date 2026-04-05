package me.luligabi.enhancedworkbenches.common.client.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import me.luligabi.enhancedworkbenches.common.client.screen.ProjectTableScreen;
import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.common.menu.MenuTypeRegistry;
import me.luligabi.enhancedworkbenches.common.mixin.AbstractContainerScreenAccessor;

@EmiEntrypoint
public class EnhancedWorkbenchesEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BlockRegistry.PROJECT_TABLE.get()));
        registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(BlockRegistry.CRAFTING_STATION.get()));

        registry.addRecipeHandler(MenuTypeRegistry.PROJECT_TABLE.get(), PROJECT_TABLE_HANDLER);
        registry.addRecipeHandler(MenuTypeRegistry.CRAFTING_STATION.get(), CRAFTING_STATION_HANDLER);

        registry.addExclusionArea(ProjectTableScreen.class, (screen, consumer) -> {
            consumer.accept(new Bounds(
                ((AbstractContainerScreenAccessor) screen).getX() - 68,
                ((AbstractContainerScreenAccessor) screen).getY(),
                64,
                79
            ));
        });
    }

    public static final CraftingStationRecipeHandler CRAFTING_STATION_HANDLER = new CraftingStationRecipeHandler();
    public static final ProjectTableRecipeHandler PROJECT_TABLE_HANDLER = new ProjectTableRecipeHandler();
}