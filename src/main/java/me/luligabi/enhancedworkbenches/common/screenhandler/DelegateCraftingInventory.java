package me.luligabi.enhancedworkbenches.common.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class DelegateCraftingInventory extends CraftingInventory {

    protected ScreenHandler handler;
    protected Inventory input;

    public DelegateCraftingInventory(ScreenHandler handler, Inventory input) {
        super(handler, 3, 3);
        this.handler = handler;
        this.input = input;
    }

    @Override
    public int size() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!getStack(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= size() ? ItemStack.EMPTY : this.input.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (slot >= 9) throw new IndexOutOfBoundsException();
        return this.input.removeStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (slot >= 9) throw new IndexOutOfBoundsException();
        ItemStack itemStack = this.input.removeStack(slot, amount);
        if (!itemStack.isEmpty()) {
            this.handler.onContentChanged(this);
        }
        return itemStack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot >= 9) throw new IndexOutOfBoundsException();
        this.input.setStack(slot, stack);
        this.handler.onContentChanged(this);
    }

    @Override
    public void markDirty() {
        this.input.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.input.canPlayerUse(player);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size(); i++) {
            removeStack(i);
        }
    }
}
