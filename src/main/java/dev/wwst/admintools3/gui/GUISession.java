package dev.wwst.admintools3.gui;

import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.modules.Module;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import sun.plugin2.message.Message;

public class GUISession implements Listener {

    private Player player;

    private Module selected;

    private Player selectedPlayer;
    private World selectedWorld;

    private boolean closed = false;

    public GUISession(Player player) {
        this.player = player;
        Bukkit.getServer().getPluginManager().registerEvents(this, AdminTools3.getInstance());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(closed) return;
        if(e.getWhoClicked().getUniqueId() != player.getUniqueId()) return;
        e.setCancelled(true);

        if(e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;

        if(MessageTranslator.getInstance().getMessage("gui.moduleSelector").equals(e.getView().getTitle())) {
            for(Module m : ModuleLoader.getInstance().getModuleList()) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(m.getItemname())) {
                    selected = m;
                    if(selected.needsPlayer()) {
                        // #TODO
                    } else if(selected.needsWorld()) {
                        // #TODO
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(closed) return;
        if(e.getPlayer().getUniqueId() == player.getUniqueId()) {
            closed = true;
            GUIManager.getInstance().closeSession(player);
        }
    }
}
