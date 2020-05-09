package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

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
            if (nbt != null && nbt.equals("normalQuiver")) {
                // Bukkit.broadcastMessage(">oh no, its a quiver!");
                event.getInventory().setResult(null);
            } else {
                ItemStack result = event.getInventory().getResult();
                // give unique user id (not stackable)
                String uuid = UUID.randomUUID().toString();
                Bukkit.getLogger().info(uuid);
                NBTHelper.addString(result, "uuid", UUID.randomUUID().toString());


            }
        }
        // else {
        // Bukkit.broadcastMessage("NE DU, DAS KLAPPT NCHT");

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String nbt = NBTHelper.getString(item, "id");
        if (nbt != null && nbt.equals("normalQuiver")) {
            // Bukkit.broadcastMessage(">oh no, its a quiver!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        // Bukkit.broadcastMessage(">broken? event");
        if (!(event.getWhoClicked() instanceof Player)) return;
        // Bukkit.broadcastMessage(">broken? player");
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR &&
                event.isRightClick() &&
                event.getCursor().getType() == Material.ARROW) {
            // Bukkit.broadcastMessage(">broken? action");
            if (!("normalQuiver".equals(NBTHelper.getString(event.getCurrentItem(), "id")))) return;
            // Bukkit.broadcastMessage(">broken? quiver");
            // get all needet values
            ItemStack arrow = event.getCursor();
            ItemStack quiver = event.getCurrentItem();
            int amount = arrow.getAmount();
            int maxArrows = NBTHelper.getInteger(quiver, "maxArrows");
            int accArrows = NBTHelper.getInteger(quiver, "accArrows");
            // calc left arrow space
            int leftAmount = maxArrows - accArrows;
            int fill = Math.min(leftAmount, amount);
            // Bukkit.broadcastMessage("" + fill);
            accArrows += fill;
            arrow.setAmount(amount - fill);
            NBTHelper.addInteger(quiver, "accArrows", accArrows);
            ItemMeta quivermeta = quiver.getItemMeta();
            ArrayList<String> lore = new ArrayList();
            lore.add("Arrows: " + accArrows + "/256");
            quivermeta.setLore(lore);
            quiver.setItemMeta(quivermeta);


            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.updateInventory();
            player.sendMessage("Loaded "+ fill+ " Arrows!");


        }
    }
}
