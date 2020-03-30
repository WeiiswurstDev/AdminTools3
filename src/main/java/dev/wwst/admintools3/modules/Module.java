package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.Configuration;
import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Module {

    protected final boolean needsWorld, needsPlayer;
    protected final String name;
    protected final Material material;

    protected final String itemname, itemlore;

    protected final int cooldown;
    protected final Map<UUID, Long> onCooldown;

    protected final MessageTranslator msg;

    protected boolean useDefaultMessageKeyFormat = true;

    protected Module(boolean needsWorld, boolean needsPlayer, String name, Material material) {
        this.needsWorld = needsWorld;
        this.needsPlayer = needsPlayer;
        this.name = name;
        this.material = material;

        this.msg = MessageTranslator.getInstance();

        this.itemname = msg.getMessage("module."+name+".item.name");
        this.itemlore = msg.getMessage("module."+name+".item.lore");

        this.cooldown = Configuration.get().getInt("modules."+name+".cooldown");
        this.onCooldown = new HashMap<>();
    }

    public boolean execute(Player player, Player other, World world) {
        if(!player.hasPermission("admintools3.module."+name)) { // wenn er sie nicht auf andere oder sich selbst anwenden darf
            // wird noch überprüft ob er sie auf sich selbst anwenden darf und er sie auf sich selbst anwendet
            if(player.hasPermission("admintools3.module."+name+".self")) {
                if(player.getUniqueId() != other.getUniqueId()) {
                    player.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name));
                    return false;
                }
            } else {
                if(player.getUniqueId() != other.getUniqueId()) {
                    player.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name));
                } else {
                    player.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name+".self"));
                }
                return false;
            }
        }
        if(needsWorld && world == null) {
            player.sendMessage(msg.getMessage("chatmessages.missingWorld", true));
            return false;
        }
        if(needsPlayer && (other == null || !other.isOnline())) {
            player.sendMessage(msg.getMessage("chatmessages.missingPlayer", true));
            return false;
        }

        if(cooldown > 0 && !player.hasPermission("admintools3.bypass.cooldown") && !player.hasPermission("admintools3.bypass.cooldown."+name)) {
            if(onCooldown.containsKey(player.getUniqueId())) {
                if(Instant.now().isBefore(Instant.ofEpochSecond(onCooldown.get(player.getUniqueId())))) {
                    player.sendMessage(msg.getMessage("chatmessages.onCooldown", true));
                    return false;
                }
            }
            onCooldown.put(player.getUniqueId(), Instant.now().getEpochSecond());
        }
        // Some modules, such as mute, have their own message key format.
        // Custom modules by third parties could use this as well to use their own message system, if they want to.
        if(useDefaultMessageKeyFormat) {
            if(needsPlayer && player.getUniqueId() != other.getUniqueId()) {
                other.sendMessage(msg.getMessageAndReplace("module."+name+".message.appliedByOther", true, player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module."+name+".message.applyToOther", true, other.getName()));
            } else
                player.sendMessage(msg.getMessage("module."+name+".message.applyToSelf", true));
        }

        if(Configuration.get().getBoolean("log-module-usage")) {
            String logmessage = "[LOG] Player "+player.getName()+" used the module "+name;
            if(other != null) logmessage += " on the player "+other.getName();
            if(world != null) logmessage += " in the world "+world.getName();
            Bukkit.getConsoleSender().sendMessage(logmessage);
        }
        return true;
    }

    public boolean needsWorld() {
        return needsWorld;
    }

    public boolean needsPlayer() {
        return needsPlayer;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemlore() {
        return itemlore;
    }
}
