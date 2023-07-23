package me.luligabi.enhancedworkbenches.client.renderer;

import me.luligabi.enhancedworkbenches.client.EnhancedWorkbenchesClient;
import me.luligabi.enhancedworkbenches.common.block.projecttable.ProjectTableBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class ProjectTableBlockEntityRenderer extends CraftingBlockEntityRenderer<ProjectTableBlockEntity> {

    public ProjectTableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected boolean canRender() {
        return EnhancedWorkbenchesClient.CLIENT_CONFIG.renderInputOnProjectTable;
    }

    @Override
    protected boolean requiresLightmapLighting() {
        return true;
    }
}