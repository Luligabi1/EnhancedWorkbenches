package me.luligabi.projecttablemod.common.screenhandler;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SimpleRecipeInputInventory extends SimpleInventory implements RecipeInputInventory {

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
