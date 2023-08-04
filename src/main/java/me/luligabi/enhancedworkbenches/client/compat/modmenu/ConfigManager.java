package me.luligabi.enhancedworkbenches.client.compat.modmenu;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class ConfigManager {
    private static ConfigHolder<ClothConfiguration> holder;

    public static void registerAutoConfig() {
        if (holder != null) {
            throw new IllegalStateException("Configuration already registered!");
        }

        holder = AutoConfig.register(ClothConfiguration.class, GsonConfigSerializer::new);
        holder.save();
    }

    public static ClothConfiguration getConfig() {
        if (holder == null) {
            return new ClothConfiguration();
        }

        return holder.getConfig();
    }

    public static void load() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.load();
    }

    public static void save() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.save();
    }
}
