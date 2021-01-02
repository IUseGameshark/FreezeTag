package me.v240.FreezeTag.hitListener;

import com.mysql.fabric.xmlrpc.base.Array;
import me.v240.FreezeTag.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class HitListener implements Listener {
    private int playersFrozen = 0;
    ArrayList<Player> freeze = new ArrayList<Player>();

    public void resetFreeze() {
        freeze.clear();
    }

    public boolean win() {
        return playersFrozen == Main.game.getNumberOfPlayer();
    }

    public HitListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player && Main.game.isPlaying()) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();

            // If player is it && other player is not && player is not already frozen && game is playing && they are in the list of player set at the beginning of the game
            if (Main.game.isPlayerIt(whoHit) && !Main.game.isPlayerIt(whoWasHit) && !freeze.contains(whoWasHit) && Main.game.isPlaying()) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online != whoWasHit)
                        online.sendMessage(ChatColor.RED + String.valueOf(whoWasHit.getName()) + " got hit and is frozen");
                    else
                        whoWasHit.sendMessage(ChatColor.RED + "You have been frozen!");
                }

                freeze.add(whoWasHit);
                playersFrozen++;
                whoWasHit.setGameMode(GameMode.ADVENTURE);

                if (win()) Main.game.end();
            }

            // If neither player is it && the player hit is frozen && the other player is not already frozen && game is playing && they are in the list of player set at the beginning of the game
            if (!Main.game.isPlayerIt(whoWasHit) && !Main.game.isPlayerIt(whoHit) && freeze.contains(whoWasHit) && !freeze.contains(whoHit)) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(ChatColor.GREEN + String.valueOf(whoWasHit.getName()) + " was unfrozen");
                }

                freeze.remove(whoWasHit);
                playersFrozen--;
                whoWasHit.setGameMode(GameMode.SURVIVAL);
            }

        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (freeze.contains(e.getPlayer())) {
            Location from = e.getFrom();
            Location to = e.getTo();
            double x=Math.floor(from.getX());
            double y=Math.floor(from.getY());
            double z=Math.floor(from.getZ());
            if(Math.floor( to.getX()) != x || Math.floor( to.getZ() ) != z || Math.floor( to.getY() ) != y)
            {
                x+=.5;
                z+=.5;
                y+=.5;
                e.getPlayer().teleport(new Location(from.getWorld(),x,y,z,from.getYaw(),from.getPitch()));
            }
        }
    }
}