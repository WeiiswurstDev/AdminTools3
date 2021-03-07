package dev.wwst.admintools3.events;

import dev.wwst.admintools3.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        message = format.replaceAll("%message%",message).replaceAll("%player%",event.getPlayer().getName());
        for(Player p : Bukkit.getOnlinePlayers()) {
            // I made a dumb typo, so the 2nd perm check is just for the people that might have the old perm in their permissions
            // so the plugin doesnt break for them
            if(p.hasPermission("admintools3.chat.receive") || p.hasPermission("admintools3.chat.recieve"))
                p.sendMessage(message);
        }
        event.setCancelled(true);
    }

}
