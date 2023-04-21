package me.luligabi.projecttablemod.common.compat.rei;

import com.google.common.collect.Iterables;
import me.luligabi.projecttablemod.common.screenhandler.ProjectTableScreenHandler;
import me.shedaniel.rei.api.common.transfer.RecipeFinder;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProjectTableMenuInfo implements SimpleGridMenuInfo<ProjectTableScreenHandler, DefaultCraftingDisplay<?>> {

    public ProjectTableMenuInfo(DefaultCraftingDisplay<?> display) {
        this.display = display;
    }


    @Override
    public void populateRecipeFinder(MenuInfoContext<ProjectTableScreenHandler, ?, DefaultCraftingDisplay<?>> context, RecipeFinder finder) {
        context.getMenu().provideRecipeInputs(new RecipeMatcher() {

            @Override
            public void addInput(ItemStack itemStack, int i) {
                finder.addItem(itemStack, i);
            }
        });
    }

    @Override
    public Iterable<SlotAccessor> getInventorySlots(MenuInfoContext<ProjectTableScreenHandler, ?, DefaultCraftingDisplay<?>> context) {
        Inventory inventory = context.getMenu().getInventory();
        return Iterables.concat(
                SimpleGridMenuInfo.super.getInventorySlots(context),
                IntStream.range(10, inventory.size())
                        .mapToObj(index -> SlotAccessor.fromContainer(inventory, index))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public int getCraftingResultSlotIndex(ProjectTableScreenHandler menu) {
        return 0;
    }

    @Override
    public int getCraftingWidth(ProjectTableScreenHandler menu) {
        return 3;
    }

    @Override
    public int getCraftingHeight(ProjectTableScreenHandler menu) {
        return 3;
    }

    @Override
    public DefaultCraftingDisplay<?> getDisplay() {
        return display;
    }


    protected final DefaultCraftingDisplay<?> display;

}