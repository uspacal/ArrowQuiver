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
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR &&
                event.isRightClick() &&
                event.getCursor().getType() == Material.ARROW) {
            // Bukkit.broadcastMessage(">broken? action");
            if (!(NBTHelper.NORMAL_QUIVER.equals(NBTHelper.getString(event.getCurrentItem(), NBTHelper.ID)))) return;
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

        }
    }

    // remove arrows
    @EventHandler
    public void onInventoryClickEventRemove(InventoryClickEvent event) {
        // Bukkit.broadcastMessage(">broken? event");
        if (!(event.getWhoClicked() instanceof Player)) return;
        // Bukkit.broadcastMessage(">broken? player");
        if (event.isRightClick() && event.getCursor() == null) {
            Bukkit.broadcastMessage(">broken? action");
            if (!("normalQuiver".equals(NBTHelper.getString(event.getCurrentItem(), "id")))) return;
            Bukkit.broadcastMessage(">broken? quiver");
            // get all needet values
            ItemStack quiver = event.getCurrentItem();
            int maxArrows = NBTHelper.getInteger(quiver, "maxArrows");
            int accArrows = NBTHelper.getInteger(quiver, "accArrows");
            // calc left arrow space

            int remove = ((accArrows >= 64) ? 64 : accArrows);
            accArrows = accArrows -= remove;
            Bukkit.broadcastMessage("" + remove);
            event.setCursor(new ItemStack(Material.ARROW, remove));
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
    public void onPlayerInteractBowEvent(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        // Bukkit.broadcastMessage(">rightclick");
        if (!(event.getMaterial() == Material.BOW)) return;
        // Bukkit.broadcastMessage(">bow");
        Player player = event.getPlayer();
        ItemStack[] inventory = player.getInventory().getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            // Bukkit.broadcastMessage(""+ i);
            if (inventory[i] == null || !(inventory[i].getType() == Material.CHEST)) continue;
            // Bukkit.broadcastMessage("Item in slot");
            if (!("normalQuiver".equals(NBTHelper.getString(inventory[i], "id")))) continue;
            // Bukkit.broadcastMessage("Quiver found");
            if (1 > (NBTHelper.getInteger(inventory[i], "accArrows"))) continue;
            // Bukkit.broadcastMessage("Arrow in quiver");
            int empty = player.getInventory().firstEmpty();

            if (empty <= -1) {
                continue;
            } else {
                ItemStack quiver = inventory[i];
                int accArrows = NBTHelper.getInteger(quiver, "accArrows");
                accArrows--;
                player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                NBTHelper.setInteger(quiver, "accArrows", accArrows);
                ItemMeta quivermeta = quiver.getItemMeta();
                ArrayList<String> lore = new ArrayList();
                lore.add("Arrows: " + accArrows + "/256");
                quivermeta.setLore(lore);
                quiver.setItemMeta(quivermeta);
                return;
            }


        }
    }

    @EventHandler
    public void onPlayerInteractCrossbowEvent(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        // Bukkit.broadcastMessage(">rightclick");
        if (!(event.getMaterial() == Material.CROSSBOW)) return;
        // Bukkit.broadcastMessage(">crossbow");
        Player player = event.getPlayer();
        ItemStack[] inventory = player.getInventory().getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            // Bukkit.broadcastMessage(""+ i);
            if (inventory[i] == null || !(inventory[i].getType() == Material.CHEST)) continue;

            // Bukkit.broadcastMessage("Item in slot");
            if (!("normalQuiver".equals(NBTHelper.getString(inventory[i], "id")))) continue;
            // Bukkit.broadcastMessage("Quiver found");
            if (1 > (NBTHelper.getInteger(inventory[i], "accArrows"))) continue;
            // Bukkit.broadcastMessage("Arrow in quiver");

            if (!(NBTHelper.NORMAL_QUIVER.equals(NBTHelper.getString(inventory[i], NBTHelper.ID)))) continue;
            if (1 > (NBTHelper.getInteger(inventory[i], NBTHelper.ACTUAL_ARROWS))) continue;

            int empty = player.getInventory().firstEmpty();

            if (empty <= -1) {
                continue;
            } else {
                ItemStack quiver = inventory[i];
                int accArrows = NBTHelper.getInteger(quiver, "accArrows");
                accArrows--;
                player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                NBTHelper.setInteger(quiver, "accArrows", accArrows);
                ItemMeta quivermeta = quiver.getItemMeta();
                ArrayList<String> lore = new ArrayList();
                lore.add("Arrows: " + accArrows + "/256");
                quivermeta.setLore(lore);
                quiver.setItemMeta(quivermeta);
                return;
            }


        }
    }
}
