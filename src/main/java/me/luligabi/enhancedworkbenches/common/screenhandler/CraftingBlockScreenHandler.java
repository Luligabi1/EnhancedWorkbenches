package me.luligabi.enhancedworkbenches.common.screenhandler;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tfar.fastbench.MixinHooks;

import java.util.Optional;

public abstract class CraftingBlockScreenHandler extends ScreenHandler {


    protected CraftingBlockScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory input, ScreenHandlerContext context) {
        super(type, syncId);
        this.input = new DelegateCraftingInventory(this, input);
        this.context = context;
        this.player = playerInventory.player;
        this.blockPos = context.get((world, pos) -> pos).orElse(BlockPos.ORIGIN);

        checkSize(input, 9);
        input.onOpen(player);
    }

    @SuppressWarnings("ConstantConditions")
    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, DelegateCraftingInventory input, CraftingResultInventory output) {
        if(world.isClient()) return;
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
        ItemStack itemStack = ItemStack.EMPTY;
        Optional<CraftingRecipe> recipeOptional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, input, world);
        if(recipeOptional.isPresent()) {
            CraftingRecipe recipe = recipeOptional.get();
            if(output.shouldCraftRecipe(world, serverPlayerEntity, recipe)) {
                ItemStack itemStack2 = recipe.craft(input, world.getRegistryManager());
                if(itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                    itemStack = itemStack2;
                }
            }
        }
        output.setStack(0, itemStack);
        handler.setPreviousTrackedSlot(0, itemStack);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        context.run((world, pos) -> {
            if (EnhancedWorkbenches.QUICKBENCH) {
                MixinHooks.slotChangedCraftingGrid(world, input, result);
            } else {
                updateResult(this, world, player, input, result);
            }
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, getBlock());
    }

    public void provideRecipeInputs(RecipeMatcher matcher) {
        input.provideRecipeInputs(matcher);
    }


    protected abstract Block getBlock();

    protected final BlockPos blockPos;
    protected final PlayerEntity player;
    protected final ScreenHandlerContext context;
    protected final DelegateCraftingInventory input;
    protected final CraftingResultInventory result = new CraftingResultInventory() /*{

        @Override
        public void markDirty() {
            super.markDirty();
            context.run((world, pos) -> {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if(blockEntity != null) {
                    blockEntity.markDirty();
                    ((CraftingBlockEntity) blockEntity).sync();
                }
            });
        }
    }*/;

    protected class CraftingSlot extends Slot {

        public CraftingSlot(int index, int x, int y) {
            super(input, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CraftingBlockScreenHandler.this.onContentChanged(inventory);
        }
    }

    protected class CraftingOutputSlot extends CraftingResultSlot {

        public CraftingOutputSlot(PlayerEntity player, int index, int x, int y) {
            super(player, CraftingBlockScreenHandler.this.input, CraftingBlockScreenHandler.this.result, index, x, y);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            CraftingBlockScreenHandler.this.onContentChanged(inventory);
        }

        @Override
        public ItemStack takeStack(int amount) {
            markDirty();
            return super.takeStack(amount);
        }

        /*
        @Override
        protected void onCrafted(ItemStack stack, int amount) {
            super.onCrafted(stack, amount);
            markDirty();
        }*/

        @Override
        protected void onCrafted(ItemStack stack) {
            super.onCrafted(stack);
            markDirty();
        }

        @Override
        protected void onTake(int amount) {
            super.onTake(amount);
            markDirty();
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            super.onTakeItem(player, stack);
            markDirty();
        }

    }
}
