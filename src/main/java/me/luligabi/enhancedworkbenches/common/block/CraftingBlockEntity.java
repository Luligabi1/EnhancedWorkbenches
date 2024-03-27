package me.luligabi.enhancedworkbenches.common.block;

import com.google.common.base.Preconditions;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class CraftingBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

    protected CraftingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        input = new SimpleInventory(3*3) {

            @Override
            public void markDirty() {
                super.markDirty();
                CraftingBlockEntity.this.markDirty();
                sync();
            }
        };
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return createScreenHandler(syncId, playerInventory);
    }

    @Override
    public Text getDisplayName() {
        return getContainerName();
    }


    @Override
    protected final void writeNbt(NbtCompound nbt) {
        toTag(nbt);
    }

    @Override
    public final void readNbt(NbtCompound nbt) {
        if (nbt.contains("#c")) {
            fromClientTag(nbt);
            if (nbt.getBoolean("#c")) {
                remesh();
            }
        } else {
            fromTag(nbt);
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public final NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        toClientTag(nbt);
        nbt.putBoolean("#c", shouldClientRemesh); // mark client tag
        shouldClientRemesh = false;
        return nbt;
    }


    public void fromTag(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt.getCompound("Input"), input.stacks);
    }

    public void toTag(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtCompound inputCompound = writeNbt(new NbtCompound(), input.stacks);
        nbt.put("Input", inputCompound);
    }

    public void toClientTag(NbtCompound nbt) {
        toTag(nbt);
    }

    public void fromClientTag(NbtCompound nbt) {
        fromTag(nbt);
    }

    public void sync(boolean shouldRemesh) {
        Preconditions.checkNotNull(world); // Maintain distinct failure case from below
        if (!(world instanceof ServerWorld serverWorld))
            throw new IllegalStateException("Cannot call sync() on the logical client! Did you check world.isClient first?");

        shouldClientRemesh = shouldRemesh | shouldClientRemesh;
        serverWorld.getChunkManager().markForUpdate(pos);
    }

    public void sync() {
        sync(true);
    }

    public final void remesh() {
        Preconditions.checkNotNull(world);
        if (!(world instanceof ClientWorld))
            throw new IllegalStateException("Cannot call remesh() on the server!");

        world.updateListeners(pos, null, null, 0);
    }


    protected abstract ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory);

    protected abstract Text getContainerName();

    public SimpleInventory getInput() {
        return input;
    }

    // the same thing as vanilla's but without the dumb isEmpty check that breaks a lot of stuff
    // Thx, Mojang
    protected NbtCompound writeNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack itemStack = stacks.get(i);
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        nbt.put("Items", nbtList);

        return nbt;
    }


    protected SimpleInventory input;
    private boolean shouldClientRemesh = true;
}