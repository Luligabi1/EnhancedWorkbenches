package me.luligabi.entitymobiles.projecttablemod.common.block;

import me.luligabi.entitymobiles.projecttablemod.mixin.CraftingInventoryAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CraftingBlockEntity extends LockableContainerBlockEntity {


    protected CraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        inventory = new SimpleCraftingInventory(7, 4); // 7*4 = 28, 9 input, 1 output, 18 inv
    }



    public static void tick(World world, BlockPos pos, BlockState state, CraftingBlockEntity blockEntity) {
        /*if(input.isEmpty()) return;

        Optional<CraftingRecipe> recipe = createRecipeOptional(world);
        if(recipe.isEmpty()) {
            blockEntity.inventory.set(0, ItemStack.EMPTY);
            return;
        }

        blockEntity.inventory.set(0, recipe.get().getOutput(world.getRegistryManager()).copy());

        */
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory = new SimpleCraftingInventory(7, 4);
        Inventories.readNbt(nbt, ((CraftingInventoryAccessor) inventory).getStacks());
        System.out.println("read: " + ((CraftingInventoryAccessor) inventory).getStacks());
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, ((CraftingInventoryAccessor) inventory).getStacks());
        System.out.println("write: " + Inventories.writeNbt(nbt, ((CraftingInventoryAccessor) inventory).getStacks()));
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(((CraftingInventoryAccessor) inventory).getStacks(), slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(((CraftingInventoryAccessor) inventory).getStacks(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    protected SimpleCraftingInventory inventory;

}
