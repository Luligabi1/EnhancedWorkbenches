package me.luligabi.enhancedworkbenches.common.client.compat.jei;

import me.luligabi.enhancedworkbenches.common.client.screen.ProjectTableScreen;
import me.luligabi.enhancedworkbenches.common.mixin.AbstractContainerScreenAccessor;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectTableGuiHandler implements IGuiContainerHandler<ProjectTableScreen> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(ProjectTableScreen screen) {
        return List.of(new Rect2i(
            ((AbstractContainerScreenAccessor) screen).getX() - 68,
            ((AbstractContainerScreenAccessor) screen).getY(),
            64,
            79
        ));
    }
}