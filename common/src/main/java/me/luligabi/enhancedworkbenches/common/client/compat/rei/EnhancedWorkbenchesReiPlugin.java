package me.luligabi.enhancedworkbenches.common.client.compat.rei;

import me.luligabi.enhancedworkbenches.common.client.screen.ProjectTableScreen;
import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.common.menu.CraftingStationMenu;
import me.luligabi.enhancedworkbenches.common.common.menu.ProjectTableMenu;
import me.luligabi.enhancedworkbenches.common.mixin.AbstractContainerScreenAccessor;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
@REIPluginClient
public class EnhancedWorkbenchesReiPlugin implements REIClientPlugin {


    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(BlockRegistry.PROJECT_TABLE.get()));
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(BlockRegistry.CRAFTING_STATION.get()));
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(SimpleTransferHandler.create(
            CraftingStationMenu.class,
            BuiltinPlugin.CRAFTING,
            new SimpleTransferHandler.IntRange(1, 10)
        ));
        registry.register(new SimpleTransferHandler() {
            @Override
            public ApplicabilityResult checkApplicable(Context context) {
                if(!(context.getMenu() instanceof ProjectTableMenu)
                    || !BuiltinPlugin.CRAFTING.equals(context.getDisplay().getCategoryIdentifier())
                    || context.getContainerScreen() == null) {
                    return ApplicabilityResult.createNotApplicable();
                } else {
                    return ApplicabilityResult.createApplicable();
                }
            }

            @Override
            public Iterable<SlotAccessor> getInputSlots(Context context) {
                return IntStream.range(1, 10)
                    .mapToObj(id -> SlotAccessor.fromSlot(context.getMenu().getSlot(id)))
                    .toList();
            }

            @Override
            public Iterable<SlotAccessor> getInventorySlots(Context context) {
                return IntStream.range(10, 64)
                    .mapToObj(id -> SlotAccessor.fromSlot(context.getMenu().getSlot(id)))
                    .toList();
            }
        });
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(ProjectTableScreen.class, screen -> {
            return List.of(new Rectangle(
                ((AbstractContainerScreenAccessor) screen).getX() - 68,
                ((AbstractContainerScreenAccessor) screen).getY(),
                64,
                79
            ));
        });
    }
}