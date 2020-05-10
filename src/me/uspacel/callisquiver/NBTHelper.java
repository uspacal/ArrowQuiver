package me.uspacel.callisquiver;



import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTHelper {

    public static final String ACTUAL_ARROWS = "AccArrows";
    public static final String MAXIMAL_ARROWS = "maxArrows";
    public static final String ID = "id";
    public static final String NORMAL_QUIVER = "normalQuiver";
    public static final String UUID = "uuid";

    public static void setString(ItemStack item, String key, String value){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.getPersistentDataContainer().set(nsk, PersistentDataType.STRING, value );
        item.setItemMeta(itemmeta);

    }
    public static String getString(ItemStack item, String key) {
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        String value = null;
        try {
            ItemMeta itemmeta = item.getItemMeta();
            value = itemmeta.getPersistentDataContainer().get(nsk, PersistentDataType.STRING);
        } catch (NullPointerException e){

        }
        return value;

    }

    public static void setInteger(ItemStack item, String key, Integer value){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.getPersistentDataContainer().set(nsk, PersistentDataType.INTEGER, value );
        item.setItemMeta(itemmeta);

    }
    public static Integer getInteger(ItemStack item, String key){
        NamespacedKey nsk = new NamespacedKey(CallisQuiver.plugin, key);
        // set item meta
        Integer value = null;
        try {
            ItemMeta itemmeta = item.getItemMeta();
            value = itemmeta.getPersistentDataContainer().get(nsk, PersistentDataType.INTEGER);
        } catch(NullPointerException e){

        }
        return value;

    }

}
