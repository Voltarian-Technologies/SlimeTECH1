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
        
        // Also check armor slots and offhand
        checkAndTransformArmor(player);
        checkAndTransformOffhand(player);
    }
    
    private void checkAndTransformArmor(org.bukkit.entity.Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        boolean changed = false;
        
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item != null && itemManager.isSlimeTECHCopperIngot(item)) {
                ItemStack transformed = transformCopperIngot(item);
                if (!transformed.equals(item)) {
                    armor[i] = transformed;
                    changed = true;
                }
            }
        }
        
        if (changed) {
            player.getInventory().setArmorContents(armor);
        }
    }
    
    private void checkAndTransformOffhand(org.bukkit.entity.Player player) {
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (offhand != null && itemManager.isSlimeTECHCopperIngot(offhand)) {
            ItemStack transformed = transformCopperIngot(offhand);
            if (!transformed.equals(offhand)) {
                player.getInventory().setItemInOffHand(transformed);
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
            if (oldMeta.hasDisplayName()) {
                newMeta.displayName(oldMeta.displayName());
            }
            
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
            
            // Copy other metadata if needed
            if (oldMeta.hasLore()) {
                newMeta.lore(oldMeta.lore());
            }
            
            if (oldMeta.hasCustomModelData()) {
                newMeta.setCustomModelData(oldMeta.getCustomModelData());
            }
            
            if (oldMeta.hasEnchants()) {
                for (org.bukkit.enchantments.Enchantment enchant : oldMeta.getEnchants().keySet()) {
                    int level = oldMeta.getEnchantLevel(enchant);
                    newMeta.addEnchant(enchant, level, true);
                }
            }
            
            // Copy item flags
            for (org.bukkit.inventory.ItemFlag flag : oldMeta.getItemFlags()) {
                newMeta.addItemFlags(flag);
            }
            
            newItem.setItemMeta(newMeta);
        }
        
        return newItem;
    }
}