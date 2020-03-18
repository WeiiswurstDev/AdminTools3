package dev.wwst.admintools3.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionBar {

    private final PacketContainer chat;
    private final ProtocolManager pm;
    private final String message;

    public ActionBar(String message) {
        this.message = message;
        if(Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            pm = ProtocolLibrary.getProtocolManager();
            chat = new PacketContainer(PacketType.Play.Server.CHAT);
            chat.getChatTypes().write(0, EnumWrappers.ChatType.GAME_INFO);
            chat.getChatComponents().write(0, WrappedChatComponent.fromJson(message));
        } else {
            pm = null;
            chat = null;
        }
    }

    public void send(Player player) {
        if(pm == null) {
            player.sendMessage("[ActionBar]: "+message);
            return;
        }
        try {
            pm.sendServerPacket(player, chat);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot send packet " + chat, e);
        }
    }

    public void sendToAll() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            send(p);
        }
    }

}
