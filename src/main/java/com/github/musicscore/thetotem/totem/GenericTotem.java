package com.github.musicscore.thetotem.totem;

import com.github.musicscore.thetotem.events.TheTotemEffectActivatesEvent;
import com.github.musicscore.thetotem.util.DataHandler;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class GenericTotem implements Listener {

    Location location;
    Entity entity;
    NBTTagCompound itemTags;

    public abstract boolean matchesItemStack(ItemStack item);

    public abstract void forcePerformEffect(Location location);

    /**
     * Prevents a special Totem item from being misused.
     * @param event The EntityResurrectEvent event that fires.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTotemMisuse(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            NBTTagCompound tag = CraftItemStack.asNMSCopy(((Player) event.getEntity())
                    .getInventory().getItemInOffHand()).getTag();
            if (tag == null || !tag.hasKey("TheTotemProperties")) {
                return;
            }
            event.setCancelled(true);
        }
    }

    /**
     * Runs when the player right clicks with a special Totem.
     * @param event The PlayerInteractEvent that fires.
     */
    @EventHandler
    public void onPlayerUseTotem(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (!matchesItemStack(item) || !event.hasBlock() || !event.getAction().name().startsWith("RIGHT_CLICK")) {
            return;
        }
        boolean isMainHand = event.getHand() == EquipmentSlot.HAND;

        TheTotemEffectActivatesEvent totemEvent = new TheTotemEffectActivatesEvent(event.getPlayer(),
                event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(),
                this,
                event.getItem());
        Bukkit.getPluginManager().callEvent(totemEvent);

        itemTags = CraftItemStack.asNMSCopy(item).getTag().getCompound("TheTotemProperties");
        location = totemEvent.getLocation();

        if (totemEvent.isCancelled()) {
            return;
        }
        forcePerformEffect(location);
        if (DataHandler.isTotemUseCooldownEnabled()) {
            DataHandler.setPlayerCooldown(event.getPlayer(), DataHandler.getTotemUseCooldownDuration());
        }
    }

}
