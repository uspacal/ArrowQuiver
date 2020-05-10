package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CallisQuiver extends JavaPlugin {

    public static RecipeHolder recipeHolder;
    public static CallisQuiver plugin = null;

    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().info("start successful");
        getServer().getPluginManager().registerEvents(new ListenerQuiver(), this);
        NewRecipe.addSimpleQuiver();
        this.getLogger().info("finisched loding");
    }

    @Override
    public void onDisable() {

    }
}
