package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

public class Utility {
    /**
     * Compares Recipe
     * @param r1 first recipe
     * @param r2 second recipe
     * @return true/ false
     */
    public static boolean compareRecipes(Recipe r1, Recipe r2) {
        if (r1 == r2) return true;
        // Bukkit.broadcastMessage(">anfang");
        if (r2 instanceof ShapedRecipe && r1 instanceof ShapedRecipe)  {
            // Bukkit.broadcastMessage(">SHAPED");
            Map<Character, ItemStack> ri1 = ((ShapedRecipe) r2).getIngredientMap();
            Bukkit.getLogger().info(ri1.toString());
            Map<Character, ItemStack> ri2 = ((ShapedRecipe) r1).getIngredientMap();
            // Bukkit.broadcastMessage(ri1.toString());
            // Bukkit.broadcastMessage(ri2.toString());
            if (ri1.size() != ri2.size()) return false;
            for (Map.Entry<Character, ItemStack> entry : ri1.entrySet()) {
                if (ri2.get(entry.getKey()).getType() != entry.getValue().getType()) return false;
            }
            // Bukkit.broadcastMessage(">SAME INGR");
            String[] rs1 = ((ShapedRecipe) r2).getShape();
            String[] rs2 = ((ShapedRecipe) r1).getShape();
            if (rs1.length != rs2.length) return false;
            for (int i = 0; i < rs1.length; i++) {
                if (!rs1[i].equals(rs2[i])) return false;
            }
            // Bukkit.broadcastMessage(">SAME SHAPE");

            return true;
        }
        // Bukkit.broadcastMessage(">not shaped");


        return false;
    }
}
