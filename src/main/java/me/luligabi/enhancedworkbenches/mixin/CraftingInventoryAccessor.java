package me.luligabi.enhancedworkbenches.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingInventory.class)
public interface CraftingInventoryAccessor {

    @Accessor("stacks")
    DefaultedList<ItemStack> enhancedworkbenches_getStacks();
}
