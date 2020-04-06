package dev.wwst.admintools3.events;

import dev.wwst.admintools3.util.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitEvent implements Listener {

    private final String joinMessage, leaveMessage;

    public JoinQuitEvent() {
        joinMessage = ChatColor.translateAlternateColorCodes('&', Configuration.get().getString("join-leave-messages.join"));
        leaveMessage = ChatColor.translateAlternateColorCodes('&',Configuration.get().getString("join-leave-messages.leave"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(joinMessage.replaceAll("%s",e.getPlayer().getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(leaveMessage.replaceAll("%s",e.getPlayer().getName()));
    }

}
