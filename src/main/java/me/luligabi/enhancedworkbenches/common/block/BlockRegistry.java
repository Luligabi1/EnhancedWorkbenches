package me.luligabi.enhancedworkbenches.common.block;

import me.luligabi.enhancedworkbenches.common.EnhancedWorkbenches;
import me.luligabi.enhancedworkbenches.common.block.craftingstation.CraftingStationBlock;
import me.luligabi.enhancedworkbenches.common.block.craftingstation.CraftingStationBlockEntity;
import me.luligabi.enhancedworkbenches.common.block.projecttable.ProjectTableBlock;
import me.luligabi.enhancedworkbenches.common.block.projecttable.ProjectTableBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static void init() {
        initBlock("project_table", PROJECT_TABLE);
        PROJECT_TABLE_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedWorkbenches.id("project_table"), FabricBlockEntityTypeBuilder.create(ProjectTableBlockEntity::new, PROJECT_TABLE).build());

        initBlock("crafting_station", CRAFTING_STATION);
        CRAFTING_STATION_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, EnhancedWorkbenches.id("crafting_station"), FabricBlockEntityTypeBuilder.create(CraftingStationBlockEntity::new, CRAFTING_STATION).build());
    }


    public static final Block PROJECT_TABLE = new ProjectTableBlock();
    public static BlockEntityType<ProjectTableBlockEntity> PROJECT_TABLE_ENTITY_TYPE;

    public static final Block CRAFTING_STATION = new CraftingStationBlock();
    public static BlockEntityType<CraftingStationBlockEntity> CRAFTING_STATION_ENTITY_TYPE;


    private static void initBlock(String identifier, Block block) {
        Registry.register(Registry.BLOCK, EnhancedWorkbenches.id(identifier), block);
        Registry.register(Registry.ITEM, EnhancedWorkbenches.id(identifier), new BlockItem(block, new FabricItemSettings().group(EnhancedWorkbenches.ENHANCED_WORKBENCHES_TAB)));
        EnhancedWorkbenches.ITEMS.add(new ItemStack(block));
    }

    private BlockRegistry() {
        // NO-OP
    }

}
