package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.AdminTools3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FreezeModule extends Module implements Listener {

    private final List<UUID> frozenPlayers;

    public FreezeModule() {
        super(false, true, "freeze", Material.PACKED_ICE);
        useDefaultMessageKeyFormat = false;
        frozenPlayers = new ArrayList<>();
        // #TODO Loading/saving of frozen players;

        Bukkit.getPluginManager().registerEvents(this, AdminTools3.getInstance());
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(other.hasPermission("admintools3.bypass.freeze")) {
            player.sendMessage(msg.getMessageAndReplace("module.freeze.bypass",true, player,other.getName()));
            return false;
        }
        if(!super.execute(player, other, world)) {
            return false;
        }
        if(frozenPlayers.contains(other.getUniqueId())) {
            frozenPlayers.remove(other.getUniqueId());
            other.sendMessage(msg.getMessage("module.freeze.message.toggleOff",true,other));
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.freeze.message.toggledOffByOther",true, player, player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.freeze.message.toggledOffForOther",true, player, other.getName()));
            }
        } else {
            frozenPlayers.add(other.getUniqueId());
            other.sendMessage(msg.getMessage("module.freeze.message.toggleOn",true, other));
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.freeze.message.toggledOnByOther", true, player, player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.freeze.message.toggledOnForOther",true, player, other.getName()));
            }
        }
        return true;
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        if(frozenPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(msg.getMessage("module.freeze.message.stillFrozen",true,event.getPlayer()));
        }
    }

}
