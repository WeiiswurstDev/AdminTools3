package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class FlyModule extends Module {

    public FlyModule() {
        super(false, true, "fly", XMaterial.FEATHER);
        useDefaultMessageKeyFormat = false;
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        if(other.getAllowFlight()) {
            other.setAllowFlight(false);
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.fly.message.toggledOffByOther",true, player, player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.fly.message.toggledOffForOther",true, player, other.getName()));
            } else {
                other.sendMessage(msg.getMessage("module.fly.message.toggleOff",true,other));
            }
        } else {
            other.setAllowFlight(true);
            if(!other.getName().equals(player.getName())) {
                other.sendMessage(msg.getMessageAndReplace("module.fly.message.toggledOnByOther", true, player, player.getName()));
                player.sendMessage(msg.getMessageAndReplace("module.fly.message.toggledOnForOther",true, player, other.getName()));
            } else {
                other.sendMessage(msg.getMessage("module.fly.message.toggleOn",true, other));
            }
        }
        return true;
    }

}
