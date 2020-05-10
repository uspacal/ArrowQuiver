package me.uspacel.callisquiver;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
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
            if (NBTHelper.NORMAL_QUIVER.equals(NBTHelper.getString(middleitem, NBTHelper.ID))) {
                // Bukkit.broadcastMessage(">oh no, its a quiver!");
                event.getInventory().setResult(null);
            } else {
                ItemStack result = event.getInventory().getResult();
                // give unique user id (not stackable)
                String uuid = UUID.randomUUID().toString();
                Bukkit.getLogger().info(uuid);
                NBTHelper.setString(result, NBTHelper.UUID, UUID.randomUUID().toString());


            }
        }
        // else {
        // Bukkit.broadcastMessage("NE DU, DAS KLAPPT NCHT");

    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {

        if (NBTHelper.NORMAL_QUIVER.equals(NBTHelper.getString(event.getItemInHand(), NBTHelper.ID))) {
            // Bukkit.broadcastMessage(">oh no, its a quiver!");
            event.setCancelled(true);
        }
    }

    // load arrows
    @EventHandler
    public void onInventoryClickEventAdd(InventoryClickEvent event) {
        // Bukkit.broadcastMessage(">broken? event");
        if (!(event.getWhoClicked() instanceof Player)) return;
        // Bukkit.broadcastMessage(">broken? player");
        if (!(NBTHelper.NORMAL_QUIVER.equals(NBTHelper.getString(event.getCurrentItem(), NBTHelper.ID)))) return;
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR &&
                event.isRightClick() &&
                event.getCursor().getType() == Material.ARROW) {
            // Bukkit.broadcastMessage(">broken? action");

            // Bukkit.broadcastMessage(">broken? quiver");
            // get all needet values
            ItemStack arrow = event.getCursor();
            ItemStack quiver = event.getCurrentItem();
            int amount = arrow.getAmount();
            int maxArrows = NBTHelper.getInteger(quiver, NBTHelper.MAXIMAL_ARROWS);
            int accArrows = NBTHelper.getInteger(quiver, NBTHelper.ACTUAL_ARROWS);
            // calc left arrow space
            int leftAmount = maxArrows - accArrows;
            int fill = Math.min(leftAmount, amount);
            // Bukkit.broadcastMessage("" + fill);
            accArrows += fill;
            arrow.setAmount(amount - fill);
            NBTHelper.setInteger(quiver, NBTHelper.ACTUAL_ARROWS, accArrows);
            ItemMeta quivermeta = quiver.getItemMeta();
            ArrayList<String> lore = new ArrayList();
            lore.add("Arrows: " + accArrows + "/256");
            quivermeta.setLore(lore);
            quiver.setItemMeta(quivermeta);


            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Loaded " + fill + " Arrows! " + "(" + accArrows + "/ 256)");
            player.updateInventory();

        } else if (event.getAction() == InventoryAction.PICKUP_HALF) {
            Bukkit.broadcastMessage(event.getAction().toString());
            Bukkit.broadcastMessage("curser Null & right click");
            ItemStack quiver = event.getCurrentItem();
            int maxArrows = NBTHelper.getInteger(quiver, NBTHelper.MAXIMAL_ARROWS);
            int accArrows = NBTHelper.getInteger(quiver, NBTHelper.ACTUAL_ARROWS);
            // calc left arrow space

            int remove = ((accArrows >= 64) ? 64 : accArrows);
            accArrows = accArrows -= remove;
            Bukkit.broadcastMessage("" + remove);
            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.ARROW, remove));
            Bukkit.broadcastMessage(">broken? arrow");
            NBTHelper.setInteger(quiver, "accArrows", accArrows);
            ItemMeta quivermeta = quiver.getItemMeta();
            ArrayList<String> lore = new ArrayList();
            lore.add("Arrows: " + accArrows + "/256");
            quivermeta.setLore(lore);
            quiver.setItemMeta(quivermeta);


            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("Removed " + remove + " Arrows! " + "(" + accArrows + "/ 256)");
            player.updateInventory();

        }
    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        // Bukkit.broadcastMessage(">rightclick");
        if (!(event.getMaterial() == Material.BOW || event.getMaterial() == Material.CROSSBOW)) return;
        // Bukkit.broadcastMessage(">bow");
        Player player = event.getPlayer();
        if (player.getInventory().contains(Material.ARROW) ||
                player.getInventory().getItemInOffHand().getType() == Material.ARROW) return;
        ItemStack[] inventory = player.getInventory().getStorageContents();
        Integer placeQuiver = null;
        for (int i = 0; i < inventory.length; i++) {
            // Bukkit.broadcastMessage(""+ i);
            if (inventory[i] == null || !(inventory[i].getType() == Material.CHEST)) continue;
            // Bukkit.broadcastMessage("Item in slot");
            if (!("normalQuiver".equals(NBTHelper.getString(inventory[i], "id")))) continue;
            // Bukkit.broadcastMessage("Quiver found");
            if (1 > (NBTHelper.getInteger(inventory[i], NBTHelper.ACTUAL_ARROWS))) continue;
            // Bukkit.broadcastMessage("Arrow in quiver");
            placeQuiver = i;
            break;
        }
        if (placeQuiver == null) return;
        int empty = player.getInventory().firstEmpty();


        if (empty <= -1 && (player.getInventory().getItemInOffHand().getAmount() != 0)) return;

        if (player.getInventory().getItemInOffHand().getAmount() == 0) empty = 40;

        {
            ItemStack quiver = inventory[placeQuiver];
            int accArrows = NBTHelper.getInteger(quiver, NBTHelper.ACTUAL_ARROWS);
            accArrows--;
            player.getInventory().setItem(empty, new ItemStack(Material.ARROW, 1));
            NBTHelper.setInteger(quiver, NBTHelper.ACTUAL_ARROWS, accArrows);
            ItemMeta quivermeta = quiver.getItemMeta();
            ArrayList<String> lore = new ArrayList();
            lore.add("Arrows: " + accArrows + "/256");
            quivermeta.setLore(lore);
            quiver.setItemMeta(quivermeta);

        }


    }


}
