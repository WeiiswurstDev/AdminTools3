package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.Configuration;
import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class MaxHeartsModule extends Module{

    public MaxHeartsModule() {
        super(false, true, "heal", XMaterial.ENCHANTED_GOLDEN_APPLE);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        other.setHealth(other.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        return true;
    }
}
