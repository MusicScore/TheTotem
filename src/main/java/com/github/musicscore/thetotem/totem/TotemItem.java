package com.github.musicscore.thetotem.totem;

import com.github.musicscore.thetotem.util.TotemRegistry;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class TotemItem {

    private final Class<? extends GenericTotem> TOTEM;
    private int power = 0;
    private boolean active = false;
    private double randomFactor = 0.0;

    public TotemItem(Class<? extends GenericTotem> totem) {
        TOTEM = totem;
    }

    public int getPower() {
        return power;
    }

    public double getRandomFactor() {
        return randomFactor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActiveState(boolean isActive) {
        active = isActive;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setRandomFactor(double factor) {
        randomFactor = factor;
    }

    public net.minecraft.server.v1_13_R2.ItemStack asNbtItemStack() {
        net.minecraft.server.v1_13_R2.ItemStack item = CraftItemStack.asNMSCopy(new ItemStack(Material.TOTEM_OF_UNDYING));
        NBTTagCompound superTag = new NBTTagCompound();

        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("Type", TotemRegistry.getTotemName(TOTEM));
        tags.setInt("Power", power);
        tags.setBoolean("Active", active);
        tags.setDouble("RandomFactor", randomFactor);

        superTag.set("TheTotemProperties", tags);
        item.setTag(superTag);
        return item;
    }

    public ItemStack asBukkitItem() {
        return CraftItemStack.asBukkitCopy(asNbtItemStack());
    }

}
