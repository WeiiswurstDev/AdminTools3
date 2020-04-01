package dev.wwst.admintools3.modules;

import com.google.common.collect.Maps;
import dev.wwst.admintools3.AdminTools3;
import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

public class MaxHeartsModule extends Module implements Listener {

    private Map<Player, Player> hasToSelect;

    public MaxHeartsModule() {
        super(false, true, "maxhearts", XMaterial.ENCHANTED_GOLDEN_APPLE);
        useDefaultMessageKeyFormat = false; // because here, "execute" just allows another selection
        hasToSelect = Maps.newHashMap();
        Bukkit.getPluginManager().registerEvents(this, AdminTools3.getInstance());
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        player.sendMessage(msg.getMessage("module.maxhearts.message.sendAmountInChat",true,player));
        hasToSelect.put(player,other);
        return true;
    }

    @SuppressWarnings("deprecation") // for legacy versions
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(!hasToSelect.containsKey(player)) {
            return;
        }
        event.setCancelled(true);
        Player other = hasToSelect.get(player);
        hasToSelect.remove(player);
        if(!other.isOnline()) {
            player.sendMessage(msg.getMessage("chatmessages.needsPlayer",true,player));
            return;
        }
        try {
            double newAmount = Double.parseDouble(event.getMessage());
            if(newAmount < 0) {
                player.sendMessage(msg.getMessageAndReplace("chatmessages.smallerThanZero",true,player,event.getMessage()));
                return;
            }
            if(XMaterial.isNewVersion()) {
                other.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newAmount);
            } else {
                other.setMaxHealth(newAmount);
            }
            other.setHealth(newAmount);
            if(player.getUniqueId() == other.getUniqueId()) {
                player.sendMessage(msg.getMessageAndReplace("module.maxhearts.message.applyToSelf",true,player,newAmount+""));
            } else {
                player.sendMessage(msg.getMessageAndReplace("module.maxhearts.message.applyToOther",true,player,other.getName(), newAmount+""));
                other.sendMessage(msg.getMessageAndReplace("module.maxhearts.message.appliedByOther",true,player,player.getName(), newAmount+""));
            }
        } catch(NumberFormatException ex) {
            player.sendMessage(msg.getMessageAndReplace("chatmessages.numberFormat",true,player,event.getMessage()));
        }
    }
}
