package me.luligabi.projecttablemod.common.compat.rei;

import me.luligabi.projecttablemod.common.screenhandler.CraftingStationScreenHandler;
import me.luligabi.projecttablemod.common.screenhandler.ProjectTableScreenHandler;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class CommonREIPlugin implements REIServerPlugin {

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(BuiltinPlugin.CRAFTING, ProjectTableScreenHandler.class, SimpleMenuInfoProvider.of(ProjectTableMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, CraftingStationScreenHandler.class, SimpleMenuInfoProvider.of(CraftingStationMenuInfo::new));
    }
}
