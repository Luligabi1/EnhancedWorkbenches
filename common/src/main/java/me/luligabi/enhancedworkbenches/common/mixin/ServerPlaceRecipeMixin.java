package me.luligabi.enhancedworkbenches.common.mixin;

import me.luligabi.enhancedworkbenches.common.common.menu.ProjectTableMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlaceRecipe.class)
public class ServerPlaceRecipeMixin<I extends RecipeInput, R extends Recipe<I>> {

    @Shadow
    protected RecipeBookMenu<I, R> menu;

    /*
    @Inject(at = @At("HEAD"), method = "recipeClicked")
    public void unlockUnknownRecipe(ServerPlayer serverPlayer, RecipeHolder<R> recipeHolder, boolean bl, CallbackInfo ci) {
        if(menu instanceof ProjectTableMenu) {
            CraftingBench.INSTANCE.setUnlockUnknownRecipes(true);
        }
    }*/

    @Inject(
        method = "moveItemToGrid",
        at = @At("HEAD"),
        cancellable = true
    )
    public void fillInputSlotUsingBenchContents(Slot slot, ItemStack stack, int index, CallbackInfoReturnable<Integer> cir) {
        boolean changedSlot = false;
        if(menu instanceof ProjectTableMenu) {
            SimpleContainer inventory = ((ProjectTableMenu) menu).container;

            boolean found = false;
            int i;
            for(i = 0; i < inventory.getItems().size(); ++i) {
                ItemStack itemStack = inventory.getItem(i);
                if (!inventory.getItem(i).isEmpty() && ItemStack.isSameItemSameComponents(stack, inventory.getItem(i)) && !inventory.getItem(i).isDamaged() && !itemStack.isEnchanted() && !itemStack.has(DataComponents.CUSTOM_NAME)) {
                    found = true;
                    break;
                }
            }
            if(found) {
                ItemStack itemStack = inventory.getItem(i).copy();
                if (!itemStack.isEmpty()) {
                    if (itemStack.getCount() > 1) {
                        inventory.removeItem(i, 1);
                    } else {
                        inventory.removeItemNoUpdate(i);
                    }

                    itemStack.setCount(1);
                    if (slot.getItem().isEmpty()) {
                        slot.setByPlayer(itemStack);
                    } else {
                        slot.getItem().grow(1);
                    }
                    changedSlot = true;
                }
            }
        }
        if(changedSlot) {
            cir.cancel();
        }
    }

}