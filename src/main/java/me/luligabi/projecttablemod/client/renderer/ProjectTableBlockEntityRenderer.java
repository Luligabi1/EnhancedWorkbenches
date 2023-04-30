package me.luligabi.projecttablemod.client.renderer;

import me.luligabi.projecttablemod.common.block.projecttable.ProjectTableBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

// FIXME lighting issues
public class ProjectTableBlockEntityRenderer extends CraftingBlockEntityRenderer<ProjectTableBlockEntity> {

    public ProjectTableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getMaximumChunkRenderDistance() {
        return 3;
    }


}