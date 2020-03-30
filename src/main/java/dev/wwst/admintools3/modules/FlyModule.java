package dev.wwst.admintools3.modules;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class FlyModule extends Module {

    public FlyModule() {
        super(false, true, "fly", Material.FEATHER);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        if(other.getAllowFlight()) {
            other.setAllowFlight(false);
            other.sendMessage(msg.getMessage("module.fly.toggleOff",true));
        } else {
            other.setAllowFlight(true);
            other.sendMessage(msg.getMessage("module.fly.toggleOn",true));
        }
        return true;
    }

}
