package com.github.musicscore.thetotem.util;

import com.github.musicscore.thetotem.TheTotemPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class DataHandler {

    ////////////////////////
    //  SPECIAL CONFIG READ-DATA METHODS
    //////////////////////

    private static FileConfiguration config = TheTotemPlugin.instance.getConfig();

    /**
     * Returns whether cooldowns are enabled.
     *
     * @return Returns if the player must wait between uses of any special Totem.
     */
    public static boolean isTotemUseCooldownEnabled() {
        return config.getBoolean("general.cooldown.enabled");
    }

    /**
     * Returns the cooldown duration in milliseconds.
     *
     * @return How long the player must wait between uses of any special Totem, in milliseconds.
     * <br>Returns 0 if cooldowns are not enabled.
     */
    public static long getTotemUseCooldownDuration() {
        if (!isTotemUseCooldownEnabled()) {
            return 0;
        }

        String option = config.getString("general.cooldown.duration", "10s");
        long durationInMilliseconds = 0;
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
