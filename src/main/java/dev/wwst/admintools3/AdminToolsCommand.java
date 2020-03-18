package dev.wwst.admintools3;

import dev.wwst.admintools3.gui.GUIManager;
import dev.wwst.admintools3.modules.Module;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminToolsCommand implements CommandExecutor {

    private final MessageTranslator msg;

    public AdminToolsCommand() {
        msg = MessageTranslator.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(msg.getMessage("chatmessages.playerOnlyCommand",true));
            return true;
        }
        if(!sender.hasPermission("admintools3.use")) {
            sender.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,"admintools3.use"));
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            GUIManager.getInstance().openSession(p);
        } else if(args.length <= 3) {
            for(Module m : ModuleLoader.getInstance().getModuleList()) {
                if(m.getName().equalsIgnoreCase(args[0])) {
                    Player other = p;
                    if(args.length >= 2) {
                        for(Player x : Bukkit.getOnlinePlayers()) {
                            if(x.getName().equalsIgnoreCase(args[1])) {other = x; break;}
                        }
                    }
                    World w = null;
                    if(args.length >= 3) {
                        w = Bukkit.getWorld(args[2]);
                    }
                    m.execute(p,other,w);
                    return true;
                }
            }
            p.sendMessage(msg.getMessageAndReplace("chatmessages.moduleNotFound",true,args[0]));
        } else {
            p.sendMessage(msg.getMessageAndReplace("chatmessages.syntax",true,"/a [<module> [otherPlayer] [world]]"));
        }

        return true;
    }
}
