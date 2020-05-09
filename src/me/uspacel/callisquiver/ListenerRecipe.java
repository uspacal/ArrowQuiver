package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class ListenerRecipe implements Listener {
    // try to craft
    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
        // if recipe is valid
        if (event.getRecipe() == null) {
            return;

        }
        // if my recipe
        Recipe eventrecipe = event.getRecipe();
        if (Utility.compareRecipes(eventrecipe, CallisQuiver.plugin.recipeHolder.recipes.get("quiver"))) {
            // Bukkit.broadcastMessage("CRAFTING IS GREAT!");
            ItemStack middleitem = event.getInventory().getMatrix()[4];
            String nbt = NBTHelper.getString(middleitem, "id");
            if (nbt != null && nbt.equals("normalQuiver")){
               // Bukkit.broadcastMessage(">oh no, its a quiver!");
                event.getInventory().setResult(null);
            }
        }
        // else {
            // Bukkit.broadcastMessage("NE DU, DAS KLAPPT NCHT");

    }

}
