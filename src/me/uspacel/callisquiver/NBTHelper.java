package me.uspacel.callisquiver;



import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTHelper {
    public static void addString(ItemStack item, String key, String value){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.getPersistentDataContainer().set(nsk, PersistentDataType.STRING, value );
        item.setItemMeta(itemmeta);

    }
    public static String getString(ItemStack item, String key){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        String value = itemmeta.getPersistentDataContainer().get(nsk, PersistentDataType.STRING);
        return value;

    }

    public static void addInteger(ItemStack item, String key, Integer value){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.getPersistentDataContainer().set(nsk, PersistentDataType.INTEGER, value );
        item.setItemMeta(itemmeta);

    }
    public static Integer getInteger(ItemStack item, String key){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        Integer value = itemmeta.getPersistentDataContainer().get(nsk, PersistentDataType.INTEGER);
        return value;

    }

}
