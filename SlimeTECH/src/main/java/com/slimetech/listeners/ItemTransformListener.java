package com.slimetech.listeners;

import com.slimetech.item.ItemManager;
import com.slimetech.item.MaterialMapper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class ItemTransformListener implements Listener {
    
    private final ItemManager itemManager = ItemManager.getInstance();
    private final MaterialMapper materialMapper = MaterialMapper.getInstance();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Transform copper ingots when player joins
        Bukkit.getScheduler().runTaskLater(com.slimetech.SlimeTECH.getInstance(), () -> {
            transformCopperIngots(event.getPlayer());
        }, 20L); // 1 second delay
    }
    
    // Make this method public so command can access it
    public void transformCopperIngots(org.bukkit.entity.Player player) {
        // Check inventory
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && itemManager.isSlimeTECHCopperIngot(item)) {
                ItemStack transformed = transformCopperIngot(item);
                if (!transformed.equals(item)) {
                    player.getInventory().setItem(i, transformed);
                }
            }
        }
    }
    
    private ItemStack transformCopperIngot(ItemStack oldItem) {
        // Get current version's copper ingot material
        org.bukkit.Material currentMaterial = materialMapper.getCopperIngotMaterial();
        
        // If already correct material, return as-is
        if (oldItem.getType() == currentMaterial) {
            return oldItem;
        }
        
        // Create new item with current version's material
        ItemStack newItem = new ItemStack(currentMaterial, oldItem.getAmount());
        org.bukkit.inventory.meta.ItemMeta oldMeta = oldItem.getItemMeta();
        org.bukkit.inventory.meta.ItemMeta newMeta = newItem.getItemMeta();
        
        if (oldMeta != null && newMeta != null) {
            // Copy the display name (always "Copper Ingot")
            newMeta.setDisplayName(oldMeta.getDisplayName());
            
            // Copy the SlimeTECH data
            org.bukkit.persistence.PersistentDataContainer oldPdc = oldMeta.getPersistentDataContainer();
            org.bukkit.persistence.PersistentDataContainer newPdc = newMeta.getPersistentDataContainer();
            
            // Copy all SlimeTECH tags
            if (oldPdc.has(
                new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "slimetech_copper"),
                org.bukkit.persistence.PersistentDataType.STRING
            )) {
                newPdc.set(
                    new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "slimetech_copper"),
                    org.bukkit.persistence.PersistentDataType.STRING,
                    "true"
                );
            }
            
            if (oldPdc.has(
                new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "item_id"),
                org.bukkit.persistence.PersistentDataType.STRING
            )) {
                String itemId = oldPdc.get(
                    new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "item_id"),
                    org.bukkit.persistence.PersistentDataType.STRING
                );
                newPdc.set(
                    new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "item_id"),
                    org.bukkit.persistence.PersistentDataType.STRING,
                    itemId
                );
            }
            
            newItem.setItemMeta(newMeta);
        }
        
        return newItem;
    }
}