package me.luligabi.enhancedworkbenches.common.common.block.projecttable;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import io.netty.buffer.ByteBufAllocator;
import me.luligabi.enhancedworkbenches.common.common.block.BlockRegistry;
import me.luligabi.enhancedworkbenches.common.common.block.CraftingBlockEntity;
import me.luligabi.enhancedworkbenches.common.common.menu.ProjectTableMenu;
import me.luligabi.enhancedworkbenches.common.common.util.ProjectTableRecipeHistory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ProjectTableBlockEntity extends CraftingBlockEntity implements ExtendedMenuProvider {

    public final ProjectTableRecipeHistory recipeHistory = new ProjectTableRecipeHistory();
    public final SimpleContainer container = new SimpleContainer(2*9) {
        @Override
        public void setChanged() {
            super.setChanged();
            ProjectTableBlockEntity.this.setChanged();
            sync();
        }
    };

    public ProjectTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.PROJECT_TABLE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected AbstractContainerMenu createScreenHandler(int syncId, Inventory playerInventory) {
        FriendlyByteBuf buf = new FriendlyByteBuf(ByteBufAllocator.DEFAULT.buffer());
        saveExtraData(buf);
        return new ProjectTableMenu(
            syncId,
            playerInventory,
            input,
            container,
            ContainerLevelAccess.create(level, worldPosition),
            buf
        );
    }

    @Override
    protected Component getContainerName() {
        return Component.translatable("container.enhancedworkbenches.project_table");
    }

    @Override
    public void fromTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.fromTag(tag, provider);
        loadAllItems(tag.getCompound("Items"), container.getItems(), provider);
        recipeHistory.fromTag(tag);
    }

    @Override
    public void toTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.toTag(tag, provider);
        CompoundTag itemsCompound = saveAllItems(new CompoundTag(), container.getItems(), provider);
        tag.put("Items", itemsCompound);
        recipeHistory.toTag(tag);
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }

    public SimpleContainer getContainer() {
        return container;
    }

}