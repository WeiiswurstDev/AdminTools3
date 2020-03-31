package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class KillModule extends Module{


    public KillModule() {
        super(false, true, "kill", XMaterial.DIAMOND_SWORD);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        other.setHealth(0);
        return true;
    }

}
