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

public class CraftingStationMenu extends CraftingBlockMenu {


    public CraftingStationMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3*3), ContainerLevelAccess.NULL);
    }

    public CraftingStationMenu(int syncId, Inventory playerInventory, Container input, ContainerLevelAccess levelAccess) {
        super(MenuTypeRegistry.CRAFTING_STATION.get(), syncId, playerInventory, input, levelAccess);


        addSlot(new CraftingOutputSlot(player, 0, 124, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new CraftingSlot(j + i * 3, 30 + j * 18, 17 + i * 18));
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

        slotsChanged(input);
    }

    @Override
    protected Block getBlock() {
        return BlockRegistry.CRAFTING_STATION.get();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // FIXME quickbench
        /*if(EnhancedWorkbenches.QUICKBENCH && index == 0) {
            return MixinHooks.handleShiftCraft(player, this, slots.get(index), input, result, 10, 46);
        }*/

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if(index == 0) {
                access.execute((world, pos) -> {
                    itemStack2.getItem().onCraftedBy(itemStack2, world, player);
                });
                if(!moveItemStackTo(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);
            } else if(index >= 10 && index < 46) {
                if(!moveItemStackTo(itemStack2, 1, 10, false)) {
                    if(index < 37) {
                        if(!moveItemStackTo(itemStack2, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!moveItemStackTo(itemStack2, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!moveItemStackTo(itemStack2, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if(itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
            if(index == 0) {
                player.drop(itemStack2, false);
            }
        }

        return itemStack;
    }

}