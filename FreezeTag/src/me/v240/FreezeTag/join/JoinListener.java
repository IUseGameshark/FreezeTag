//this class basically give the player resistance on join
package me.v240.FreezeTag.join;

import me.v240.FreezeTag.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinListener implements Listener {

    private static Main plugin;

    public JoinListener(Main plugin){

        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        //could also use Player player = event.getPlayer();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        //that just gets rid of any potion effects players had before joining

        player.setGlowing(false);
        player.setGravity(true);

        //checks for the resistance bypass permission before giving effect
        if (!(player.hasPermission("FreezeTag.Resistance.Bypass"))) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 255));
            //this adds the resistance effect for 1000000 mins (or 277 hours or infinity) at level 255.

        } else{
            player.sendMessage("You have the resistance bypass permission so you didn't get resistance!");
        }
    }

}