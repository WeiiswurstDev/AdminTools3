package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.util.ItemBuilder;
import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GamemodeModule extends Module implements Listener {

    private final String invName;
    private final Inventory gamemodeSelector;

    public GamemodeModule() {
        super(false, true, "gm", XMaterial.DIAMOND_BLOCK);
        invName = msg.getMessage("gui.gamemodeSelector.invName");
        gamemodeSelector = Bukkit.createInventory(null, InventoryType.HOPPER, invName);
        gamemodeSelector.setItem(0,new ItemBuilder(XMaterial.IRON_PICKAXE, msg.getMessage("gui.gamemodeSelector.survival")).build());
        gamemodeSelector.setItem(1,new ItemBuilder(XMaterial.BEACON, msg.getMessage("gui.gamemodeSelector.creative")).build());
        gamemodeSelector.setItem(2,ItemBuilder.WHITEPANE);
        gamemodeSelector.setItem(3,new ItemBuilder(XMaterial.WOODEN_SWORD, msg.getMessage("gui.gamemodeSelector.adventure")).build());
        gamemodeSelector.setItem(4,new ItemBuilder(XMaterial.FEATHER, msg.getMessage("gui.gamemodeSelector.spectator")).build());
        Bukkit.getPluginManager().registerEvents(this, AdminTools3.getInstance());
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        other.openInventory(gamemodeSelector);
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
            case IRON_PICKAXE:
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(msg.getMessage("module.gm.message.survival",true,player));
                break;
            case BEACON:
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(msg.getMessage("module.gm.message.creative",true,player));
                break;
            case WOODEN_SWORD:
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(msg.getMessage("module.gm.message.adventure",true,player));
                break;
            case FEATHER:
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(msg.getMessage("module.gm.message.spectator",true,player));
                break;
            default:
                break;
        }
        player.closeInventory();
    }

}
