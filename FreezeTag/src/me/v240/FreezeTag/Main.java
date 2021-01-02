package me.v240.FreezeTag;

import me.v240.FreezeTag.elements.Game;
import me.v240.FreezeTag.hitListener.HitListener;
import me.v240.FreezeTag.join.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;
import me.v240.FreezeTag.commands.StartGame;

public class Main extends JavaPlugin {
    public static Game game;
    public static HitListener hListener;

    @Override
    public void onLoad(){
        getLogger().info("FreezeTag has loaded");
    }

    @Override
    public void onEnable(){
        getLogger().info("FreezeTag has been enabled");
        saveDefaultConfig();
        new JoinListener(this);
        new StartGame(this);
        hListener = new HitListener(this);

        game = new Game(this);
    }

    @Override
    public void onDisable(){
        getLogger().info("FreezeTag has been disabled");
    }

}
