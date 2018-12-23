package com.github.musicscore.thetotem.totem;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HealingTotem extends GenericTotem {

    public boolean matchesItemStack(ItemStack item) {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if (item.getType() != Material.TOTEM_OF_UNDYING || tag == null) {
            return false;
        }
        NBTTagCompound properties = tag.getCompound("TheTotemProperties");
        if (properties == null || !properties.getString("Type").equals("healing_totem")) {
            return false;
        }
        return false;
    }

    @EventHandler
    public void onPlayerUseTotem(PlayerInteractEvent event) {
        super.onPlayerUseTotem(event);
        Bukkit.getConsoleSender().sendMessage("yep");
    }

    public void forcePerformEffect(Location location) {
        if (entity == null || location == null) {
            return;
        }

        double addedHealth = 4 * (1 + (0.1 * itemTags.getInt("Power")));

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getLocation().distance(location) <= 6) {
                player.spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 7, 0.3, 0.5, 0.3);
                player.setHealth(Math.min(player.getHealth() + addedHealth, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
            }
        }
    }

}
