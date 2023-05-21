package me.luligabi.projecttablemod.client.renderer;

import me.luligabi.projecttablemod.client.ProjectTableModClient;
import me.luligabi.projecttablemod.common.block.projecttable.ProjectTableBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class ProjectTableBlockEntityRenderer extends CraftingBlockEntityRenderer<ProjectTableBlockEntity> {

    public ProjectTableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected boolean canRender() {
        return ProjectTableModClient.CLIENT_CONFIG.renderInputOnProjectTable;
    }

    @Override
    protected boolean requiresLightmapLighting() {
        return true;
    }
}