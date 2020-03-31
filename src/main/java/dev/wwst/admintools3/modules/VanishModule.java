package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.AdminTools3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VanishModule extends Module implements Listener {

    private final List<UUID> vanishedPlayers;
    private final AdminTools3 plugin;

    public VanishModule() {
        super(false, true, "vanish", Material.POTION);
        useDefaultMessageKeyFormat = false;
        plugin = AdminTools3.getInstance();
        vanishedPlayers = new ArrayList<>();
        // #TODO Loading/saving of frozen players;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        if(vanishedPlayers.contains(other.getUniqueId())) {
            vanishedPlayers.remove(other.getUniqueId());
            other.sendMessage(msg.getMessage("module.vanish.message.toggleOff",true, player));

            for(Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin,other);
            }

        } else {
            vanishedPlayers.add(other.getUniqueId());
            other.sendMessage(msg.getMessage("module.vanish.message.toggleOn",true,player));

            for(Player p : Bukkit.getOnlinePlayers()) {
                if(!p.hasPermission("admintools3.vanish.bypass"))
                    p.hidePlayer(plugin,other);
            }
        }
        return true;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if(event.getPlayer().hasPermission("admintools3.bypass.vanish")) return;

        for(UUID vanished : vanishedPlayers) {
            Player vanishedPlayer = Bukkit.getPlayer(vanished);
            if(vanishedPlayer != null) event.getPlayer().hidePlayer(plugin, vanishedPlayer);
        }
    }

}
