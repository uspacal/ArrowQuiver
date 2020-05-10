package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class NewRecipe {
    public static void addSimpleQuiver() {
        ItemStack simplequiver = new ItemStack(Material.CHEST);
        ItemMeta quivermeta = simplequiver.getItemMeta();
        quivermeta.setDisplayName("§rSimple Quiver");
        ArrayList<String> lore = new ArrayList();
        lore.add("Arrows: "+ 8 + "/256");
        quivermeta.setLore(lore);
        simplequiver.setItemMeta(quivermeta);
        NBTHelper.setString(simplequiver, NBTHelper.ID, NBTHelper.NORMAL_QUIVER);
        NBTHelper.setInteger(simplequiver, NBTHelper.MAXIMAL_ARROWS, 256 );
        NBTHelper.setInteger(simplequiver, NBTHelper.ACTUAL_ARROWS, 8 );
        NamespacedKey quiverkey = new NamespacedKey(CallisQuiver.plugin, "arrow_quiver");
        ShapedRecipe quiverrecipe = new ShapedRecipe(quiverkey, simplequiver);
        quiverrecipe.shape("AAA","ACA","AAA");
        quiverrecipe.setIngredient('A', Material.ARROW);
        quiverrecipe.setIngredient('C', Material.CHEST);
        Bukkit.addRecipe(quiverrecipe);
        CallisQuiver.recipeHolder.recipes.put("simplequiver", Bukkit.getServer().getRecipesFor(simplequiver).get(0));


    }
    public void addRecipeMeta(ItemStack item, ItemMeta name) {

    }
}
