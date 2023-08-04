package me.luligabi.enhancedworkbenches.client.compat.craftingtweaks;

import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import net.blay09.mods.craftingtweaks.api.*;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridBalanceHandler;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridClearHandler;
import net.blay09.mods.craftingtweaks.api.impl.DefaultGridRotateHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

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
                            (new DefaultGridClearHandler()).clearGrid(craftingGrid, playerEntity, handler, b);

                            // Uuh, seems updating client render data not helps, so I need to mark the slot so it gets updated?
                            // Probably needs a better solution, but... ok, this works.
                            screenHandler.getSlot(1).markDirty();
                        }
                    }).rotateHandler(new GridRotateHandler<>() {
                        @Override
                        public void rotateGrid(CraftingGrid craftingGrid, PlayerEntity playerEntity, ScreenHandler handler, boolean b) {
                            (new DefaultGridRotateHandler()).rotateGrid(craftingGrid, playerEntity, handler, b);

                            screenHandler.getSlot(1).markDirty();
                        }
                    }).balanceHandler(new GridBalanceHandler<>() {
                        @Override
                        public void balanceGrid(CraftingGrid craftingGrid, PlayerEntity playerEntity, ScreenHandler handler) {
                            (new DefaultGridBalanceHandler()).balanceGrid(craftingGrid, playerEntity, handler);

                            screenHandler.getSlot(1).markDirty();
                        }

                        @Override
                        public void spreadGrid(CraftingGrid craftingGrid, PlayerEntity playerEntity, ScreenHandler handler) {
                            (new DefaultGridBalanceHandler()).spreadGrid(craftingGrid, playerEntity, handler);

                            screenHandler.getSlot(1).markDirty();
                        }
                    });
        }
    }
}
