package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.util.Configuration;
import dev.wwst.admintools3.util.ItemBuilder;
import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Collection;

public class ChatClearModule extends Module implements Listener {

    private final String invName;
    private final Inventory gamemodeSelector;

    private final int messageAmount;


    public ChatClearModule() {
        super(false, false, "chatclear", XMaterial.STRUCTURE_VOID);
        invName = msg.getMessage("gui.chatclear.invName");
        gamemodeSelector = Bukkit.createInventory(null, InventoryType.BREWING, invName);
        gamemodeSelector.setItem(0,new ItemBuilder(XMaterial.IRON_BLOCK, msg.getMessage("gui.chatclear.forYou")).build());
        gamemodeSelector.setItem(2,new ItemBuilder(XMaterial.GOLD_BLOCK, msg.getMessage("gui.chatclear.forAll")).build());
        gamemodeSelector.setItem(1,ItemBuilder.WHITEPANE);
        gamemodeSelector.setItem(3,ItemBuilder.WHITEPANE);
        Bukkit.getPluginManager().registerEvents(this, AdminTools3.getInstance());
        int messageAmount = Configuration.get().getInt("module.chatclear.messageAmount",100);
        if(messageAmount > 256) messageAmount = 256;
        this.messageAmount = messageAmount;
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        player.openInventory(gamemodeSelector);
        return true;
    }


    @EventHandler
    public void click(InventoryClickEvent event) {
        if(!invName.equals(event.getView().getTitle())) return;
        event.setCancelled(true);
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

        switch(event.getCurrentItem().getType()) {
            case IRON_BLOCK:
                ccPlayer(player);
                player.closeInventory();
                break;
            case GOLD_BLOCK:
                player.closeInventory();
                if(player.hasPermission("admintools3.module.chatclear"))
                    ccAll(player);
                else
                    player.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,player,"admintools3.module.chatclear"));
                break;
            default:
                break;
        }

    }

    private void ccPlayer(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminTools3.getInstance(), () -> {
            for(int i = 0; i < messageAmount; i++) {
                p.sendMessage(" ");
            }
            p.sendMessage(msg.getMessage("module.chatclear.message.clearedForYou",true,p));
        });
    }

    private void ccAll(Player player) {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        final Server server = Bukkit.getServer();
        Bukkit.getScheduler().runTaskAsynchronously(AdminTools3.getInstance(), () -> {
            for(Player p : players){
                for(int i = 0; i < messageAmount; i++) {
                    p.sendMessage(" ");
                }
            }
            player.sendMessage(msg.getMessage("module.chatclear.message.clearedForAll",true,player));
            server.broadcastMessage(msg.getMessageAndReplace("module.chatclear.message.clearedByPlayer",true,player,player.getName()));
        });
    }
}
