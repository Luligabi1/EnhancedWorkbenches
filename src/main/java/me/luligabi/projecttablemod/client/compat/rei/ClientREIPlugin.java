package me.luligabi.projecttablemod.client.compat.rei;

import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class ClientREIPlugin implements REIClientPlugin {


    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(BlockRegistry.PROJECT_TABLE));
    }
}
