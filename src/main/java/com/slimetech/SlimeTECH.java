package com.slimetech;

import com.slimetech.gui.GUIManager;
import com.slimetech.item.ItemManager;
import com.slimetech.item.MaterialMapper;
import com.slimetech.listeners.ItemTransformListener;
import com.slimetech.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SlimeTECH extends JavaPlugin {
    
    private static SlimeTECH instance;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Create plugin folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize systems in correct order
        MaterialMapper.getInstance(); // First - detects version
        ItemManager.getInstance();    // Second - registers items
        
        // Register events
        Bukkit.getPluginManager().registerEvents(new ItemTransformListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(GUIManager.getInstance(), this);
        
        // Register commands
        getCommand("slimetch").setExecutor(new SlimeTECHCommand());
        getCommand("slimetch").setTabCompleter(new SlimeTECHCommand());
        
        // Log startup
        getLogger().info("==========================================");
        getLogger().info("SlimeTECH v" + getDescription().getVersion() + " enabled!");
        getLogger().info("Version: " + MaterialMapper.getInstance().getVersionInfo());
        getLogger().info("Copper Ingot → " + 
            MaterialMapper.getInstance().getCopperIngotMaterial());
        getLogger().info("Guide system: ✓ Active");
        getLogger().info("GUI system: ✓ Active");
        getLogger().info("==========================================");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SlimeTECH disabled!");
        instance = null;
    }
    
    public static SlimeTECH getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SlimeTECH is not initialized!");
        }
        return instance;
    }
}