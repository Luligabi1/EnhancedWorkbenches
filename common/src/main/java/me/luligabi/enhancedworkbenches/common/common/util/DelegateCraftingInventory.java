package me.luligabi.enhancedworkbenches.common.common.util;


import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;


public class DelegateCraftingInventory extends TransientCraftingContainer {

    protected AbstractContainerMenu menu;
    protected Container input;

    public DelegateCraftingInventory(AbstractContainerMenu menu, Container input) {
        super(menu, 3, 3);
        this.menu = menu;
        this.input = input;
    }

    public CraftingInput toCraftingInput() {
        return CraftingInput.of(3, 3, getItems());
    }

    public CraftingInput toPositionedCraftingInput() {
        return CraftingInput.ofPositioned(3, 3, getItems()).input();
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < 9; i++) {
            if(!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 9 ? ItemStack.EMPTY : input.getItem(slot);
    }


    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if(slot >= 9) throw new IndexOutOfBoundsException();
        return input.removeItemNoUpdate(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if(slot >= 9) throw new IndexOutOfBoundsException();
        ItemStack itemStack = input.removeItem(slot, amount);
        if(!itemStack.isEmpty()) {
            menu.slotsChanged(this);
        }
        return itemStack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if(slot >= 9) throw new IndexOutOfBoundsException();
        input.setItem(slot, stack);
        menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {
        input.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return input.stillValid(player);
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < 9; i++) {
            removeItemNoUpdate(i);
        }
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> stacks = new ArrayList<>(9);
        for(int i = 0; i < 9; i++) {
            stacks.add(getItem(i));
        }
        return stacks;
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for(int i = 0; i < 9; i++) {
            stackedContents.accountSimpleStack(getItem(i));
        }
    }
}