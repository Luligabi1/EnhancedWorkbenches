package me.luligabi.enhancedworkbenches.common.client.screen;

import me.luligabi.enhancedworkbenches.common.common.menu.CraftingStationMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CraftingStationScreen extends CraftingBlockScreen<CraftingStationMenu> {

    public CraftingStationScreen(CraftingStationMenu abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, inventory, title);
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return ResourceLocation.withDefaultNamespace("textures/gui/container/crafting_table.png");
    }

    @Override
    protected void init() {
        super.init();
        setCoordinates();
    }

}