package com.github.musicscore.thetotem.util;

import com.github.musicscore.thetotem.TheTotemPlugin;
import com.github.musicscore.thetotem.totem.GenericTotem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotemRegistry {

    private static Map<String, Class<? extends GenericTotem>> registry = new HashMap<>();
    private static Map<String, GenericTotem> instanceRegistry = new HashMap<>();

    public static void registerTotem(Class<? extends GenericTotem> totem, String name) {
        GenericTotem totemInstance;
        try {
            totemInstance = totem.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e) {
            Bukkit.getLogger().warning("Could not properly register totem \"" + name + "\"!");
            e.printStackTrace();
            return;
        }

        registry.put(name, totem);
        instanceRegistry.put(name, totemInstance);

        Bukkit.getPluginManager().registerEvents(totemInstance, TheTotemPlugin.instance);
        Bukkit.getLogger().info("Registered totem \"" + ChatColor.RED + name + ChatColor.WHITE + "\"");
    }

    public static List<Class<? extends GenericTotem>> getRegisteredTotems() {
        return new ArrayList<>(registry.values());
    }

    public static List<String> getRegisteredTotemNames() {
        return new ArrayList<>(registry.keySet());
    }

    public static String getTotemName(Class<? extends GenericTotem> totem) {
        for (String name : registry.keySet()) {
            if (registry.get(name).equals(totem)) {
                return name;
            }
        }
        return null;
    }

    public static Class<? extends GenericTotem> getTotemClassFromName(String name) {
        return registry.get(name);
    }

    public static GenericTotem getTotemFromName(String name) {
        return instanceRegistry.get(name);
    }

}
