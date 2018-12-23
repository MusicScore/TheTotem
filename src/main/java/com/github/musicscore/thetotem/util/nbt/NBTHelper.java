package com.github.musicscore.thetotem.util.nbt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTHelper {

    public static boolean verifyVersion(JavaPlugin plugin) {
        Bukkit.getConsoleSender().sendMessage(plugin.getServer().getClass().getPackage().getName());
        return false;
    }

}
