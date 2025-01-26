package me.wenlyt.BingoChallenge;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskGenerator {

    private static final Material[] POSSIBLE_ITEMS = {
            Material.OAK_LOG, Material.STONE, Material.IRON_INGOT,
            Material.DIAMOND, Material.WHEAT, Material.COBBLESTONE
    };

    public static List<String> generateTasks() {
        List<String> tasks = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Material randomMaterial = POSSIBLE_ITEMS[(int) (Math.random() * POSSIBLE_ITEMS.length)];
            tasks.add(randomMaterial.toString());
        }

        return tasks;
    }
}
