package me.luligabi.enhancedworkbenches.client.compat.rei;

import me.luligabi.enhancedworkbenches.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.screenhandler.CraftingStationScreenHandler;
import me.luligabi.enhancedworkbenches.common.screenhandler.ProjectTableScreenHandler;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

import java.util.stream.IntStream;

public class ClientREIPlugin implements REIClientPlugin {


    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(BlockRegistry.PROJECT_TABLE));
        registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(BlockRegistry.CRAFTING_STATION));
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(SimpleTransferHandler.create(
                CraftingStationScreenHandler.class,
                BuiltinPlugin.CRAFTING,
                new SimpleTransferHandler.IntRange(1, 10)
        ));
        registry.register(new SimpleTransferHandler() {
            @Override
            public ApplicabilityResult checkApplicable(Context context) {
                if (!(context.getMenu() instanceof ProjectTableScreenHandler)
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
}
