package dev.wwst.admintools3.modules;

import dev.wwst.admintools3.util.XMaterial;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PvpChangeModule extends Module {

    public PvpChangeModule() {
        super(true,false,"pvp", XMaterial.IRON_SWORD);
        useDefaultMessageKeyFormat = false;
    }

    @Override
    public boolean execute(Player player, Player other, World world) {
        if(!super.execute(player,other,world)) {
            return false;
        }
        if(world.getPVP()) {
            world.setPVP(false);
            player.sendMessage(msg.getMessageAndReplace("module.pvp.message.toggleOff",true,player,world.getName()));
        } else {
            world.setPVP(true);
            player.sendMessage(msg.getMessageAndReplace("module.pvp.message.toggleOn",true,player,world.getName()));
        }
        return false;
    }
}
