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

import java.util.Arrays;

public class AdminToolsCommand implements CommandExecutor {

    private final MessageTranslator msg;

    public AdminToolsCommand() {
        msg = MessageTranslator.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(msg.getMessage("chatmessages.playerOnlyCommand",true, null));
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("admintools3.use")) {
            p.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,p,"admintools3.use"));
            return true;
        }

        label = label.toLowerCase();
        if(label.equals("admingui") || label.equals("a") || label.equals("admintools")) {
            if(args.length == 0) {
                GUIManager.getInstance().openSession(p);
            } else if(args.length <= 3) {
                executeModule(p,args[0], Arrays.copyOfRange(args,1,args.length));
            } else {
                p.sendMessage(msg.getMessageAndReplace("chatmessages.syntax",true,p,"/a [<module> [otherPlayer] [world]]"));
            }
        } else {
            executeModule(p,label,args);
        }

        return true;
    }

    private void executeModule(Player p, String moduleName, String[] args) {
        for(Module m : ModuleLoader.getInstance().getModuleList()) {
            if(m.getAliases().contains(moduleName)) {
                Player other = p;
                if(args.length >= 2) {
                    for(Player x : Bukkit.getOnlinePlayers()) {
                        if(x.getName().equalsIgnoreCase(args[0])) {other = x; break;}
                    }
                }
                World w = null;
                if(args.length >= 3) {
                    w = Bukkit.getWorld(args[1]);
                }
                m.execute(p,other,w);
                return;
            }
        }
        p.sendMessage(msg.getMessageAndReplace("chatmessages.moduleNotFound",true,p,moduleName));
    }
}
