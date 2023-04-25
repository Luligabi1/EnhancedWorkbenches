package me.luligabi.projecttablemod.common.block;

import me.luligabi.projecttablemod.mixin.CraftingInventoryAccessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

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


    public static NbtCompound writeNbt(NbtCompound nbt, SimpleCraftingInventory inventory) {
        return writeNbt(nbt, inventory, true);
    }

    public static NbtCompound writeNbt(NbtCompound nbt, SimpleCraftingInventory inventory, boolean setIfEmpty) {
        DefaultedList<ItemStack> stacks = ((CraftingInventoryAccessor) inventory).getStacks();
        NbtList nbtList = new NbtList();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            }
        }

        if (!nbtList.isEmpty() || setIfEmpty) {
            nbt.put("Input", nbtList);
        }

        return nbt;
    }

    public static void readNbt(NbtCompound nbt, SimpleCraftingInventory inventory) {
        DefaultedList<ItemStack> stacks = ((CraftingInventoryAccessor) inventory).getStacks();
        NbtList nbtList = nbt.getList("Input", 10);

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j >= 0 && j < stacks.size()) {
                stacks.set(j, ItemStack.fromNbt(nbtCompound));
            }
        }

    }
}