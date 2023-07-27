package me.luligabi.enhancedworkbenches.common.screenhandler;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;

import java.util.List;

public interface IRecipeInputInventory extends Inventory, RecipeInputProvider {
    int getWidth();

    int getHeight();

    List<ItemStack> getInputStacks();
}
