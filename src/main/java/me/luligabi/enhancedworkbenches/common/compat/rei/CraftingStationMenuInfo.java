package me.luligabi.enhancedworkbenches.common.compat.rei;

import me.luligabi.enhancedworkbenches.common.screenhandler.CraftingStationScreenHandler;
import me.shedaniel.rei.api.common.transfer.RecipeFinder;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;

public class CraftingStationMenuInfo implements SimpleGridMenuInfo<CraftingStationScreenHandler, DefaultCraftingDisplay<?>> {
    public CraftingStationMenuInfo(DefaultCraftingDisplay<?> display) {
        this.display = display;
    }

    @Override
    public void populateRecipeFinder(MenuInfoContext<CraftingStationScreenHandler, ?, DefaultCraftingDisplay<?>> context, RecipeFinder finder) {
        context.getMenu().provideRecipeInputs(new RecipeMatcher() {
            @Override
            public void addInput(ItemStack itemStack, int i) {
                finder.addItem(itemStack, i);
            }
        });
    }

    @Override
    public int getCraftingResultSlotIndex(CraftingStationScreenHandler menu) {
        return 0;
    }

    @Override
    public int getCraftingWidth(CraftingStationScreenHandler menu) {
        return 3;
    }

    @Override
    public int getCraftingHeight(CraftingStationScreenHandler menu) {
        return 3;
    }

    @Override
    public DefaultCraftingDisplay<?> getDisplay() {
        return display;
    }


    protected final DefaultCraftingDisplay<?> display;

}