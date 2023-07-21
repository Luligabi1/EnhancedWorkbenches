package me.luligabi.projecttablemod.common.block.craftingstation;

import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.luligabi.projecttablemod.common.block.CraftingBlockEntity;
import me.luligabi.projecttablemod.common.screenhandler.CraftingStationScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CraftingStationBlockEntity extends CraftingBlockEntity {

    public CraftingStationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.CRAFTING_STATION_ENTITY_TYPE, pos, state);
    }


    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CraftingStationScreenHandler(
                syncId,
                playerInventory,
                input,
                ScreenHandlerContext.create(world, pos)
        );
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.projecttablemod.crafting_station");
    }

}
