package com.github.musicscore.thetotem.util;

import com.github.musicscore.thetotem.TheTotemPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class DataHandler {

    ////////////////////////
    //  SPECIAL CONFIG READ-DATA METHODS
    //////////////////////

    private static FileConfiguration config = TheTotemPlugin.instance.getConfig();

    public static boolean isTotemUseCooldownEnabled() {
        return config.getBoolean("general.cooldown.enabled");
    }

    public static long getTotemUseCooldownDuration() {
        String option = config.getString("general.cooldown.duration", "10s");
        long durationInMilliseconds = 0;

        // returns in milliseconds
        try {
            double duration = Double.parseDouble(option.substring(0, option.length() - 1));
            char suffix = option.charAt(option.length() - 1);

            switch (suffix) {
                case 't':
                    durationInMilliseconds = Math.round(duration * 50);
                case 'm':
                    durationInMilliseconds = Math.round(duration * 60000);
                case 'h':
                    durationInMilliseconds = Math.round(duration * 3600000);
                    // Default to seconds
                default:
                    durationInMilliseconds = Math.round(duration * 1000);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return durationInMilliseconds;
    }

    ////////////////////////
    //  SAVED DATA METHODS
    //////////////////////

    private static FileConfiguration data = TheTotemPlugin.saves;

    // generic data tools

    public static void setPlayerData(OfflinePlayer player, String path, Object value) {
        String uuid = String.valueOf(player.getUniqueId());
        if (data.getConfigurationSection(uuid) == null) {
            data.createSection(uuid);
        }
        data.getConfigurationSection(uuid).set(path, value);
    }

    // specialized tools

    static String playerCooldownPath = "totem_cooldown";

    public static long setPlayerCooldown(OfflinePlayer player, long milliseconds) {
        if (milliseconds < 0) {
            return -1;
        }
        long endTime = System.currentTimeMillis() + milliseconds;
        setPlayerData(player, playerCooldownPath, endTime);
        return endTime;
    }

    public static long getPlayerRemainingCooldown(OfflinePlayer player) {
        long expiration = data.getLong(playerCooldownPath) - System.currentTimeMillis();
        if (expiration <= 0) {
            setPlayerData(player, playerCooldownPath, null);
            return 0;
        }
        return expiration;
    }

}
