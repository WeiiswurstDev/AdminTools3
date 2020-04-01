package dev.wwst.admintools3.events;

import dev.wwst.admintools3.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminChatEvent implements Listener {

    private final String format, prefix;

    public AdminChatEvent() {
        format = ChatColor.translateAlternateColorCodes('&',Configuration.get().getString("adminchat.format"));
        prefix = ChatColor.translateAlternateColorCodes('&',Configuration.get().getString("adminchat.prefix")).toLowerCase();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!event.getPlayer().hasPermission("admintools3.adminchat.send")) {
            return;
        }
        if(!event.getMessage().toLowerCase().startsWith(prefix)) {
            return;
        }
        String message = event.getMessage().replaceAll(prefix,"");
        Bukkit.broadcast(format.replaceAll("%message%",message).replaceAll("%player%",event.getPlayer().getName()), "admintools3.adminchat.recieve");
        event.setCancelled(true);
    }

}
