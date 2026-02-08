package com.slimetech.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuideBook {
    
    private final GUIManager guiManager = GUIManager.getInstance();
    
    /**
     * Creates the SlimeTECH Guide Book item
     */
    public ItemStack createGuideBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        
        if (meta != null) {
            // Set display name using Adventure API
            meta.displayName(net.kyori.adventure.text.Component.text("SlimeTECH Guide")
                .color(net.kyori.adventure.text.format.TextColor.color(0x55AAFF))
                .decorate(net.kyori.adventure.text.format.TextDecoration.BOLD));
            
            // Set lore using Adventure API
            java.util.List<net.kyori.adventure.text.Component> lore = Arrays.asList(
                net.kyori.adventure.text.Component.text("The complete guide to SlimeTECH")
                    .color(net.kyori.adventure.text.format.TextColor.color(0xAAAAAA)),
                net.kyori.adventure.text.Component.text("Learn about items, crafting, and machines")
                    .color(net.kyori.adventure.text.format.TextColor.color(0xAAAAAA)),
                net.kyori.adventure.text.Component.text(""),
                net.kyori.adventure.text.Component.text("Right-click to open")
                    .color(net.kyori.adventure.text.format.TextColor.color(0x555555)),
                net.kyori.adventure.text.Component.text("or use /st open-guide")
                    .color(net.kyori.adventure.text.format.TextColor.color(0x555555))
            );
            meta.lore(lore);
            
            // Add enchantment glow (visual only)
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            // Mark as guide book with persistent data
            org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(
                new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "guide_book"),
                org.bukkit.persistence.PersistentDataType.STRING,
                "true"
            );
            
            book.setItemMeta(meta);
        }
        
        return book;
    }
    
    /**
     * Checks if an item is a SlimeTECH Guide Book
     */
    public boolean isGuideBook(ItemStack item) {
        if (item == null || item.getType() != Material.ENCHANTED_BOOK || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        org.bukkit.persistence.PersistentDataContainer pdc = meta.getPersistentDataContainer();
        
        return pdc.has(
            new org.bukkit.NamespacedKey(com.slimetech.SlimeTECH.getInstance(), "guide_book"),
            org.bukkit.persistence.PersistentDataType.STRING
        );
    }
    
    /**
     * Opens the guide book GUI for a player
     */
    public void openGuideBook(Player player) {
        Inventory guide = guiManager.createProtectedGUI("&9&lSlimeTECH Guide", player);
        
        // Setup guide pages
        setupMainPage(guide);
        
        // Open for player
        guiManager.openGUI(player, guide);
    }
    
    /**
     * Sets up the main guide page
     */
    private void setupMainPage(Inventory guide) {
        // Title item at top center
        guiManager.setItem(guide, 4, createTitleItem());
        
        // Category buttons
        guiManager.addButton(guide, 19, "guide_getting_started",
            Material.GRASS_BLOCK, "&aGetting Started",
            "&7Learn the basics of SlimeTECH",
            "&7First steps and essential items");
        
        guiManager.addButton(guide, 21, "guide_copper_system",
            Material.COPPER_INGOT, "&6Copper System",
            "&7Version-adaptive materials",
            "&7How copper transforms between versions",
            "&7Usage in recipes");
        
        guiManager.addButton(guide, 23, "guide_crafting",
            Material.CRAFTING_TABLE, "&eCrafting System",
            "&7How to craft SlimeTECH items",
            "&7Crafting stations and recipes");
        
        guiManager.addButton(guide, 25, "guide_energy",
            Material.REDSTONE, "&cEnergy System",
            "&7Power generation and transfer",
            "&7Machines and automation");
        
        guiManager.addButton(guide, 29, "guide_magic",
            Material.ENCHANTING_TABLE, "&5Magic System",
            "&7Magical items and spells",
            "&7Altars and enchantments");
        
        guiManager.addButton(guide, 31, "guide_tools",
            Material.DIAMOND_PICKAXE, "&bTools & Gadgets",
            "&7Upgraded tools and gadgets",
            "&7Special abilities and uses");
        
        guiManager.addButton(guide, 33, "guide_faq",
            Material.BOOK, "&fFAQ & Troubleshooting",
            "&7Common questions and solutions",
            "&7Known issues and workarounds");
        
        // Navigation buttons
        guiManager.addButton(guide, 45, "guide_back",
            Material.ARROW, "&fBack", "&7Return to previous page");
        
        guiManager.addButton(guide, 49, "guide_close",
            Material.BARRIER, "&cClose Guide", "&7Close this guide");
        
        guiManager.addButton(guide, 53, "guide_next",
            Material.ARROW, "&fNext Page", "&7Go to next page");
        
        // Register button handlers
        guiManager.registerButton("guide_getting_started", (player, slot) -> {
            openGettingStartedPage(player);
        });
        
        guiManager.registerButton("guide_copper_system", (player, slot) -> {
            openCopperSystemPage(player);
        });
        
        guiManager.registerButton("guide_close", (player, slot) -> {
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Guide closed!");
        });
    }
    
    private ItemStack createTitleItem() {
        return guiManager.createGuiItem(
            Material.ENCHANTED_BOOK,
            "&9&lSlimeTECH Guide",
            "&7Version: &f1.0.1-DEV",
            "&7Your guide to all things SlimeTECH",
            "",
            "&eClick categories to learn more"
        );
    }
    
    private void openGettingStartedPage(Player player) {
        Inventory page = guiManager.createProtectedGUI("&9&lGetting Started", player);
        
        // Content
        guiManager.setItem(page, 11, guiManager.createGuiItem(
            Material.COMPASS, "&bFirst Steps",
            "&71. Get the SlimeTECH Guide Book",
            "&72. Start with Copper Ingots",
            "&73. Learn the crafting system",
            "&74. Build your first machine"
        ));
        
        guiManager.setItem(page, 13, guiManager.createGuiItem(
            Material.CHEST, "&bEssential Items",
            "&7• Copper Ingots (version-adaptive)",
            "&7• Basic Circuits",
            "&7• Energy Cells",
            "&7• Crafting Components"
        ));
        
        guiManager.setItem(page, 15, guiManager.createGuiItem(
            Material.CLOCK, "&bQuick Tips",
            "&7• Use &e/st gui &7for crafting",
            "&7• Copper auto-transforms between versions",
            "&7• Check &e/st version &7for info",
            "&7• Machines need power to work"
        ));
        
        // Back button
        guiManager.addButton(page, 45, "guide_back_main",
            Material.ARROW, "&fBack to Main", "&7Return to main guide");
        
        guiManager.openGUI(player, page);
    }
    
    private void openCopperSystemPage(Player player) {
        Inventory page = guiManager.createProtectedGUI("&9&lCopper System", player);
        
        // Content about copper transformation
        guiManager.setItem(page, 11, guiManager.createGuiItem(
            Material.COPPER_INGOT, "&6Copper Ingot",
            "&7A version-adaptive material",
            "&7In 1.17+: Shows as Copper Ingot",
            "&7In 1.16.5: Shows as Brick",
            "&7Always functions the same!",
            "",
            "&eGet with: &f/st copper"
        ));
        
        guiManager.setItem(page, 13, guiManager.createGuiItem(
            Material.BRICK, "&6Visual Adaptation",
            "&7Items auto-transform when:",
            "&7• Server version changes",
            "&7• Player joins/reloads",
            "&7• Using &e/st transform",
            "",
            "&7Functionality never changes!"
        ));
        
        guiManager.setItem(page, 15, guiManager.createGuiItem(
            Material.CRAFTING_TABLE, "&6Usage in Recipes",
            "&7Use in all SlimeTECH recipes",
            "&7Works the same in all versions",
            "&7No recipe changes needed",
            "",
            "&7One JAR for 1.16.5 → 1.21+"
        ));
        
        // Back button
        guiManager.addButton(page, 45, "guide_back_main",
            Material.ARROW, "&fBack to Main", "&7Return to main guide");
        
        guiManager.openGUI(player, page);
    }
}