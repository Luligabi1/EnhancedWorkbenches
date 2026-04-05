package me.luligabi.enhancedworkbenches.common.common.menu;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import me.luligabi.enhancedworkbenches.common.common.EnhancedWorkbenches;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class MenuTypeRegistry {


    public static final RegistrySupplier<MenuType<ProjectTableMenu>> PROJECT_TABLE = EnhancedWorkbenches.MENU_TYPES.register(
        EnhancedWorkbenches.id("project_table"),
        () -> MenuRegistry.ofExtended(ProjectTableMenu::new)
    );

    public static final RegistrySupplier<MenuType<CraftingStationMenu>> CRAFTING_STATION = EnhancedWorkbenches.MENU_TYPES.register(
        EnhancedWorkbenches.id("crafting_station"),
        () -> new MenuType<>(CraftingStationMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static void init() {
        // NO-OP
    }

    private MenuTypeRegistry() {
        // NO-OP
    }
}