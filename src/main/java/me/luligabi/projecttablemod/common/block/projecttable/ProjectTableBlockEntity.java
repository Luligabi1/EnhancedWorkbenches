package me.luligabi.projecttablemod.common.block.projecttable;

import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.luligabi.projecttablemod.common.block.CraftingBlockEntity;
import me.luligabi.projecttablemod.common.screenhandler.ProjectTableScreenHandler;
import me.luligabi.projecttablemod.common.screenhandler.SimpleRecipeInputInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ProjectTableBlockEntity extends CraftingBlockEntity {

    public ProjectTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.PROJECT_TABLE_ENTITY_TYPE, pos, state);
        inventory = new SimpleInventory(2*9) {

            @Override
            public void markDirty() {
                super.markDirty();
                ProjectTableBlockEntity.this.markDirty();
                sync();
            }
        };
    }


    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ProjectTableScreenHandler(
                syncId,
                playerInventory,
                input,
                inventory,
                ScreenHandlerContext.create(world, pos)
        );
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.projecttablemod.project_table");
    }

    @Override
    public void fromTag(NbtCompound nbt) {
        super.fromTag(nbt);

        Inventories.readNbt(nbt.getCompound("Items"), inventory.stacks);
    }

    @Override
    public void toTag(NbtCompound nbt) {
        super.toTag(nbt);

        NbtCompound itemsCompound = writeNbt(new NbtCompound(), inventory.stacks);
        nbt.put("Items", itemsCompound);
    }

    public SimpleInventory getInventory() {
        return inventory;
    }


    protected SimpleInventory inventory;
}
