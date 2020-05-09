package me.uspacel.callisquiver;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
            if (Utility.NBTStrings.NORMAL_QUIVER.string.equals(NBTHelper.getString(middleitem, Utility.NBTStrings.ID.string))) {
                // Bukkit.broadcastMessage(">oh no, its a quiver!");
                event.getInventory().setResult(null);
            } else {
                ItemStack result = event.getInventory().getResult();
                // give unique user id (not stackable)
                String uuid = UUID.randomUUID().toString();
                Bukkit.getLogger().info(uuid);
                NBTHelper.addString(result, Utility.NBTStrings.UUID.string, UUID.randomUUID().toString());


            }
        }
        // else {
        // Bukkit.broadcastMessage("NE DU, DAS KLAPPT NCHT");

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        if (Utility.NBTStrings.NORMAL_QUIVER.string.equals(NBTHelper.getString(event.getItemInHand(), Utility.NBTStrings.ID.string))) {
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
            if (!(Utility.NBTStrings.NORMAL_QUIVER.string.equals(NBTHelper.getString(event.getCurrentItem(), Utility.NBTStrings.ID.string)))) return;
            // Bukkit.broadcastMessage(">broken? quiver");
            // get all needet values
            ItemStack arrow = event.getCursor();
            ItemStack quiver = event.getCurrentItem();
            int amount = arrow.getAmount();
            int maxArrows = NBTHelper.getInteger(quiver, Utility.NBTStrings.MAXIMAL_ARROWS.string);
            int accArrows = NBTHelper.getInteger(quiver, Utility.NBTStrings.ACTUAL_ARROWS.string);
            // calc left arrow space
            int leftAmount = maxArrows - accArrows;
            int fill = Math.min(leftAmount, amount);
            // Bukkit.broadcastMessage("" + fill);
            accArrows += fill;
            arrow.setAmount(amount - fill);
            NBTHelper.addInteger(quiver, Utility.NBTStrings.ACTUAL_ARROWS.string, accArrows);
            ItemMeta quivermeta = quiver.getItemMeta();
            ArrayList<String> lore = new ArrayList();
            lore.add("Arrows: " + accArrows + "/256");
            quivermeta.setLore(lore);
            quiver.setItemMeta(quivermeta);


            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.updateInventory();
            player.sendMessage("Loaded " + fill + " Arrows!");


        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        // Bukkit.broadcastMessage(">rightclick");
        if (!(event.getMaterial() == Material.BOW)) return;
        // Bukkit.broadcastMessage(">bow");
        Player player = event.getPlayer();
        ItemStack[] inventory = player.getInventory().getStorageContents();
        for (int i = 0; i < inventory.length; i++) {

            if (inventory[i] == null || !(inventory[i].getType() == Material.CHEST)) continue;
            if (!(Utility.NBTStrings.NORMAL_QUIVER.string.equals(NBTHelper.getString(inventory[i], Utility.NBTStrings.ID.string)))) continue;
            if (1 > (NBTHelper.getInteger(inventory[i], Utility.NBTStrings.ACTUAL_ARROWS.string))) continue;
            int empty = player.getInventory().firstEmpty();
            if (empty <= -1) {
                continue;
            } else {
                player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            }


        }
    }
}
