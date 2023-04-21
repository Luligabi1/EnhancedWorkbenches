package me.luligabi.entitymobiles.projecttablemod.common.screenhandler;

import me.luligabi.entitymobiles.projecttablemod.common.block.SimpleCraftingInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class ProjectTableScreenHandler extends ScreenHandler {


    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleCraftingInventory(7, 4), ScreenHandlerContext.EMPTY);
    }

    public ProjectTableScreenHandler(int syncId, PlayerInventory playerInventory, SimpleCraftingInventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlingRegistry.PROJECT_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.context = context;
        this.player = playerInventory.player;
        checkSize(inventory, 19);
        inventory.onOpen(player);

        addSlot(new CraftingResultSlot(player, inventory, inventory, 0, 124, 35) /*{

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

        }*/);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                addSlot(new Slot(inventory, (j + i * 3) + 1, 30 + j * 18, 17 + i * 18) {

                    @Override
                    public void setStack(ItemStack stack) {
                        super.setStack(stack);
                        System.out.println(getIndex());
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
                addSlot(new Slot(inventory, (j + i * 9 + 9) + 1, 8 + j * 18, 77 + i * 18));
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
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, CraftingInventory inventory) {
        if(!world.isClient()) {
            System.out.println("updateResult");
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory, world);
            if(optional.isPresent()) {
                System.out.println("updateResult2");
                CraftingRecipe craftingRecipe = optional.get();
                ItemStack itemStack2 = craftingRecipe.craft(inventory, world.getRegistryManager());
                if(itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                    itemStack = itemStack2;
                }
            }

            inventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        context.run((world, pos) -> {
            updateResult(this, world, player, this.inventory);
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    private final PlayerEntity player;
    private final ScreenHandlerContext context;
    private final SimpleCraftingInventory inventory;
}
