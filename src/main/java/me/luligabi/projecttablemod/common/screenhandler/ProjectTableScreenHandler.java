package me.luligabi.projecttablemod.common.screenhandler;

import me.luligabi.projecttablemod.common.block.SimpleCraftingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class ProjectTableScreenHandler extends ScreenHandler {


    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleCraftingInventory(9, 3), new SimpleInventory(2*9), ScreenHandlerContext.EMPTY);
    }

    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory, SimpleCraftingInventory input, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, syncId);
        this.input = input;
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        checkSize(input, 9);
        checkSize(inventory, 18);
        input.onOpen(player);
        inventory.onOpen(player);

        addSlot(new CraftingResultSlot(player, input, result, 0, 124, 35) {

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                onContentChanged(inventory);
            }

            @Override
            public ItemStack takeStack(int amount) {
                onContentChanged(inventory);
                return super.takeStack(amount);
            }


            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                onContentChanged(inventory);
            }


        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new Slot(input, j + i * 3, 30 + j * 18, 17 + i * 18) {

                    @Override
                    public void setStack(ItemStack stack) {
                        super.setStack(stack);
                        onContentChanged(inventory);
                    }

                    @Override
                    public ItemStack takeStack(int amount) {
                        onContentChanged(inventory);
                        return super.takeStack(amount);
                    }

                });
            }
        }

        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 77 + i * 18));
            }
        }


        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 184));
        }

        onContentChanged(input);
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory inventory, CraftingResultInventory resultInventory) {
        if(!world.isClient()) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
            if(optional.isPresent()) {
                CraftingRecipe craftingRecipe = optional.get();
                if(resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.craft(inventory, world.getRegistryManager());
                    if(itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        context.run((world, pos) -> {
            if(!world.isClient()) Objects.requireNonNull(world.getBlockEntity(pos)).markDirty();
            updateResult(this, world, player, this.input, result);
        });
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if(slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if(index == 0) {
                context.run((world, pos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world, player);
                });
                if(!insertItem(itemStack2, 28, 64, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if(index >= 1 && index < 10) {
                if(!insertItem(itemStack2, 10, 64, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(index >= 10 && index < 28) {
                if(!insertItem(itemStack2, 1, 10, false)) {
                    if (!insertItem(itemStack2, 28, 64, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(index >= 28 && index < 64) {
                if(!insertItem(itemStack2, 1, 28, false)) {
                    if(index < 55) {
                        if(!insertItem(itemStack2, 55, 64, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if(!insertItem(itemStack2, 28, 55, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!insertItem(itemStack2, 28, 64, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if(itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;

            slot.onTakeItem(player, itemStack2);
            if(index == 0) player.dropItem(itemStack2, false);
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public void provideRecipeInputs(RecipeMatcher matcher) {
        input.provideRecipeInputs(matcher);
    }

    public SimpleCraftingInventory getInput() {
        return input;
    }

    private final PlayerEntity player;
    private final ScreenHandlerContext context;
    private final CraftingResultInventory result;
    private final SimpleCraftingInventory input;
}
