package com.mcdragonmasters.PotatoSurvival.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiscoverRecipesOnJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        List<NamespacedKey> recipes = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof Keyed) {
                NamespacedKey key = ((Keyed) recipe).getKey();
                recipes.add(key);
            }
        }
        e.getPlayer().discoverRecipes(recipes);
    }

}
