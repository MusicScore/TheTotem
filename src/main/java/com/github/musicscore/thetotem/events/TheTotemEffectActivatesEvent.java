package com.github.musicscore.thetotem.events;

import com.github.musicscore.thetotem.totem.GenericTotem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TheTotemEffectActivatesEvent extends Event implements Cancellable {

    ////////////////////////
    //  INTERFACE FIELDS AND METHODS
    //////////////////////

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public HandlerList getHandlers() {
        return getHandlerList();
    }

    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    ////////////////////////
    //  CONSTRUCTOR
    //////////////////////

    public TheTotemEffectActivatesEvent(Player player, Location location, GenericTotem totem, ItemStack item) {
        PLAYER = player;
        this.location = location;
        this.totem = totem;
        this.item = item;
        cancelled = false;
    }

    ////////////////////////
    //  INSTANCE FIELDS AND METHODS
    //////////////////////

    private final Player PLAYER;
    private Location location;
    private GenericTotem totem;
    private ItemStack item;

    public Player getPlayer() {
        return PLAYER;
    }

    public ItemStack getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(double x, double y, double z, World world) {
        location = new Location(world, x, y, z);
    }

    public GenericTotem getUsedTotem() {
        return totem;
    }

    public void setUsedTotem(GenericTotem totem) {
        this.totem = totem;
    }
}
