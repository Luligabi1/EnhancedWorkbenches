package me.luligabi.enhancedworkbenches.common.screenhandler;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SimpleRecipeInputInventory extends SimpleInventory implements IRecipeInputInventory {

    public SimpleRecipeInputInventory(int size) {
        super(size);
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public List<ItemStack> getInputStacks() {
        return List.copyOf(stacks);
    }
}
