package com.slimetech;

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
            default:
                sendHelp(sender);
                return true;
        }
    }
    
    private boolean handleCopper(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this!");
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
        return true;
    }
    
    private boolean handleTransform(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this!");
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
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§lSlimeTECH Commands:");
        sender.sendMessage("§7/st copper [amount] §8- Get version-adaptive copper ingot");
        sender.sendMessage("§7/st version §8- Check version adaptation");
        sender.sendMessage("§7/st transform §8- Update items to current version");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (String option : Arrays.asList("copper", "version", "transform")) {
                if (option.startsWith(args[0].toLowerCase())) {
                    completions.add(option);
                }
            }
            return completions;
        }
        return new ArrayList<>();
    }
    
    private int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}