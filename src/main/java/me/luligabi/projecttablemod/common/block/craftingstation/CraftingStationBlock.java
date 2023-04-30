package me.luligabi.projecttablemod.common.block.craftingstation;

import me.luligabi.projecttablemod.common.block.CraftingBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CraftingStationBlock extends CraftingBlock {

    public CraftingStationBlock() {
        super(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CraftingStationBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CraftingStationBlockEntity) {
                ItemScatterer.spawn(world, pos, ((CraftingStationBlockEntity) blockEntity).getInput());
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }


    private static final VoxelShape COUNTER = createCuboidShape(0, 12, 0, 16, 16, 16);
    private static final VoxelShape LEG_1 = createCuboidShape(0, 0, 0, 4, 12, 4);
    private static final VoxelShape LEG_2 = createCuboidShape(0, 0, 12, 4, 12, 16);
    private static final VoxelShape LEG_3 = createCuboidShape(12, 0, 12, 16, 12, 16);
    private static final VoxelShape LEG_4 = createCuboidShape(12, 0, 0, 16, 12, 4);
    private static final VoxelShape VOXEL_SHAPE = VoxelShapes.union(
            CraftingStationBlock.COUNTER,
            CraftingStationBlock.LEG_1,
            CraftingStationBlock.LEG_2,
            CraftingStationBlock.LEG_3,
            CraftingStationBlock.LEG_4
    );
}