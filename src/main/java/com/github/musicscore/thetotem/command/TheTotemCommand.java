package com.github.musicscore.thetotem.command;

import com.github.musicscore.thetotem.events.TheTotemEffectActivatesEvent;
import com.github.musicscore.thetotem.totem.GenericTotem;
import com.github.musicscore.thetotem.totem.TotemItem;
import com.github.musicscore.thetotem.util.TotemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TheTotemCommand implements CommandExecutor, TabCompleter {

    private enum Action {
        GIVE,
        TAKE,
        USE
    }

    private Player matchPlayer(String name) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().matches(name)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Invalid amount of arguments! There must be at least 3 arguments.");
            return false;
        }

        Action action = Action.valueOf(args[0]);
        GenericTotem totem = TotemRegistry.getTotemFromName(args[1]);

        if (totem == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid totem!");
            return true;
        }

        int power;
        try {
            power = Integer.parseInt(args[2]);
        }
        catch (Exception e) {
            power = 0;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(args[3]);
        }
        catch (Exception e) {
            quantity = 1;
        }

        Player player = commandSender instanceof Player ? (Player) commandSender : null;
        try {
            player = matchPlayer(args[4]);
        }
        catch (Exception e) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(ChatColor.RED + "Invalid player!");
                return true;
            }
        }

        ItemStack item = null;
        if (action == Action.GIVE || action == Action.TAKE) {
            TotemItem totemItem = new TotemItem(totem.getClass());
            totemItem.setPower(power);
            totemItem.setRandomFactor(Math.random());

            item = totemItem.asBukkitItem();
            item.setAmount(quantity);
        }

        switch (action) {
            case GIVE:
                player.getInventory().addItem(item);
                return true;
            case TAKE:
                player.getInventory().removeItem(item);
                return true;
            case USE:
                TheTotemEffectActivatesEvent event =
                        new TheTotemEffectActivatesEvent(player, player.getLocation(), totem, null);
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    totem.forcePerformEffect(player.getLocation());
                }
                return true;
        }
        commandSender.sendMessage(ChatColor.RED + "Arguments not recognized.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            Action[] firstArgument = Action.values();
            for (Action argument : firstArgument) {
                String returnedArg = argument.toString();
                if (returnedArg.startsWith(args[0].toUpperCase())) {
                    list.add(returnedArg.toLowerCase());
                }
            }
        }
        else if (args.length == 2) {
            for (String totemName : TotemRegistry.getRegisteredTotemNames()) {
                if (totemName.startsWith(args[1].toLowerCase())) {
                    list.add(totemName);
                }
            }
        }
        else if (args.length == 3) {
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
        }
        else if (args.length == 5) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                String playerName = player.getName();
                if (playerName.toLowerCase().startsWith(args[4].toLowerCase())) {
                    list.add(playerName);
                }
            }
        }
        return list;
    }
}
