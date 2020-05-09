package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Recipe;

public class ListenerRecipe implements Listener {

    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {

        if (event.getRecipe() == null) {
            return;

        }
        Recipe eventrecipe = event.getRecipe();
        if (CallisQuiver.plugin.recipeHolder.recipes.contains(eventrecipe) == true) {
            Bukkit.broadcastMessage("CRAFTING IS GREAT!");

        }
        else {
            Bukkit.broadcastMessage("NE DU, DAS KLAPPT NCHT");
        }
    }

}
