package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class InvClearModule extends Module {

    public InvClearModule() {
        super(false, true, "invclear", XMaterial.CHEST);
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player, other, world)) {
            return false;
        }
        other.getInventory().clear();
        return true;
    }
}
