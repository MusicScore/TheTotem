package com.github.musicscore.thetotem;

import com.github.musicscore.thetotem.command.TheTotemCommand;
import com.github.musicscore.thetotem.totem.HealingTotem;
import com.github.musicscore.thetotem.totem.PoisonTotem;
import com.github.musicscore.thetotem.util.TotemRegistry;
import com.github.musicscore.thetotem.util.nbt.NBTHelper;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class TheTotemPlugin extends JavaPlugin {

    ////////////////////////
    //  STATIC FIELDS AND METHODS
    //////////////////////

    private final static int CONFIG_VERSION = 1;

    public static TheTotemPlugin instance;
    public static YamlConfiguration saves;
    public static Permission perm;

    ////////////////////////
    //  INSTANCE FIELDS AND METHODS
    //////////////////////

    private File file;

    public void onEnable() {
        if (!NBTHelper.verifyVersion(this)) {
            onDisable();
            return;
        }

        instance = this;
        file = new File(getDataFolder(), "saves.yml");

        Logger logger = Bukkit.getLogger();

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            logger.warning("Vault not found. Specialized permissions-based features that rely on Vault may not work!");
        }
        else {
            if (!preparePermissions()) {
                logger.warning(ChatColor.RED + "Permissions service manager not found. Is Vault properly installed?");
            }
            else {

            }
        }

        saveDefaultConfig();
        reloadConfig();

        try {
            if (!getConfig().isSet("general.version") ||
                    getConfig().getInt("general.version", 0) != CONFIG_VERSION) {
                logger.warning(ChatColor.RED + "Your config version does not match the current config version!"
                    + " Please stop your server, delete your config file, and restart your server. This will ensure"
                    + " that your config file is up-to-date and error-free.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        reloadSaves();

        registerCommands();

        TotemRegistry.registerTotem(HealingTotem.class, "healing_totem");
        TotemRegistry.registerTotem(PoisonTotem.class, "poison_totem");
    }

    public void onDisable() {
        instance = null;
        perm = null;
    }

    public void reloadSaves() {
        saves = new YamlConfiguration();
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            saves.load(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSaves() {
        try {
            saves.save(file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////
    //  Private helpers
    //////////////////////

    private void registerCommands() {
        TheTotemCommand cmd = new TheTotemCommand();
        getCommand("thetotem").setExecutor(cmd);
        getCommand("thetotem").setTabCompleter(cmd);
    }

    // Taken almost directly from Vault API GitHub readme.
    private boolean preparePermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perm = rsp.getProvider();
        return perm != null;
    }

}
