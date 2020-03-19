package dev.wwst.admintools3.gui;

import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.modules.Module;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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

        if(MessageTranslator.getInstance().getMessage("gui.moduleSelector.invName").equals(e.getView().getTitle())) {
            for(Module m : ModuleLoader.getInstance().getModuleList()) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(m.getItemname())) {
                    selected = m;
                    if(!player.hasPermission("admintools3.module."+m.getName()) && !player.hasPermission("admintools3.module."+m.getName()+".self")) {
                        player.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+m.getName()));
                        player.closeInventory();
                        return;
                    }

                    if(selected.needsPlayer()) {

                        if(!player.hasPermission("admintools3.module."+m.getName())) {
                            selectedPlayer = player;
                            if(selected.needsWorld()) {
                                closed = true;
                                player.openInventory(GUIManager.getInstance().generateWorldSelector(player));
                                closed = false;
                            } else {
                                player.closeInventory();
                                selected.execute(player,selectedPlayer,null);
                            }
                            return;
                        }

                        closed = true;
                        player.openInventory(GUIManager.getInstance().generatePlayerSelector(player));
                        closed = false;
                    } else if(selected.needsWorld()) {
                        closed = true;
                        player.openInventory(GUIManager.getInstance().generateWorldSelector(player));
                        closed = false;
                    } else {
                        player.closeInventory();
                        selected.execute(player,null,null);
                    }
                }
            }
        } else if(MessageTranslator.getInstance().getMessage("gui.playerSelector.invName").equals(e.getView().getTitle())) {
            if(e.getCurrentItem().getType() != Material.PLAYER_HEAD) return;
            SkullMeta clickedMeta = (SkullMeta) e.getCurrentItem().getItemMeta();
            selectedPlayer = Bukkit.getPlayer(clickedMeta.getOwningPlayer().getUniqueId());
            if(selected.needsWorld()) {
                closed = true;
                player.openInventory(GUIManager.getInstance().generateWorldSelector(player));
                closed = false;
            } else {
                player.closeInventory();
                selected.execute(player,selectedPlayer,null);
            }
        } else if(MessageTranslator.getInstance().getMessage("gui.worldSelector.invName").equals(e.getView().getTitle())) {
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            for(World w : Bukkit.getWorlds()) {
                if(w.getName().equals(name.substring(2))) {
                    selectedWorld = w;
                    selected.execute(player,selectedPlayer,selectedWorld);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(closed) return;
        Bukkit.getConsoleSender().sendMessage("5");
        if(e.getPlayer().getUniqueId() == player.getUniqueId()) {
            closed = true;
            Bukkit.getConsoleSender().sendMessage("6");
            GUIManager.getInstance().closeSession(player);
        }
    }
}
