package me.luligabi.projecttablemod.common.screenhandler;

import me.luligabi.projecttablemod.common.block.SimpleCraftingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class CraftingStationScreenHandler extends ScreenHandler {


    public CraftingStationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleCraftingInventory(3, 3), ScreenHandlerContext.EMPTY);
    }

    public CraftingStationScreenHandler(int syncId, PlayerInventory playerInventory, SimpleCraftingInventory input, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.CRAFTING_STATION_SCREEN_HANDLER, syncId);
        this.input = input;
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        checkSize(input, 9);
        input.onOpen(player);

        addSlot(new CraftingStationOutputSlot(player, input, result, 0, 124, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new CraftingStationSlot(input, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        onContentChanged(input);
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory inventory, CraftingResultInventory resultInventory) {
        if(!world.isClient()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
            if(optional.isPresent()) {
                CraftingRecipe craftingRecipe = optional.get();
                if(resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.craft(inventory, world.getRegistryManager());
                    if(itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        sendContentUpdates();
        context.run((world, pos) -> {
            updateResult(this, world, player, input, result);
            try {
                world.getBlockEntity(pos).markDirty();
            } catch(NullPointerException e) {
                e.fillInStackTrace();
            }
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if(index == 0) {
                context.run((world, pos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world, player);
                });
                if(!insertItem(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if(index >= 10 && index < 46) {
                if(!insertItem(itemStack2, 1, 10, false)) {
                    if(index < 37) {
                        if(!insertItem(itemStack2, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!insertItem(itemStack2, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!insertItem(itemStack2, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if(itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            if(index == 0) {
                player.dropItem(itemStack2, false);
            }
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public void provideRecipeInputs(RecipeMatcher matcher) {
        input.provideRecipeInputs(matcher);
    }


    private final PlayerEntity player;
    private final ScreenHandlerContext context;
    private final CraftingResultInventory result;
    private final SimpleCraftingInventory input;

    private class CraftingStationSlot extends Slot {

        public CraftingStationSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CraftingStationScreenHandler.this.onContentChanged(inventory);
        }
    }

    private class CraftingStationOutputSlot extends CraftingResultSlot {

        public CraftingStationOutputSlot(PlayerEntity player, CraftingInventory input, Inventory inventory, int index, int x, int y) {
            super(player, input, inventory, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CraftingStationScreenHandler.this.onContentChanged(inventory);
        }
    }
}
