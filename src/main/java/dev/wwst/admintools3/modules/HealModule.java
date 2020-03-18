package dev.wwst.admintools3.modules;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class HealModule extends Module {



    public HealModule() {
        super(false, true, "heal", Material.GOLDEN_APPLE);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        other.setHealth(other.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        other.setFoodLevel(20);
        return true;
    }
}
