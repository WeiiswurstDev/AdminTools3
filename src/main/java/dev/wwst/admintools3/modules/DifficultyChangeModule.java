package dev.wwst.admintools3.modules;

import com.google.common.collect.Maps;
import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.util.ItemBuilder;
import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class DifficultyChangeModule extends Module implements Listener {

    private final String invName;
    private final Inventory gamemodeSelector;

    private final Map<Player, World> selected;

    public DifficultyChangeModule() {
        super(true, false, "difficulty", XMaterial.WOODEN_SWORD);
        selected = Maps.newHashMap();
        invName = msg.getMessage("gui.difficulty.invName");

        gamemodeSelector = Bukkit.createInventory(null, InventoryType.HOPPER, invName);
        gamemodeSelector.setItem(0,new ItemBuilder(XMaterial.WOODEN_HOE, msg.getMessage("gui.difficulty.peaceful")).build());
        gamemodeSelector.setItem(1,new ItemBuilder(XMaterial.WOODEN_SWORD, msg.getMessage("gui.difficulty.easy")).build());
        gamemodeSelector.setItem(2,ItemBuilder.WHITEPANE);
        gamemodeSelector.setItem(3,new ItemBuilder(XMaterial.IRON_SWORD, msg.getMessage("gui.difficulty.normal")).build());
        gamemodeSelector.setItem(4,new ItemBuilder(XMaterial.DIAMOND_SWORD, msg.getMessage("gui.difficulty.hard")).build());
        Bukkit.getPluginManager().registerEvents(this, AdminTools3.getInstance());
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        player.openInventory(gamemodeSelector);
        selected.put(player,world);
        return true;
    }


    @EventHandler
    public void click(InventoryClickEvent event) {
        if(!invName.equals(event.getView().getTitle())) return;
        event.setCancelled(true);
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

        World world = selected.get(player);
        player.closeInventory();
        Difficulty difficulty = null;
        switch(event.getCurrentItem().getType()) {
            case WOODEN_HOE:
                difficulty = Difficulty.PEACEFUL;
                break;
            case WOODEN_SWORD:
                difficulty = Difficulty.EASY;
                break;
            case IRON_SWORD:
                difficulty = Difficulty.NORMAL;
                break;
            case DIAMOND_SWORD:
                difficulty = Difficulty.HARD;
                break;
            default:
                break;
        }
        if(difficulty == null) {
            player.sendMessage(msg.getMessage("module.difficulty.message.noDifficultySelected",true,player));
        } else {
            world.setDifficulty(difficulty);
            player.sendMessage(msg.getMessageAndReplace("module.difficulty.message.difficultySet",true,player,msg.getMessage("gui.difficulty."+difficulty.name().toLowerCase())));
        }
    }
}
