package com.slimetech.listeners;

import com.slimetech.gui.GuideBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    private final GuideBook guideBook = new GuideBook();
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Give guide book to new players
        if (!event.getPlayer().hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(com.slimetech.SlimeTECH.getInstance(), () -> {
                event.getPlayer().getInventory().addItem(guideBook.createGuideBook());
                event.getPlayer().sendMessage(ChatColor.GREEN + "Welcome to SlimeTECH!");
                event.getPlayer().sendMessage(ChatColor.GRAY + "Check your inventory for the guide book.");
                event.getPlayer().sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/st help" + 
                    ChatColor.GRAY + " for commands.");
            }, 40L); // 2 second delay
        }
    }
}