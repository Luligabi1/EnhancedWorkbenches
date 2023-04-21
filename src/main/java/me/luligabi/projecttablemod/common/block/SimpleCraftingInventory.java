package me.luligabi.projecttablemod.common.block;

import me.luligabi.projecttablemod.mixin.CraftingInventoryAccessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;

public class SimpleCraftingInventory extends CraftingInventory {

    public SimpleCraftingInventory(int width, int height) {
        super(null, width, height);
    }


    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(((CraftingInventoryAccessor) this).getStacks(), slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ((CraftingInventoryAccessor) this).getStacks().set(slot, stack);
    }


    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }
}