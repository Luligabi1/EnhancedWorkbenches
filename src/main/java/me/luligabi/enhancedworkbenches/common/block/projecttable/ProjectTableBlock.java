package me.luligabi.enhancedworkbenches.common.block.projecttable;

import me.luligabi.enhancedworkbenches.common.block.CraftingBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ProjectTableBlock extends CraftingBlock {
    public ProjectTableBlock() {
        super(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ProjectTableBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ProjectTableBlockEntity) {
                ItemScatterer.spawn(world, pos, ((ProjectTableBlockEntity) blockEntity).getInput());
                ItemScatterer.spawn(world, pos, ((ProjectTableBlockEntity) blockEntity).getInventory());
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


}