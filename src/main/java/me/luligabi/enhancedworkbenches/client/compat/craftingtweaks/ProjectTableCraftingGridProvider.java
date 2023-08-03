package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import net.blay09.mods.craftingtweaks.api.*;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridRotateHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ProjectTableCraftingGridProvider implements CraftingGridProvider {
    public ProjectTableCraftingGridProvider() {
        CraftingTweaksAPI.registerCraftingGridProvider(this);
    }

    @Override
    public String getModId() {
        return "enhancedworkbenches";
    }

    @Override
    public boolean requiresServerSide() {
        return false;
    }

    @Override
    public boolean handles(ScreenHandler handler) {
        return handler instanceof ProjectTableScreenHandler;
    }

    @Override
    public void buildCraftingGrids(CraftingGridBuilder craftingGridBuilder, ScreenHandler screenHandler) {
        if (screenHandler instanceof ProjectTableScreenHandler) {
            craftingGridBuilder.addGrid("crafting", 1, 9).setButtonAlignment(ButtonAlignment.LEFT)
                    .clearHandler(new GridClearHandler<>() {
                        @Override
                        public void clearGrid(CraftingGrid craftingGrid, PlayerEntity playerEntity, ScreenHandler handler, boolean b) {
                            // Put items from the crafting grid to the inventory of the table and only then to inventory.
                            for (int i = 1; i < 10; i++) {
                                screenHandler.transferSlot(playerEntity, i);
                            }

                            // Uuh, seems updating client render data not helps, so I need to mark the slot so it gets updated?
                            // Probably needs a better solution, but... ok, this works.
                            screenHandler.slots.get(1).markDirty();
                        }
                    }).rotateHandler(new GridRotateHandler<>() {
                        @Override
                        public void rotateGrid(CraftingGrid craftingGrid, PlayerEntity playerEntity, ScreenHandler handler, boolean b) {
                            (new DefaultGridRotateHandler()).rotateGrid(craftingGrid, playerEntity, handler, b);

                            screenHandler.slots.get(1).markDirty();
                        }
                    });
        }
    }
}
