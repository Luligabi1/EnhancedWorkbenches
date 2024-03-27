package me.luligabi.enhancedworkbenches.common.mixin;

import me.luligabi.enhancedworkbenches.common.screenhandler.SimpleRecipeInputInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.fastbench.interfaces.CraftingInventoryDuck;

@Mixin(SimpleRecipeInputInventory.class)
public class SimpleRecipeInputInventoryMixin implements CraftingInventoryDuck {
    @Unique
    public boolean checkMatrixChanges = true;

    @Override
    public void setCheckMatrixChanges(boolean checkMatrixChanges) {
        this.checkMatrixChanges = checkMatrixChanges;
    }

    @Override
    public boolean getCheckMatrixChanges() {
        return this.checkMatrixChanges;
    }
}
