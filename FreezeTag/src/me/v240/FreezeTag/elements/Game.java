package me.v240.FreezeTag.elements;

import me.v240.FreezeTag.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    // For countdown timer to run in another thread
    private int task;
    public int getTask() {
        return task;
    }
    public void setTask(int task) { this.task = task; }

    // Return is the game is still playing
    public boolean isPlaying() { return isPlaying; }

    // List of all the players that are "it"
    private ArrayList<Player> its = new ArrayList<>();

    // Amount of players that are playing
    private int playersInGame = 0;

    // The time intervals that should be outputed to the chat
    private ArrayList<Integer> outs = new ArrayList<>();

    // The time of the game (which is pulled from the config.yml)
    private int time;

    // Gets the amount of people that will become taggers (from config file)
    private int taggers;

    // Is the game in progress?
    public boolean isPlaying;

    // returns the number of avoiders
    public int getNumberOfPlayer() {
        return playersInGame;
    }

    public boolean isPlayerIt(Player p) {
        if (its.contains(p)) {
            return true;
        }

        return false;
    }

    // Constructor
    public Game(Main plugin) {
        this.task = -1;
        this.isPlaying = false;

        String game_time = plugin.getConfig().getString("game_time");
        time = Integer.parseInt(game_time);

        String taggersStr = plugin.getConfig().getString("taggers");
        taggers = Integer.parseInt(taggersStr);

        // Initialize the outs list with 1-15
        for (int i = 1; i < 16; i++) {
            outs.add(i);
        }

        // Add thirty second intervals for the outs
        for (int i = 0; i * 30 < time; i++) {
            outs.add(i * 30);
        }

        // Tells players the starting value for the game
        outs.add(time);
    }

    public void end() {
        isPlaying = false;

        Bukkit.getScheduler().cancelTask((getTask()));

        for (Player online: Bukkit.getOnlinePlayers()) {
            online.setGlowing(false);
            online.setGameMode(GameMode.SURVIVAL);
            online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        if (Main.hListener.win()) {
            for (Player online: Bukkit.getOnlinePlayers()) {
                online.sendMessage(ChatColor.GOLD + "The taggers won!");
            }
        } else {
            for (Player online: Bukkit.getOnlinePlayers()) {
                online.sendMessage(ChatColor.GOLD + "The avoiders won!");
            }
        }

        Main.hListener.resetFreeze();

        // Clear the people that are it
        its.clear();
    }

    public void start(Main plugin) {
        isPlaying = true;

        pickIts();

        if(getTask() != -1)
            Bukkit.getScheduler().cancelTask(task);

        for (Player p: Bukkit.getOnlinePlayers()) {
            if (its.contains(p)) {
                p.sendMessage(ChatColor.AQUA + "You're it!");
            } else {
                p.sendMessage(ChatColor.AQUA + "You're not it :(");
                playersInGame++;
            }
        }


        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            public void run() {
                if (time <= 0) {
                    //game ended
                    end();
                    return;
                }

                if (outs.contains(time)) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendMessage(" Time Left: " + formatTime(time));
                    }
                }

                time--;
            }
        }, 20, 20); //20 ticks = 1 second
    }

    public String formatTime(int time) {
        int minutes = (int)Math.floor(time / 60);
        int seconds = (int)(time - (minutes * 60));

        String output = String.valueOf(minutes) + ":" + String.valueOf(seconds);

        // 2:0 -> 2:00
        // Converts 0 to double 0
        if (seconds == 0)
            output += "0";


        return output;
    }

    public void pickIts() {
        ArrayList<Player> Players = new ArrayList<>();
        for (Player p: Bukkit.getOnlinePlayers()) {
            Players.add(p);
        }

        Collections.shuffle(Players);

        // Get i amount of players
        for (int i = 0; i < taggers; i++) {
            its.add(Players.get(i));
        }
    }

}
