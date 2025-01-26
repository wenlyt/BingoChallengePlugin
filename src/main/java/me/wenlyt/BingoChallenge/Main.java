package me.wenlyt.BingoChallenge;

import me.wenlyt.BingoChallenge.Commands.StartBingoCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin Enabled...");

        this.gameManager = new GameManager(this);

        getCommand("startrhunt").setExecutor(new StartBingoCommand(gameManager));

        Bukkit.getPluginManager().registerEvents(new GameListener(gameManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled...");
    }
}
