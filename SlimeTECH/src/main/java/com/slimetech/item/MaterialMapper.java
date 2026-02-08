package com.slimetech.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MaterialMapper {
    private static MaterialMapper instance;
    private boolean isModernVersion;
    
    private MaterialMapper() {
        String version = Bukkit.getBukkitVersion();
        String[] parts = version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        
        // 1.17+ has copper ingots
        isModernVersion = (major > 1) || (major == 1 && minor >= 17);
    }
    
    public static MaterialMapper getInstance() {
        if (instance == null) {
            instance = new MaterialMapper();
        }
        return instance;
    }
    
    public Material getCopperIngotMaterial() {
        if (isModernVersion) {
            try {
                return Material.COPPER_INGOT; // 1.17+
            } catch (Exception e) {
                return Material.IRON_INGOT; // Fallback
            }
        } else {
            return Material.BRICK; // 1.16 and below
        }
    }
    
    public Material getCopperBlockMaterial() {
        if (isModernVersion) {
            try {
                return Material.COPPER_BLOCK; // 1.17+
            } catch (Exception e) {
                return Material.IRON_BLOCK; // Fallback
            }
        } else {
            return Material.BRICKS; // 1.16 and below
        }
    }
    
    public boolean isModernVersion() {
        return isModernVersion;
    }
    
    public String getVersionInfo() {
        return isModernVersion ? "1.17+ (Copper shows as Copper Ingot)" : "1.16- (Copper shows as Brick)";
    }
}