package me.luligabi.enhancedworkbenches.common;

import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class EnhancedWorkbenches implements ModInitializer {
    public static final ItemGroup ENHANCED_WORKBENCHES_TAB = FabricItemGroupBuilder.build(id("enhanced_workbenches_tab"),
            () -> new ItemStack(BlockRegistry.PROJECT_TABLE));

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ScreenHandlingRegistry.init();

        if (FabricLoader.getInstance().isModLoaded("craftingtweaks")) {
            try {
                Class.forName("me.luligabi.enhancedworkbenches.client.compat.craftingtweaks.ProjectTableCraftingGridProvider").getConstructor().newInstance();
                Class.forName("me.luligabi.enhancedworkbenches.client.compat.craftingtweaks.CraftingStationCraftingGridProvider").getConstructor().newInstance();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static Identifier id(String id) {
        return new Identifier(IDENTIFIER, id);
    }

    public static final String IDENTIFIER = "enhancedworkbenches";

    public static final List<ItemStack> ITEMS = new ArrayList<>();
}
