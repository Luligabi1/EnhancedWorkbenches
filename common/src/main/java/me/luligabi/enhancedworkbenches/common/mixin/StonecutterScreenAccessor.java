package me.luligabi.enhancedworkbenches.common.mixin;

import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StonecutterScreen.class)
public interface StonecutterScreenAccessor {

    @Accessor("RECIPE_SPRITE")
    static ResourceLocation getRecipeSprite() {
        throw new AssertionError();
    }

    @Accessor("RECIPE_HIGHLIGHTED_SPRITE")
    static ResourceLocation getRecipeHighlightedSprite() {
        throw new AssertionError();
    }
}
