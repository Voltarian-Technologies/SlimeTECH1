package com.slimetech.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SlimeTECHItem {
    private final String itemId;
    private final String displayName;
    
    public SlimeTECHItem(String itemId, String displayName) {
        this.itemId = itemId;
        this.displayName = displayName;
    }
    
    public Material getVisualMaterial() {
        if (itemId.equals("copper_ingot")) {
            return MaterialMapper.getInstance().getCopperIngotMaterial();
        }
        return Material.STONE;
    }
    
    public ItemStack createItemStack(int amount) {
        Material visualMaterial = getVisualMaterial();
        ItemStack item = new ItemStack(visualMaterial, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Always show as "Copper Ingot" even when it's a brick
            meta.displayName(Component.text(displayName, NamedTextColor.WHITE));
            
            // Mark it as a SlimeTECH copper ingot
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "slimetech_copper"),
                   PersistentDataType.STRING, "true");
            pdc.set(new NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "item_id"),
                   PersistentDataType.STRING, itemId);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public String getItemId() { return itemId; }
    public String getDisplayName() { return displayName; }
}