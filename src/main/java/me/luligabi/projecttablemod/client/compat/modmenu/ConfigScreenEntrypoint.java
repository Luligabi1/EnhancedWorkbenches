package me.luligabi.projecttablemod.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;
import me.luligabi.projecttablemod.client.ClientConfig;
import me.luligabi.projecttablemod.client.ProjectTableModClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreenEntrypoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen parent) {
        ClientConfig config = ProjectTableModClient.CLIENT_CONFIG;

        /*
         * Rendering (Generic)
         */
        Option<Boolean> renderInput = Option.createBuilder(Boolean.class)
                .name(Text.translatable("configOption.projecttablemod.renderInput"))
                .tooltip(Text.translatable("configOption.projecttablemod.renderInput.tooltip"))
                .binding(
                        true,
                        () -> config.renderInput,
                        newValue -> config.renderInput = newValue
                )
                .controller((booleanOption) -> new BooleanController(booleanOption, BooleanController.YES_NO_FORMATTER, true))
                .build();

        Option<Integer> renderInputDistance = Option.createBuilder(Integer.class)
                .name(Text.translatable("configOption.projecttablemod.renderInputDistance"))
                .tooltip(Text.translatable("configOption.projecttablemod.renderInputDistance.tooltip"))
                .binding(
                        3,
                        () -> config.renderInputDistance,
                        newValue -> config.renderInputDistance = newValue
                )
                .available(config.renderInput)
                .controller((intOption) -> new IntegerFieldController(intOption, 1, Integer.MAX_VALUE))
                .build();

        Option<Boolean> renderInputRequireFancy = Option.createBuilder(Boolean.class)
                .name(Text.translatable("configOption.projecttablemod.renderInputRequireFancy"))
                .tooltip(Text.translatable("configOption.projecttablemod.renderInputRequireFancy.tooltip"))
                .binding(
                        true,
                        () -> config.renderInputRequireFancy,
                        newValue -> config.renderInputRequireFancy = newValue
                )
                .available(config.renderInput)
                .controller((booleanOption) -> new BooleanController(booleanOption, BooleanController.YES_NO_FORMATTER, true))
                .build();

        Option<Boolean> renderInputOnProjectTable = Option.createBuilder(Boolean.class)
                .name(Text.translatable("configOption.projecttablemod.renderInputOnProjectTable"))
                .tooltip(Text.translatable("configOption.projecttablemod.renderInputOnProjectTable.tooltip"))
                .binding(
                        true,
                        () -> config.renderInputOnProjectTable,
                        newValue -> config.renderInputOnProjectTable = newValue
                )
                .available(config.renderInput)
                .controller((booleanOption) -> new BooleanController(booleanOption, BooleanController.YES_NO_FORMATTER, true))
                .build();

        Option<Boolean> renderInputOnCraftingStation = Option.createBuilder(Boolean.class)
                .name(Text.translatable("configOption.projecttablemod.renderInputOnCraftingStation"))
                .tooltip(Text.translatable("configOption.projecttablemod.renderInputOnCraftingStation.tooltip"))
                .binding(
                        true,
                        () -> config.renderInputOnCraftingStation,
                        newValue -> config.renderInputOnCraftingStation = newValue
                )
                .available(config.renderInput)
                .controller((booleanOption) -> new BooleanController(booleanOption, BooleanController.YES_NO_FORMATTER, true))
                .build();



        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("itemGroup.projecttablemod.item_group"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("configCategory.projecttablemod.rendering"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("configCategory.projecttablemod.rendering"))
                                .option(renderInput)
                                .option(renderInputDistance)
                                .option(renderInputRequireFancy)
                        .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("block.projecttablemod.project_table"))
                                .option(renderInputOnProjectTable)
                        .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("block.projecttablemod.crafting_station"))
                                .option(renderInputOnCraftingStation)
                        .build())
                        .build())
                .save(() -> ProjectTableModClient.saveConfig(config))
                .build()
                .generateScreen(parent);
    }
}
