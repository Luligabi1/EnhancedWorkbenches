package me.luligabi.enhancedworkbenches.common.screenhandler;

import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

import java.util.Set;
import java.util.function.Predicate;

public class ProjectTableScreenHandler extends CraftingBlockScreenHandler implements Inventory {


    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleRecipeInputInventory(3*3), new SimpleInventory(2*9), ScreenHandlerContext.EMPTY);
    }

    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory, SimpleRecipeInputInventory input, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, syncId, playerInventory, input, context);
        this.inventory = inventory;
        checkSize(inventory, 18);
        inventory.onOpen(player);

        addSlot(new CraftingOutputSlot(player, 0, 124, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new CraftingSlot(j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 77 + i * 18));
            }
        }


        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 184));
        }

        onContentChanged(input);
    }

    @Override
    protected Block getBlock() {
        return BlockRegistry.PROJECT_TABLE;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if(slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if(index == 0) {
                context.run((world, pos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world, player);
                });
                if(!insertItem(itemStack2, 10, 64, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if(index >= 1 && index < 10) {
                if(!insertItem(itemStack2, 10, 64, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 10 && index < 28) {
                if(!insertItem(itemStack2, 1, 10, false)) {
                    if (!insertItem(itemStack2, 28, 64, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(index >= 28 && index < 64) {
                if(!insertItem(itemStack2, 1, 28, false)) {
                    if(index < 55) {
                        if(!insertItem(itemStack2, 55, 64, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!insertItem(itemStack2, 28, 55, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!insertItem(itemStack2, 28, 64, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if(itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;

            slot.onTakeItem(player, itemStack2);
            if(index == 0) player.dropItem(itemStack2, false);
        }

        return itemStack;
    }


    public Inventory getInventory() {
        return inventory;
    }

    private final Inventory inventory;



    //--- Inventory overrides ---//

    @Override
    public int getMaxCountPerStack() {
        return inventory.getMaxCountPerStack();
    }

    @Override
    public void onOpen(PlayerEntity player) {
        inventory.onOpen(player);
    }

    @Override
    public void onClose(PlayerEntity player) {
        inventory.onClose(player);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return inventory.isValid(slot, stack);
    }

    @Override
    public boolean canTransferTo(Inventory hopperInventory, int slot, ItemStack stack) {
        return inventory.canTransferTo(hopperInventory, slot, stack);
    }

    @Override
    public int count(Item item) {
        return inventory.count(item);
    }

    @Override
    public boolean containsAny(Set<Item> items) {
        return inventory.containsAny(items);
    }

    @Override
    public boolean containsAny(Predicate<ItemStack> predicate) {
        return inventory.containsAny(predicate);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        inventory.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}