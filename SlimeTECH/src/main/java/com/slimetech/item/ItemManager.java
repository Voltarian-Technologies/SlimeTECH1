package com.slimetech.item;

import org.bukkit.inventory.ItemStack;

public class ItemManager {
    private static ItemManager instance;
    private final SlimeTECHItem copperIngotItem;
    
    private ItemManager() {
        copperIngotItem = new SlimeTECHItem("copper_ingot", "Copper Ingot");
    }
    
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }
    
    public ItemStack createCopperIngot(int amount) {
        return copperIngotItem.createItemStack(amount);
    }
    
    public boolean isSlimeTECHCopperIngot(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
        
        return pdc.has(
            new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "slimetech_copper"),
            org.bukkit.persistence.PersistentDataType.STRING
        );
    }
}