package com.mcdragonmasters.potatosurvival.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Utils {
    @SuppressWarnings("deprecation")
    public static ItemStack smeltedForm(ItemStack item) {
        Material type = item.getType();
        for (@NotNull Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            if (it.next() instanceof BlastingRecipe recipe){
                Material ingredient = recipe.getInput().getType();
                if (type == ingredient){
                    ItemStack result = item.clone();
                    result.setType(recipe.getResult().getType());
                    return result;
                }
            }
        }
        return item;
    }
}
