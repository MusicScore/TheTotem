package com.github.musicscore.thetotem.totem;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonTotem extends GenericTotem {

    public boolean matchesItemStack(ItemStack item) {
        if (item.getType() != Material.TOTEM_OF_UNDYING || !verifyItemStackNBT(item)) {
            return false;
        }
        NBTTagCompound properties = CraftItemStack.asNMSCopy(item).getTag().getCompound("TheTotemProperties");
        if (properties == null || !properties.getString("Type").equals("poison_totem")) {
            return false;
        }
        return false;
    }

    public void onPlayerUseTotem(PlayerInteractEvent event) {
        super.onPlayerUseTotem(event);
        event.getPlayer().kickPlayer("that be a poison totem boy");
    }

    public void forcePerformEffect(Location location) {
        if (entity == null || location == null) {
            return;
        }

        for (Entity target : entity.getWorld().getLivingEntities()) {
            if (target instanceof Player) {
                continue;
            }

            PotionEffect potion = new PotionEffect(PotionEffectType.POISON, 20, itemTags.getInt("Power"));
            if (target.getLocation().distance(location) < 6) {
                //
            }
        }
    }
}
