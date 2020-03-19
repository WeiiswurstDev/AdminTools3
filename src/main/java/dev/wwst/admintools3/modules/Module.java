package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Module {

    protected boolean needsWorld, needsPlayer;
    protected String name;
    protected Material material;

    protected String itemname, itemlore;

    protected Module(boolean needsWorld, boolean needsPlayer, String name, Material material) {
        this.needsWorld = needsWorld;
        this.needsPlayer = needsPlayer;
        this.name = name;
        this.material = material;

        this.itemname = MessageTranslator.getInstance().getMessage("module."+name+".item.name");
        this.itemlore = MessageTranslator.getInstance().getMessage("module."+name+".item.lore");
    }

    public boolean execute(Player player, Player other, World world) {
        if(!player.hasPermission("admintools3.module."+name)) { // wenn er sie nicht auf andere oder sich selbst anwenden darf
            // wird noch überprüft ob er sie auf sich selbst anwenden darf und er sie auf sich selbst anwendet
            if(player.hasPermission("admintools3.module."+name+".self")) {
                if(player.getUniqueId() != other.getUniqueId()) {
                    player.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name));
                    return false;
                }
            } else {
                if(player.getUniqueId() != other.getUniqueId()) {
                    player.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name));
                } else {
                    player.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("chatmessages.noperm",true,"admintools3.module."+name+".self"));
                }
                return false;
            }
        }
        if(needsWorld && world == null) {
            player.sendMessage(MessageTranslator.getInstance().getMessage("chatmessage.missingWorld", true));
            return false;
        }
        if(needsPlayer && (other == null || !other.isOnline())) {
            player.sendMessage(MessageTranslator.getInstance().getMessage("chatmessage.missingPlayer", true));
            return false;
        }

        if(needsPlayer && player.getUniqueId() != other.getUniqueId()) {
            other.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("module." + name + ".message.appliedFromOther", true, player.getName()));
            player.sendMessage(MessageTranslator.getInstance().getMessageAndReplace("module."+name+".message.applyToOther", true, other.getName()));
        } else
            player.sendMessage(MessageTranslator.getInstance().getMessage("module."+name+".message.applyToSelf", true));
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
