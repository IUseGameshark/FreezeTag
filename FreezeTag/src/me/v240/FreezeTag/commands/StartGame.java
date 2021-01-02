package me.v240.FreezeTag.commands;

import me.v240.FreezeTag.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class StartGame implements CommandExecutor {
    private Main plugin;

    private int task; //runnabled id



    public StartGame(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("freezetag").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if (p.hasPermission("FreezeTag.StartStop")) {
            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("start")) {
                    // ! is not, so if the game IS NOT playing
                    if (!Main.game.isPlaying()) {
                        p.sendMessage(ChatColor.AQUA+ "Game starting!");

                        Main.game.start(plugin);
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    Main.game.end();
                    p.sendMessage(ChatColor.RED + "Game ending!");
                } else {
                    p.sendMessage(ChatColor.RED + "Try /FreezeTag <start/stop>");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Try /FreezeTag <start/stop>");
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have the permission to start the game!");
        }

        return true;
    }
}
