package me.luligabi.projecttablemod.client.renderer;

import me.luligabi.projecttablemod.client.ProjectTableModClient;
import me.luligabi.projecttablemod.common.block.craftingstation.CraftingStationBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class CraftingStationBlockEntityRenderer extends CraftingBlockEntityRenderer<CraftingStationBlockEntity> {

    public CraftingStationBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected boolean canRender() {
        return ProjectTableModClient.CLIENT_CONFIG.renderInputOnCraftingStation;
    }

    @Override
    protected boolean requiresLightmapLighting() {
        return false;
    }
}