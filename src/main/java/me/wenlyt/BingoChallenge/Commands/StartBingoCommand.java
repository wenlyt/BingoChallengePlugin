package me.wenlyt.BingoChallenge.Commands;

import me.wenlyt.BingoChallenge.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartBingoCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartBingoCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            gameManager.startGame();
            return true;
        }
        return false;
    }
}
