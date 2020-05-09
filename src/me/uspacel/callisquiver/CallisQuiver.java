package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

public class CallisQuiver extends JavaPlugin {

    public RecipeHolder recipeHolder;
    public static CallisQuiver plugin = null;

    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().info("start successful");
        recipeHolder = new RecipeHolder();

        getServer().getPluginManager().registerEvents(new ListenerRecipe(), this);

        // new item
        ItemStack quiver = new ItemStack(Material.CHEST);
        // item meta
        ItemMeta quivermeta = quiver.getItemMeta();
        // set display name
        quivermeta.setDisplayName("Arrow Quiver");
        // new list
        ArrayList<String> lore = new ArrayList();
        lore.add("Arrows: "+ 8 + "/256");

        //add lore
        quivermeta.setLore(lore);
        // display in item data
        quiver.setItemMeta(quivermeta);
        // add Sting
        NBTHelper.addString(quiver, Utility.NBTStrings.ID.string, Utility.NBTStrings.NORMAL_QUIVER.string);
        NBTHelper.addInteger(quiver, Utility.NBTStrings.MAXIMAL_ARROWS.string, 256 );
        NBTHelper.addInteger(quiver, Utility.NBTStrings.ACTUAL_ARROWS.string, 8 );
        // make namekey
        NamespacedKey quiverkey = new NamespacedKey(this, "arrow_quiver");
        // create recipe
        ShapedRecipe quiverrecipe = new ShapedRecipe(quiverkey, quiver);
        quiverrecipe.shape("AAA","ACA","AAA");
        quiverrecipe.setIngredient('A', Material.ARROW);
        quiverrecipe.setIngredient('C', Material.CHEST);
        Bukkit.addRecipe(quiverrecipe);
        // hold !BUKKIT! (BUKKIT changes recipe)recipe
        recipeHolder.recipes.put("quiver", Bukkit.getServer().getRecipesFor(quiver).get(0));



    }

    @Override
    public void onDisable() {

    }
}
