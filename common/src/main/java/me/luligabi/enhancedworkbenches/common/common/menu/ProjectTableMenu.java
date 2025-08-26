package me.luligabi.enhancedworkbenches.common.common.menu;

import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;


public class ProjectTableMenu extends CraftingBlockMenu {


    public ProjectTableMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3*3), new SimpleContainer(2*9), ContainerLevelAccess.NULL);
    }

    public ProjectTableMenu(int syncId, Inventory playerInventory, Container input, Container inventory, ContainerLevelAccess levelAccess) {
        super(MenuTypeRegistry.PROJECT_TABLE.get(), syncId, playerInventory, input, levelAccess);
        checkContainerSize(inventory, 18);
        inventory.startOpen(player);

        addSlot(new CraftingOutputSlot(player, inventory, 0, 124, 35));

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

        slotsChanged(input);
    }

    @Override
    protected Block getBlock() {
        return BlockRegistry.PROJECT_TABLE.get();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // FIXME quickbench
        /*if(EnhancedWorkbenches.QUICKBENCH && index == 0) {
            return MixinHooks.handleShiftCraft(player, this, slots.get(index), input, result, 10, 64);
        }*/

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if(slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if(index == 0) {
                context.execute((world, pos) -> {
                    itemStack2.getItem().onCraftedBy(itemStack2, world, player);
                });
                if(!moveItemStackTo(itemStack2, 10, 64, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if(index >= 1 && index < 10) {
                if(!moveItemStackTo(itemStack2, 10, 64, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 10 && index < 28) {
                if(!moveItemStackTo(itemStack2, 1, 10, false)) {
                    if(!moveItemStackTo(itemStack2, 28, 64, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(index >= 28 && index < 64) {
                if(!moveItemStackTo(itemStack2, 1, 28, false)) {
                    if(index < 55) {
                        if(!moveItemStackTo(itemStack2, 55, 64, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!moveItemStackTo(itemStack2, 28, 55, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!moveItemStackTo(itemStack2, 28, 64, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if(itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, itemStack2);
            if(index == 0) player.drop(itemStack2, false);
        }

        return itemStack;
    }

}