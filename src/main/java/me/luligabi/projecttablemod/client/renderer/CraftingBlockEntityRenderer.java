package me.luligabi.projecttablemod.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.luligabi.projecttablemod.client.ProjectTableModClient;
import me.luligabi.projecttablemod.common.block.CraftingBlock;
import me.luligabi.projecttablemod.common.block.CraftingBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.HashMap;

public abstract class CraftingBlockEntityRenderer<T extends CraftingBlockEntity> implements BlockEntityRenderer<T> {

    @SuppressWarnings("unused")
    public CraftingBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(!shouldRender()) return;
        Inventory inventory = entity.getInput();
        if(inventory.isEmpty() || !(entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof CraftingBlock)) return;

        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(Properties.HORIZONTAL_FACING);
        for(int i = 0; i < 9; i++) {
            renderItem(entity, inventory, i, direction, matrices, vertexConsumers, light);
        }
    }

    private void renderItem(T entity, Inventory inventory, int index, Direction direction, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if(inventory.getStack(index).isEmpty()) return;
        matrices.push();
        Pair<Double, Double> pos = getDirectionPositionMap(direction).get(index);

        matrices.translate(pos.getLeft(), 1D, pos.getRight());
        matrices.scale(0.1F, 0.1F, 0.1F);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(getItemAngle(direction)));
        MinecraftClient.getInstance().getItemRenderer().renderItem(inventory.getStack(index), ModelTransformationMode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), (int) entity.getPos().asLong());
        matrices.pop();
    }

    private HashMap<Integer, Pair<Double, Double>> getDirectionPositionMap(Direction direction) {
        return switch(direction) {
            case NORTH -> NORTH_POSITIONS;
            case SOUTH -> SOUTH_POSITIONS;
            case WEST -> WEST_POSITIONS;
            case EAST -> EAST_POSITIONS;
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }

    private float getItemAngle(Direction direction) {
        return switch(direction) {
            case NORTH -> 0;
            case SOUTH -> 2 * 90;
            case WEST, EAST -> direction.getHorizontal() * 90;
            default -> throw new IllegalStateException("Unexpected direction: " + direction);
        };
    }

    private boolean shouldRender() {
        if(!ProjectTableModClient.CLIENT_CONFIG.renderInput || !canRender()) return false;

        return !ProjectTableModClient.CLIENT_CONFIG.renderInputRequireFancy || MinecraftClient.isFancyGraphicsOrBetter();
    }

    protected abstract boolean canRender();

    @Override
    public int getRenderDistance() {
        return ProjectTableModClient.CLIENT_CONFIG.renderInputDistance * 16;
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    private static final HashMap<Integer, Pair<Double, Double>> NORTH_POSITIONS;
    private static final HashMap<Integer, Pair<Double, Double>> SOUTH_POSITIONS;
    private static final HashMap<Integer, Pair<Double, Double>> WEST_POSITIONS;
    private static final HashMap<Integer, Pair<Double, Double>> EAST_POSITIONS;

    static {
        NORTH_POSITIONS = Maps.newHashMap(new ImmutableMap.Builder<Integer, Pair<Double, Double>>()
                .put(0, new Pair<>(0.0625*11D, 0.0625*11D))
                .put(1, new Pair<>(0.0625*8D, 0.0625*11D))
                .put(2, new Pair<>(0.0625*5D, 0.0625*11D))
                .put(3, new Pair<>(0.0625*11D, 0.0625*8D))
                .put(4, new Pair<>(0.0625*8D, 0.0625*8D))
                .put(5, new Pair<>(0.0625*5D, 0.0625*8D))
                .put(6, new Pair<>(0.0625*11D, 0.0625*5D))
                .put(7, new Pair<>(0.0625*8D, 0.0625*5D))
                .put(8, new Pair<>(0.0625*5D, 0.0625*5D))
        .build());
        SOUTH_POSITIONS = Maps.newHashMap(new ImmutableMap.Builder<Integer, Pair<Double, Double>>()
                .put(0, new Pair<>(0.0625*5D, 0.0625*5D))
                .put(1, new Pair<>(0.0625*8D, 0.0625*5D))
                .put(2, new Pair<>(0.0625*11D, 0.0625*5D))


                .put(3, new Pair<>(0.0625*5D, 0.0625*8D))
                .put(4, new Pair<>(0.0625*8D, 0.0625*8D))
                .put(5, new Pair<>(0.0625*11D, 0.0625*8D))

                .put(6, new Pair<>(0.0625*5D, 0.0625*11D))
                .put(7, new Pair<>(0.0625*8D, 0.0625*11D))
                .put(8, new Pair<>(0.0625*11D, 0.0625*11D))
        .build());
        WEST_POSITIONS = Maps.newHashMap(new ImmutableMap.Builder<Integer, Pair<Double, Double>>()
                .put(0, new Pair<>(0.0625*11D, 0.0625*5D))
                .put(1, new Pair<>(0.0625*11D, 0.0625*8D))
                .put(2, new Pair<>(0.0625*11D, 0.0625*11D))
                .put(3, new Pair<>(0.0625*8D, 0.0625*5D))
                .put(4, new Pair<>(0.0625*8D, 0.0625*8D))
                .put(5, new Pair<>(0.0625*8D, 0.0625*11D))
                .put(6, new Pair<>(0.0625*5D, 0.0625*5D))
                .put(7, new Pair<>(0.0625*5D, 0.0625*8D))
                .put(8, new Pair<>(0.0625*5D, 0.0625*11D))
        .build());
        EAST_POSITIONS = Maps.newHashMap(new ImmutableMap.Builder<Integer, Pair<Double, Double>>()
                .put(0, new Pair<>(0.0625*5D, 0.0625*11D))
                .put(1, new Pair<>(0.0625*5D, 0.0625*8D))
                .put(2, new Pair<>(0.0625*5D, 0.0625*5D))
                .put(3, new Pair<>(0.0625*8D, 0.0625*11D))
                .put(4, new Pair<>(0.0625*8D, 0.0625*8D))
                .put(5, new Pair<>(0.0625*8D, 0.0625*5D))
                .put(6, new Pair<>(0.0625*11D, 0.0625*11D))
                .put(7, new Pair<>(0.0625*11D, 0.0625*8D))
                .put(8, new Pair<>(0.0625*11D, 0.0625*5D))
        .build());
    }
}