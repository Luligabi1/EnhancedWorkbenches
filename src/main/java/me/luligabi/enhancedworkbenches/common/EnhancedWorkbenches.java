package me.luligabi.enhancedworkbenches.common;

import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class EnhancedWorkbenches implements ModInitializer {

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ScreenHandlingRegistry.init();

        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.of("Enhanced Workbenches"))
                .icon(() -> new ItemStack(BlockRegistry.PROJECT_TABLE))
                .entries((ctx, entries) ->
                        entries.addAll(EnhancedWorkbenches.ITEMS)
                )
        .build());
    }

    public static Identifier id(String id) {
        return new Identifier(IDENTIFIER, id);
    }

    public static final String IDENTIFIER = "enhancedworkbenches";
    public static final boolean QUICKBENCH = FabricLoader.getInstance().isModLoaded("quickbench");

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("item_group"));
    public static final List<ItemStack> ITEMS = new ArrayList<>();
}
