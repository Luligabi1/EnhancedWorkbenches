package me.luligabi.projecttablemod.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
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
        Option<Boolean> renderInput = Option.<Boolean>createBuilder()
                .name(Text.translatable("configOption.projecttablemod.renderInput"))
                .description(OptionDescription.of(Text.translatable("configOption.projecttablemod.renderInput.tooltip")))
                .binding(
                        true,
                        () -> config.renderInput,
                        newValue -> config.renderInput = newValue
                ) // option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true)
                .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                .build();

        Option<Integer> renderInputDistance = Option.<Integer>createBuilder()
                .name(Text.translatable("configOption.projecttablemod.renderInputDistance"))
                .description(OptionDescription.of(Text.translatable("configOption.projecttablemod.renderInputDistance.tooltip")))
                .binding(
                        3,
                        () -> config.renderInputDistance,
                        newValue -> config.renderInputDistance = newValue
                )
                .available(config.renderInput)
                .controller(option -> IntegerFieldControllerBuilder.create(option).range(1, Integer.MAX_VALUE))
                .build();

        Option<Boolean> renderInputRequireFancy = Option.<Boolean>createBuilder()
                .name(Text.translatable("configOption.projecttablemod.renderInputRequireFancy"))
                .description(OptionDescription.of(Text.translatable("configOption.projecttablemod.renderInputRequireFancy.tooltip")))
                .binding(
                        true,
                        () -> config.renderInputRequireFancy,
                        newValue -> config.renderInputRequireFancy = newValue
                )
                .available(config.renderInput)
                .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                .build();

        Option<Boolean> renderInputOnProjectTable = Option.<Boolean>createBuilder()
                .name(Text.translatable("configOption.projecttablemod.renderInputOnProjectTable"))
                .description(OptionDescription.of(Text.translatable("configOption.projecttablemod.renderInputOnProjectTable.tooltip")))
                .binding(
                        true,
                        () -> config.renderInputOnProjectTable,
                        newValue -> config.renderInputOnProjectTable = newValue
                )
                .available(config.renderInput)
                .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
                .build();

        Option<Boolean> renderInputOnCraftingStation = Option.<Boolean>createBuilder()
                .name(Text.translatable("configOption.projecttablemod.renderInputOnCraftingStation"))
                .description(OptionDescription.of(Text.translatable("configOption.projecttablemod.renderInputOnCraftingStation.tooltip")))
                .binding(
                        true,
                        () -> config.renderInputOnCraftingStation,
                        newValue -> config.renderInputOnCraftingStation = newValue
                )
                .available(config.renderInput)
                .controller(option -> BooleanControllerBuilder.create(option).yesNoFormatter().coloured(true))
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
