package me.luligabi.enhancedworkbenches.common.screenhandler;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import tfar.fastbench.MixinHooks;

public class ProjectTableScreenHandler extends CraftingBlockScreenHandler {


    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3*3), new SimpleInventory(2*9), ScreenHandlerContext.EMPTY);
    }

    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory input, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, syncId, playerInventory, input, context);
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
        if (EnhancedWorkbenches.QUICKBENCH && index == 0) {
            return MixinHooks.handleShiftCraft(player, this, slots.get(index), input, result, 10, 64);
        }

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

}