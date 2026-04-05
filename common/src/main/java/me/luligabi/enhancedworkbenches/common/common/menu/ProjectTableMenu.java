package me.luligabi.enhancedworkbenches.common.common.menu;

import com.google.common.collect.Lists;
import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.common.block.projecttable.ProjectTableBlockEntity;
import me.luligabi.enhancedworkbenches.common.common.util.ProjectTableRecipeHistory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ProjectTableMenu extends CraftingBlockMenu {

    public BlockPos clientPos;
    private boolean isOutputtingRecipe = false;
    private RecipeHolder<CraftingRecipe> lastRecipe = null;
    public final SimpleContainer container;

    public ProjectTableMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory, new SimpleContainer(3*3), new SimpleContainer(2*9), ContainerLevelAccess.NULL, buf);
        clientPos = buf.readBlockPos();
    }

    public ProjectTableMenu(int syncId, Inventory playerInventory, Container input, SimpleContainer container, ContainerLevelAccess levelAccess, FriendlyByteBuf buf) {
        super(MenuTypeRegistry.PROJECT_TABLE.get(), syncId, playerInventory, input, levelAccess);
        clientPos = BlockPos.ZERO;
        checkContainerSize(container, 18);
        container.startOpen(player);
        this.container = container;

        addSlot(new ProjectTableOutputSlot(player, container, 0, 124, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new CraftingSlot(j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(container, j + i * 9, 8 + j * 18, 77 + i * 18));
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

        slotsChanged(input);
    }

    @Override
    public void slotsChanged(Container container) {
        if(isOutputtingRecipe) return;
        access.execute((level, pos) -> {
            Optional<RecipeHolder<CraftingRecipe>> resultRecipe = updateResult(this, level, player, input, result);
            if(resultRecipe.isEmpty() || isOutputtingRecipe) return;
            lastRecipe = resultRecipe.get();
        });
    }

    @Override
    public boolean clickMenuButton(Player player, int index) {
        if(index >= 0 && index <= 8) {
            return true;
        } else if(index >= 10 && index <= 18) {
            System.out.println("clickMenuButton CTRL | " + index);
            access.execute((level, pos) -> {
                if(level.getBlockEntity(pos) instanceof ProjectTableBlockEntity projectTable) {
                    projectTable.recipeHistory.togglePin(index - 10);
                    projectTable.setChanged();
                    projectTable.sync();
                }
            });
            return true;
        }
        return super.clickMenuButton(player, index);
    }

    @Override
    public void handlePlacement(boolean bl, RecipeHolder recipeHolder, ServerPlayer serverPlayer) {
        this.beginPlacingRecipe();

        try {
            (new PlaceRecipe(this)).recipeClicked(serverPlayer, recipeHolder, bl);
        } finally {
            this.finishPlacingRecipe(recipeHolder);
        }
    }

    @Override
    protected void beginPlacingRecipe() {
        isOutputtingRecipe = true;
    }

    @Override
    protected void finishPlacingRecipe(RecipeHolder recipeHolder) {
        isOutputtingRecipe = false;
        access.execute((level, pos) -> {
            updateResult(this, level, player, input, result);
        });
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    protected Block getBlock() {
        return BlockRegistry.PROJECT_TABLE.get();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        // FIXME quickbench
        /*if(EnhancedWorkbenches.QUICKBENCH && index == 0) {
            return MixinHooks.handleShiftCraft(player, this, slots.get(index), input, result, 10, 64);
        }*/

        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();

            if (index == 0) {
                access.execute((world, pos) -> itemStack2.getItem().onCraftedBy(itemStack2, world, player));
                if (!moveItemStackTo(itemStack2, 10, 28, false) &&
                        !moveItemStackTo(itemStack2, 28, 64, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack2, itemStack);

            } else if (index >= 1 && index < 10) {
                if (!moveItemStackTo(itemStack2, 10, 28, false) &&
                        !moveItemStackTo(itemStack2, 28, 64, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (index >= 10 && index < 28) {
                if (!moveItemStackTo(itemStack2, 28, 64, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 64) {
                if (!moveItemStackTo(itemStack2, 10, 28, false) &&
                        !moveItemStackTo(itemStack2, 1, 10, false)) {
                    if (index < 55) {
                        if (!moveItemStackTo(itemStack2, 55, 64, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!moveItemStackTo(itemStack2, 28, 55, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, itemStack2);
            if (index == 0) player.drop(itemStack2, false);
        }

        return itemStack;
    }

    private class ProjectTableOutputSlot extends CraftingOutputSlot {

        public ProjectTableOutputSlot(Player player, Container container, int index, int x, int y) {
            super(player, container, index, x, y);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ProjectTableMenu.this.slotsChanged(container);
        }

        @Override
        public ItemStack remove(int amount) {
            setChanged();
            return super.remove(amount);
        }

        @Override
        protected void checkTakeAchievements(ItemStack stack) {
            super.checkTakeAchievements(stack);
            setChanged();
        }

        @Override
        protected void onSwapCraft(int amount) {
            super.onSwapCraft(amount);
            setChanged();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            /*
             * Prevents slotsChanged from running while items
             * are being removed from the input as that adds
             * non-intended recipes to the history
             */
            isOutputtingRecipe = true;
            super.onTake(player, stack);
            isOutputtingRecipe = false;
            access.execute((level, pos) -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof ProjectTableBlockEntity projectTable && !lastRecipe.value().isSpecial()) {
                    projectTable.recipeHistory.add(new ProjectTableRecipeHistory.RecipeHistoryEntry(lastRecipe.id()));
                    projectTable.setChanged();
                    projectTable.sync();
                }
            });
            setChanged();
        }
    }

    private static class PlaceRecipe extends ServerPlaceRecipe {

        private final SimpleContainer container;

        public PlaceRecipe(ProjectTableMenu tableMenu) {
            super(tableMenu);
            this.container = tableMenu.container;
        }


        @Override
        public void recipeClicked(ServerPlayer serverPlayer, @Nullable RecipeHolder recipeHolder, boolean bl) {
            if (recipeHolder != null && serverPlayer.getRecipeBook().contains(recipeHolder)) {
                this.inventory = serverPlayer.getInventory();
                if (this.testClearGrid() || serverPlayer.isCreative()) {
                    this.stackedContents.clear();
                    serverPlayer.getInventory().fillStackedContents(this.stackedContents);
                    this.container.fillStackedContents(this.stackedContents);
                    this.menu.fillCraftSlotsStackedContents(this.stackedContents);
                    if (this.stackedContents.canCraft(recipeHolder.value(), null)) {
                        this.handleRecipeClicked(recipeHolder, bl);
                    } else {
                        this.clearGrid();
                        serverPlayer.connection.send(new ClientboundPlaceGhostRecipePacket(serverPlayer.containerMenu.containerId, recipeHolder));
                    }

                    serverPlayer.getInventory().setChanged();
                    container.setChanged();
                }
            }
        }

        private boolean testClearGrid() {
            List<ItemStack> list = Lists.newArrayList();
            int i = this.getAmountOfFreeSlotsInInventory();

            for (int j = 0; j < this.menu.getGridWidth() * this.menu.getGridHeight() + 1; ++j) {
                if (j != this.menu.getResultSlotIndex()) {
                    ItemStack itemStack = this.menu.getSlot(j).getItem().copy();
                    if (!itemStack.isEmpty()) {
                        int k = this.inventory.getSlotWithRemainingSpace(itemStack);
                        if (k == -1) k = findContainerSlotWithRemainingSpace(itemStack);

                        if (k == -1 && list.size() <= i) {
                            for (ItemStack itemStack2 : list) {
                                if (ItemStack.isSameItem(itemStack2, itemStack)
                                        && itemStack2.getCount() != itemStack2.getMaxStackSize()
                                        && itemStack2.getCount() + itemStack.getCount() <= itemStack2.getMaxStackSize()) {
                                    itemStack2.grow(itemStack.getCount());
                                    itemStack.setCount(0);
                                    break;
                                }
                            }
                            if (!itemStack.isEmpty()) {
                                if (list.size() >= i) return false;
                                list.add(itemStack);
                            }
                        } else if (k == -1) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private int findContainerSlotWithRemainingSpace(ItemStack itemStack) {
            for (int i = 0; i < this.container.getContainerSize(); i++) {
                ItemStack stored = this.container.getItem(i);
                if (stored.isEmpty()) return i;
                if (ItemStack.isSameItemSameComponents(stored, itemStack)
                        && stored.getCount() < stored.getMaxStackSize()) {
                    return i;
                }
            }
            return -1;
        }

        private int getAmountOfFreeSlotsInInventory() {
            int i = 0;

            for (ItemStack itemStack : this.inventory.items) {
                if (itemStack.isEmpty()) {
                    ++i;
                }
            }
            for (ItemStack itemStack : this.container.getItems()) {
                if (itemStack.isEmpty()) {
                    ++i;
                }
            }

            return i;
        }

        @Override
        protected int moveItemToGrid(Slot slot, ItemStack itemStack, int i) {
            int remaining = super.moveItemToGrid(slot, itemStack, i);
            if (remaining == 0) return 0;

            int toTake = (remaining == -1) ? i : remaining;

            int j = findSlotMatchingUnusedItem(itemStack);
            if (j == -1) return remaining;

            ItemStack stored = this.container.getItem(j);
            int k;
            if (toTake < stored.getCount()) {
                this.container.removeItem(j, toTake);
                k = toTake;
            } else {
                this.container.removeItemNoUpdate(j);
                k = stored.getCount();
            }

            if (slot.getItem().isEmpty()) {
                slot.set(stored.copyWithCount(k));
            } else {
                slot.getItem().grow(k);
            }

            return toTake - k;
        }

        private int findSlotMatchingUnusedItem(ItemStack template) {
            for (int i = 0; i < this.container.getContainerSize(); i++) {
                ItemStack stack = this.container.getItem(i);
                if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, template)) {
                    return i;
                }
            }
            return -1;
        }

    }


}