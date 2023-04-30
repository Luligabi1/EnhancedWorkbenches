package me.luligabi.projecttablemod.client.renderer;

import me.luligabi.projecttablemod.common.block.craftingstation.CraftingStationBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class CraftingStationBlockEntityRenderer extends CraftingBlockEntityRenderer<CraftingStationBlockEntity> {

    public CraftingStationBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getMaximumChunkRenderDistance() {
        return 3;
    }


}