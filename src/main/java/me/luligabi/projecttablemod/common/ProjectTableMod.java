package me.luligabi.projecttablemod.common;

import me.luligabi.projecttablemod.common.block.BlockRegistry;
import me.luligabi.projecttablemod.common.screenhandler.ScreenHandlingRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ProjectTableMod implements ModInitializer {

    @Override
    public void onInitialize() {
        BlockRegistry.init();
        ScreenHandlingRegistry.init();
    }

    public static Identifier id(String id) {
        return new Identifier(IDENTIFIER, id);
    }

    public static final String IDENTIFIER = "projecttablemod";
}
