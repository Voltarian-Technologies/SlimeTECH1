package com.slimetech;

import com.slimetech.gui.CraftingStationGUI;
import com.slimetech.gui.GuideBook;
import com.slimetech.item.ItemManager;
import com.slimetech.item.MaterialMapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlimeTECHCommand implements CommandExecutor, TabCompleter {
    
    private final ItemManager itemManager = ItemManager.getInstance();
    private final MaterialMapper materialMapper = MaterialMapper.getInstance();
    private final GuideBook guideBook = new GuideBook();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "copper":
                return handleCopper(sender, args);
            case "version":
                return handleVersion(sender);
            case "transform":
                return handleTransform(sender);
            case "guide":
                return handleGuide(sender, args);
            case "open-guide":
                return handleOpenGuide(sender);
            case "gui":
                return handleGUI(sender, args);
            case "help":
                sendHelp(sender);
                return true;
            default:
                sender.sendMessage("§cUnknown command! Use §e/st help §cfor help.");
                return true;
        }
    }
    
    private boolean handleCopper(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this!");
            return true;
        }
        
        Player player = (Player) sender;
        int amount = args.length > 1 ? tryParseInt(args[1], 1) : 1;
        
        org.bukkit.inventory.ItemStack copper = itemManager.createCopperIngot(amount);
        player.getInventory().addItem(copper);
        
        String materialName = copper.getType().toString().toLowerCase().replace("_", " ");
        player.sendMessage("§aGiven §7" + amount + "x Copper Ingot");
        player.sendMessage("§7Shows as: §f" + materialName + " §7in this version");
        player.sendMessage("§7Will auto-transform if server version changes");
        
        return true;
    }
    
    private boolean handleVersion(CommandSender sender) {
        sender.sendMessage("§6§lSlimeTECH Version Info:");
        sender.sendMessage("§7Server: §f" + materialMapper.getVersionInfo());
        sender.sendMessage("§7Copper Ingot → Shows as: §f" + 
            materialMapper.getCopperIngotMaterial().toString().toLowerCase().replace("_", " "));
        sender.sendMessage("§7Auto-transformation: §a✓ Enabled");
        sender.sendMessage("§7Guide Book: §a✓ Available");
        sender.sendMessage("§7Crafting GUI: §a✓ Available");
        return true;
    }
    
    private boolean handleTransform(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this!");
            return true;
        }
        
        Player player = (Player) sender;
        player.sendMessage("§7Transforming your copper ingots...");
        
        // Manually trigger transformation
        com.slimetech.listeners.ItemTransformListener transformer = 
            new com.slimetech.listeners.ItemTransformListener();
        transformer.transformCopperIngots(player);
        
        player.sendMessage("§aCopper ingots updated to current version!");
        return true;
    }
    
    private boolean handleGuide(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 1 && args[1].equalsIgnoreCase("open")) {
            // Open guide GUI directly
            guideBook.openGuideBook(player);
            player.sendMessage("§aOpened SlimeTECH Guide!");
        } else {
            // Give physical guide book
            org.bukkit.inventory.ItemStack guide = guideBook.createGuideBook();
            player.getInventory().addItem(guide);
            player.sendMessage("§aGiven SlimeTECH Guide Book!");
            player.sendMessage("§7Right-click to open, or use §e/st open-guide");
        }
        
        return true;
    }
    
    private boolean handleOpenGuide(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this!");
            return true;
        }
        
        Player player = (Player) sender;
        guideBook.openGuideBook(player);
        player.sendMessage("§aOpened SlimeTECH Guide!");
        return true;
    }
    
    private boolean handleGUI(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 1) {
            // Open specific category
            String category = args[1];
            new CraftingStationGUI().showCategory(player, category);
            player.sendMessage("§aOpening §7" + category + " §acategory...");
        } else {
            // Open main crafting station
            new CraftingStationGUI().openCraftingStation(player);
            player.sendMessage("§aOpening SlimeTECH Crafting Station...");
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§lSlimeTECH Commands:");
        sender.sendMessage("§7/st copper [amount] §8- Get version-adaptive copper ingot");
        sender.sendMessage("§7/st version §8- Check version adaptation info");
        sender.sendMessage("§7/st transform §8- Update items to current version");
        sender.sendMessage("§7/st guide §8- Get SlimeTECH Guide Book");
        sender.sendMessage("§7/st guide open §8- Open guide without book");
        sender.sendMessage("§7/st open-guide §8- Open guide GUI directly");
        sender.sendMessage("§7/st gui [category] §8- Open crafting station GUI");
        sender.sendMessage("§7/st help §8- Show this help");
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPlayedBefore()) {
                sender.sendMessage("");
                sender.sendMessage("§aWelcome to SlimeTECH! Check your inventory for the guide book!");
            }
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> commands = Arrays.asList(
                "copper", "version", "transform", "guide", 
                "open-guide", "gui", "help"
            );
            
            for (String command : commands) {
                if (command.startsWith(args[0].toLowerCase())) {
                    completions.add(command);
                }
            }
        } 
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("guide")) {
                if ("open".startsWith(args[1].toLowerCase())) {
                    completions.add("open");
                }
            }
            else if (args[0].equalsIgnoreCase("gui")) {
                List<String> categories = Arrays.asList(
                    "basic", "energy", "storage", "magic", "tools"
                );
                
                for (String category : categories) {
                    if (category.startsWith(args[1].toLowerCase())) {
                        completions.add(category);
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("copper")) {
                if ("1".startsWith(args[1])) completions.add("1");
                if ("16".startsWith(args[1])) completions.add("16");
                if ("32".startsWith(args[1])) completions.add("32");
                if ("64".startsWith(args[1])) completions.add("64");
            }
        }
        
        return completions;
    }
    
    private int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}