package me.luligabi.enhancedworkbenches.client.renderer;

import me.luligabi.enhancedworkbenches.client.EnhancedWorkbenchesClient;
import me.luligabi.enhancedworkbenches.common.block.craftingstation.CraftingStationBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class CraftingStationBlockEntityRenderer extends CraftingBlockEntityRenderer<CraftingStationBlockEntity> {
    public CraftingStationBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected boolean canRender() {
        return EnhancedWorkbenchesClient.CLIENT_CONFIG.renderInputOnCraftingStation;
    }

    @Override
    protected boolean requiresLightmapLighting() {
        return false;
    }
}