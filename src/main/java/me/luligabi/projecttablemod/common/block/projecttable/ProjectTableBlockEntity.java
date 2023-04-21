package me.luligabi.projecttablemod.common.block.projecttable;

import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.luligabi.projecttablemod.common.block.CraftingBlockEntity;
import me.luligabi.projecttablemod.common.screenhandler.ProjectTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ProjectTableBlockEntity extends CraftingBlockEntity {

    public ProjectTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.PROJECT_TABLE_ENTITY_TYPE, pos, state);
    }


    @Override
    public int size() {
        return 28;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ProjectTableScreenHandler(
                syncId,
                playerInventory,
                inventory,
                ScreenHandlerContext.create(world, pos)
        );
    }

    @Override
    protected Text getContainerName() {
        return Text.of("block.projecttablemod.project_table");
    }

}
