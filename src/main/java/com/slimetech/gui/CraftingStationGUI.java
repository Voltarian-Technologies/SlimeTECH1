package com.slimetech.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CraftingStationGUI {
    
    private final GUIManager guiManager = GUIManager.getInstance();
    
    /**
     * Opens the crafting station GUI
     */
    public void openCraftingStation(Player player) {
        // Create protected GUI
        Inventory gui = guiManager.createProtectedGUI("&8&lSlimeTECH Crafting Station", player);
        
        // Fill the GUI with your items
        setupCraftingGrid(gui);
        setupCategoryButtons(gui);
        setupControlButtons(gui);
        
        // Open for player
        guiManager.openGUI(player, gui);
    }
    
    /**
     * Sets up the 3x3 crafting grid (slots 10-16, 19-25, 28-34)
     */
    private void setupCraftingGrid(Inventory gui) {
        // Crafting grid area (3x3 grid)
        int[] craftingSlots = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
        };
        
        // Fill crafting area with placeholder items
        for (int slot : craftingSlots) {
            guiManager.setItem(gui, slot, 
                guiManager.createGuiItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "&7Crafting Slot"));
        }
        
        // Result slot (slot 23 is center, but let's use slot 24 for result)
        guiManager.setItem(gui, 24, 
            guiManager.createGuiItem(Material.BARRIER, "&cResult", "&7Place items to see result"));
    }
    
    /**
     * Sets up category selection buttons on the left
     */
    private void setupCategoryButtons(Inventory gui) {
        // Category buttons column (slots 9, 18, 27, 36, 45)
        guiManager.addButton(gui, 9, "category_basic", 
            Material.FURNACE, "&6Basic Machines", 
            "&7Click to view basic machines",
            "&7Generators, processors, etc.");
        
        guiManager.addButton(gui, 18, "category_energy", 
            Material.REDSTONE, "&cEnergy Systems", 
            "&7Click to view energy items",
            "&7Generators, batteries, wires");
        
        guiManager.addButton(gui, 27, "category_storage", 
            Material.CHEST, "&eStorage & Transport", 
            "&7Click to view storage systems",
            "&7Chests, transporters, pipes");
        
        guiManager.addButton(gui, 36, "category_magic", 
            Material.ENCHANTING_TABLE, "&5Magic Items", 
            "&7Click to view magical items",
            "&7Wands, altars, spells");
        
        guiManager.addButton(gui, 45, "category_tools", 
            Material.DIAMOND_PICKAXE, "&bTools & Gadgets", 
            "&7Click to view tools",
            "&7Upgraded tools and gadgets");
    }
    
    /**
     * Sets up control buttons at the bottom
     */
    private void setupControlButtons(Inventory gui) {
        // Bottom row control buttons
        guiManager.addButton(gui, 48, "back_button", 
            Material.ARROW, "&fBack", 
            "&7Return to previous menu");
        
        guiManager.addButton(gui, 49, "close_gui", 
            Material.BARRIER, "&cClose", 
            "&7Close this menu");
        
        guiManager.addButton(gui, 50, "next_page", 
            Material.ARROW, "&fNext Page", 
            "&7Go to next page");
        
        // Info book
        guiManager.addButton(gui, 53, "info_book", 
            Material.BOOK, "&6SlimeTECH Guide", 
            "&7Click to open guide book",
            "&7Learn about SlimeTECH items");
    }
    
    /**
     * Example of how to populate with actual items
     */
    public void showCategory(Player player, String category) {
        Inventory gui = guiManager.createProtectedGUI("&8&lSlimeTECH - " + category, player);
        
        // Clear the grid area using itemSlots
        int[] itemSlots = {20, 21, 22, 23, 24, 29, 30, 31, 32};
        
        for (int slot : itemSlots) {
            // Clear slots with placeholder
            guiManager.setItem(gui, slot, 
                guiManager.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "&7Available Slot"));
        }
        
        // Example items for the category
        switch (category.toLowerCase()) {
            case "basic":
                guiManager.setItem(gui, 20, createCopperIngotDisplay());
                guiManager.setItem(gui, 21, createSolarPanelDisplay());
                guiManager.setItem(gui, 22, createElectricFurnaceDisplay());
                break;
            case "energy":
                guiManager.setItem(gui, 20, createBatteryDisplay());
                guiManager.setItem(gui, 21, createGeneratorDisplay());
                guiManager.setItem(gui, 22, createWireDisplay());
                break;
            case "storage":
                guiManager.setItem(gui, 20, createStorageChestDisplay());
                guiManager.setItem(gui, 21, createItemPipeDisplay());
                break;
            case "magic":
                guiManager.setItem(gui, 20, createMagicWandDisplay());
                guiManager.setItem(gui, 21, createEnchantingAltarDisplay());
                break;
            case "tools":
                guiManager.setItem(gui, 20, createElectricPickaxeDisplay());
                guiManager.setItem(gui, 21, createScannerDisplay());
                break;
        }
        
        // Add back button
        guiManager.addButton(gui, 45, "back_to_main", 
            Material.ARROW, "&fBack to Main", 
            "&7Return to main menu");
        
        guiManager.openGUI(player, gui);
    }
    
    // Helper methods to create display items
    private org.bukkit.inventory.ItemStack createCopperIngotDisplay() {
        return guiManager.createGuiItem(
            Material.COPPER_INGOT,
            "&fCopper Ingot",
            "&7Version-adaptive material",
            "&7Shows as brick in 1.16.5",
            "&7Shows as copper in 1.17+",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createSolarPanelDisplay() {
        return guiManager.createGuiItem(
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&bSolar Panel",
            "&7Generates energy from sunlight",
            "&e⚡ 16 FE/t (daytime)",
            "&e⚡ 4 FE/t (night)",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createElectricFurnaceDisplay() {
        return guiManager.createGuiItem(
            Material.BLAST_FURNACE,
            "&6Electric Furnace",
            "&7Smelts items using electricity",
            "&e⚡ 32 FE/t",
            "&e⏱️ 2x faster than regular",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createBatteryDisplay() {
        return guiManager.createGuiItem(
            Material.REDSTONE_BLOCK,
            "&cEnergy Cell",
            "&7Stores electrical energy",
            "&e⚡ Capacity: 100,000 FE",
            "&e⚡ Transfer: 1,000 FE/t",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createGeneratorDisplay() {
        return guiManager.createGuiItem(
            Material.FURNACE,
            "&6Coal Generator",
            "&7Burns fuel to generate power",
            "&e⚡ 80 FE/t from coal",
            "&e⛽ Burns: Coal, charcoal",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createWireDisplay() {
        return guiManager.createGuiItem(
            Material.REDSTONE,
            "&cEnergy Wire",
            "&7Transmits energy between machines",
            "&e⚡ Transfer: 512 FE/t",
            "&e📏 Range: 16 blocks",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createStorageChestDisplay() {
        return guiManager.createGuiItem(
            Material.CHEST,
            "&eAdvanced Chest",
            "&7Stores 108 slots of items",
            "&e🔗 Auto-sorting available",
            "&e🔒 Lockable with permissions",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createItemPipeDisplay() {
        return guiManager.createGuiItem(
            Material.HOPPER,
            "&eItem Pipe",
            "&7Transports items between inventories",
            "&e📦 64 items/second",
            "&e🔀 Filtering available",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createMagicWandDisplay() {
        return guiManager.createGuiItem(
            Material.STICK,
            "&5Magic Wand",
            "&7Casts basic spells",
            "&e✨ Fireball, Teleport, Heal",
            "&e🔋 Requires mana",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createEnchantingAltarDisplay() {
        return guiManager.createGuiItem(
            Material.ENCHANTING_TABLE,
            "&5Enchanting Altar",
            "&7Creates custom enchantments",
            "&e⚡ Requires magical energy",
            "&e📚 Books and XP needed",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createElectricPickaxeDisplay() {
        return guiManager.createGuiItem(
            Material.DIAMOND_PICKAXE,
            "&bElectric Pickaxe",
            "&7Mines blocks with energy",
            "&e⚡ 10 FE per block",
            "&e⛏️ 3x faster than diamond",
            "",
            "&eClick to view recipes"
        );
    }
    
    private org.bukkit.inventory.ItemStack createScannerDisplay() {
        return guiManager.createGuiItem(
            Material.SPYGLASS,
            "&bOre Scanner",
            "&7Detects nearby ores",
            "&e⚡ 100 FE per scan",
            "&e📡 32 block range",
            "",
            "&eClick to view recipes"
        );
    }
}