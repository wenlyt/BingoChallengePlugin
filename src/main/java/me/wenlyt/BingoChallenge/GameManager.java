package me.wenlyt.BingoChallenge;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

public class GameManager {

    private final Main plugin;
    private boolean gameActive = false;
    private final HashMap<UUID, List<String>> playerTasks = new HashMap<>();
    private int gameTime;
    private BukkitRunnable timerTask;

    private World world;
    private Set<Chunk> modifiedChunks = new HashSet<>();

    public GameManager(Main plugin) {
        this.plugin = plugin;
        this.gameTime = plugin.getConfig().getInt("game-time", 600);
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public List<String> getPlayerTasks(Player player) {
        return playerTasks.get(player.getUniqueId());
    }

    public void markTaskComplete(Player player, String task) {
        List<String> tasks = playerTasks.get(player.getUniqueId());
        tasks.remove(task);

        if (tasks.isEmpty()) {
            stopGame(player);
        }
    }

    public void startGame() {
        if (gameActive) {
            Bukkit.broadcastMessage("Игра уже началась!");
            return;
        }

        gameActive = true;
        world = Bukkit.getWorlds().get(0);

        modifiedChunks.clear();

        Bukkit.broadcastMessage("Игра началась! У вас есть " + gameTime / 60 + " минут, чтобы собрать ресурсы!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> tasks = TaskGenerator.generateTasks();
            playerTasks.put(player.getUniqueId(), tasks);

            player.sendMessage("Ваши задания:");
            for (String task : tasks) {
                player.sendMessage("- " + task);
            }
        }

        startTimer();
    }

    public void stopGame(Player winner) {
        gameActive = false;
        if (timerTask != null) {
            timerTask.cancel();
        }

        if (winner != null) {
            Bukkit.broadcastMessage("Игра завершена! Победитель: " + winner.getName());
        } else {
            Bukkit.broadcastMessage("Игра завершена! Время истекло, никто не победил.");
        }

        restoreChunks();

        playerTasks.clear();
    }

    private void startTimer() {
        timerTask = new BukkitRunnable() {
            int timeLeft = gameTime;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    stopGame(null);
                    cancel();
                    return;
                }

                if (timeLeft % 60 == 0 || timeLeft <= 10) {
                    Bukkit.broadcastMessage("Осталось " + (timeLeft / 60) + " минут!");
                }

                timeLeft--;
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L); // 20 тиков = 1 секунда
    }

    public void onBlockBreak(BlockBreakEvent event) {
        if (!gameActive) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        Chunk chunk = block.getChunk();
        modifiedChunks.add(chunk);
    }
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!gameActive) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();

        Chunk chunk = block.getChunk();
        modifiedChunks.add(chunk);
    }

    private void restoreChunks() {
        for (Chunk chunk : modifiedChunks) {
            chunk.unload(true);
            chunk.load(true);
        }

        modifiedChunks.clear();
    }
}