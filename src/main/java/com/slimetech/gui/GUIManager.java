package com.slimetech.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIManager implements Listener {
    
    private static GUIManager instance;
    private final Map<UUID, Inventory> openGUIs = new HashMap<>();
    private final Map<String, GUIButton> buttonHandlers = new HashMap<>();
    
    public static GUIManager getInstance() {
        if (instance == null) {
            instance = new GUIManager();
        }
        return instance;
    }
    
    private GUIManager() {
        // Register default button handlers
        registerDefaultButtons();
    }
    
    /**
     * Creates a protected double chest GUI
     * @param title The GUI title
     * @param player The player opening the GUI
     * @return The created inventory
     */
    public Inventory createProtectedGUI(String title, Player player) {
        // Create double chest inventory (54 slots) with modern API
        Inventory gui = Bukkit.createInventory(null, 54, 
            net.kyori.adventure.text.Component.text(ChatColor.translateAlternateColorCodes('&', title)));
        
        // Store that this player has a GUI open
        openGUIs.put(player.getUniqueId(), gui);
        
        // Fill borders with glass panes (optional)
        fillBorders(gui, Material.BLACK_STAINED_GLASS_PANE);
        
        return gui;
    }
    
    /**
     * Fills the border slots with a specific material
     */
    private void fillBorders(Inventory inventory, Material borderMaterial) {
        // Top row (slots 0-8)
        for (int i = 0; i < 9; i++) {
            setItem(inventory, i, createGuiItem(borderMaterial, " "));
        }
        
        // Bottom row (slots 45-53)
        for (int i = 45; i < 54; i++) {
            setItem(inventory, i, createGuiItem(borderMaterial, " "));
        }
        
        // Left column (slots 9, 18, 27, 36)
        for (int i = 9; i < 54; i += 9) {
            setItem(inventory, i, createGuiItem(borderMaterial, " "));
        }
        
        // Right column (slots 17, 26, 35, 44)
        for (int i = 17; i < 54; i += 9) {
            setItem(inventory, i, createGuiItem(borderMaterial, " "));
        }
    }
    
    /**
     * Sets an item at a specific slot with protection
     */
    public void setItem(Inventory inventory, int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }
    
    /**
     * Creates a GUI item with name and lore
     */
    public ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Use Adventure Component for display name
            meta.displayName(net.kyori.adventure.text.Component.text()
                .content(ChatColor.translateAlternateColorCodes('&', name))
                .build());
            
            if (lore.length > 0) {
                java.util.List<net.kyori.adventure.text.Component> loreComponents = 
                    new java.util.ArrayList<>();
                for (String line : lore) {
                    loreComponents.add(net.kyori.adventure.text.Component.text()
                        .content(ChatColor.translateAlternateColorCodes('&', line))
                        .build());
                }
                meta.lore(loreComponents);
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Registers a button handler for a specific item
     */
    public void registerButton(String buttonId, GUIButton handler) {
        buttonHandlers.put(buttonId, handler);
    }
    
    /**
     * Adds a clickable button to the GUI
     */
    public void addButton(Inventory inventory, int slot, String buttonId, 
                         Material material, String name, String... lore) {
        ItemStack button = createGuiItem(material, name, lore);
        
        // Add custom NBT or metadata to identify as button
        ItemMeta meta = button.getItemMeta();
        org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(
            new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "gui_button"),
            org.bukkit.persistence.PersistentDataType.STRING,
            buttonId
        );
        button.setItemMeta(meta);
        
        setItem(inventory, slot, button);
    }
    
    /**
     * Opens the GUI for a player
     */
    public void openGUI(Player player, Inventory gui) {
        player.openInventory(gui);
        openGUIs.put(player.getUniqueId(), gui);
    }
    
    // Event handlers to protect the GUI
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        
        // Check if this is one of our GUIs
        if (clickedInventory != null && openGUIs.containsValue(clickedInventory)) {
            event.setCancelled(true); // Prevent taking/moving items
            
            // Check if clicked item is a button
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
                
                String buttonId = pdc.get(
                    new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "gui_button"),
                    org.bukkit.persistence.PersistentDataType.STRING
                );
                
                if (buttonId != null && buttonHandlers.containsKey(buttonId)) {
                    // Execute button action
                    buttonHandlers.get(buttonId).onClick(player, event.getSlot());
                }
            }
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        // Player player = (Player) event.getWhoClicked(); // Removed unused variable
        
        // Check if any dragged slot is in our GUI
        for (int slot : event.getRawSlots()) {
            Inventory inventory = event.getView().getInventory(slot);
            if (openGUIs.containsValue(inventory)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        
        Player player = (Player) event.getPlayer();
        openGUIs.remove(player.getUniqueId());
    }
    
    private void registerDefaultButtons() {
        // Example button handlers
        registerButton("close_gui", (player, slot) -> {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "GUI closed!");
        });
        
        registerButton("back_button", (player, slot) -> {
            player.sendMessage(ChatColor.YELLOW + "Back button clicked at slot " + slot + "!");
        });
        
        registerButton("next_page", (player, slot) -> {
            player.sendMessage(ChatColor.BLUE + "Next page from slot " + slot + "!");
        });
        
        registerButton("guide_back_main", (player, slot) -> {
            new GuideBook().openGuideBook(player);
            player.sendMessage(ChatColor.YELLOW + "Returning to main guide...");
        });
        
        registerButton("back_to_main", (player, slot) -> {
            new CraftingStationGUI().openCraftingStation(player);
            player.sendMessage(ChatColor.YELLOW + "Returning to main crafting menu...");
        });
    }
    
    // Interface for button actions
    public interface GUIButton {
        void onClick(Player player, int slot);
    }
}