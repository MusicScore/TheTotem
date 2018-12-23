package com.github.musicscore.thetotem.util.nbt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTHelper {

    public static boolean verifyVersion(JavaPlugin plugin) {
        // TODO: Actually verify the version of the server and maybe implement a version check for 1.14
        //  (when it comes out)
        Bukkit.getConsoleSender().sendMessage(plugin.getServer().getClass().getPackage().getName());
        return false;
    }

}
