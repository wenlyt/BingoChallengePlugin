package me.wenlyt.BingoChallenge;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.List;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!gameManager.isGameActive()) return;

        Player player = event.getPlayer();
        Material itemType = event.getItem().getItemStack().getType();

        List<String> tasks = gameManager.getPlayerTasks(player);

        if (tasks.contains(itemType.toString())) {
            player.sendMessage("Вы собрали: " + itemType);
            gameManager.markTaskComplete(player, itemType.toString());
        }
    }
}
