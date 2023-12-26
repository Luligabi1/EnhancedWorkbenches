package me.luligabi.enhancedworkbenches.client;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.IntSlider;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class ClientConfig {


    public static final ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
            .id(new Identifier("enhancedworkbenches", "client"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("enhancedworkbenches-client.json"))
                    .build())
            .build();

    @AutoGen(category = "rendering", group = "rendering")
    @Boolean(formatter = Boolean.Formatter.YES_NO, colored = true)
    @SerialEntry
    public boolean renderInput = true;
    @AutoGen(category = "rendering", group = "rendering")
    @IntSlider(min = 1, max = 48, step = 1)
    @SerialEntry
    public int renderInputDistance = 3;
    @AutoGen(category = "rendering", group = "rendering")
    @Boolean(formatter = Boolean.Formatter.YES_NO, colored = true)
    @SerialEntry
    public boolean renderInputRequireFancy = true;

    @AutoGen(category = "rendering", group = "project_table")
    @Boolean(formatter = Boolean.Formatter.YES_NO, colored = true)
    @SerialEntry
    public boolean renderInputOnProjectTable = true;
    @AutoGen(category = "rendering", group = "crafting_station")
    @Boolean(formatter = Boolean.Formatter.YES_NO, colored = true)
    @SerialEntry
    public boolean renderInputOnCraftingStation = true;
}
