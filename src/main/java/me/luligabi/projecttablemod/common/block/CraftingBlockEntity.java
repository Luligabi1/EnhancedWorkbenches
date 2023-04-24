package me.luligabi.projecttablemod.common.block;

import me.luligabi.projecttablemod.mixin.CraftingInventoryAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class CraftingBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

    protected CraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        input = new SimpleCraftingInventory(3, 3);
    }
    

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return createScreenHandler(syncId, playerInventory);
    }

    @Override
    public Text getDisplayName() {
        return getContainerName();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        input = new SimpleCraftingInventory(3, 3);
        SimpleCraftingInventory.readNbt(nbt, input);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        SimpleCraftingInventory.writeNbt(nbt, input);
    }


    protected abstract ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory);

    protected abstract Text getContainerName();

    public SimpleCraftingInventory getInput() {
        return input;
    }


    protected SimpleCraftingInventory input;
}