package dev.wwst.admintools3;

import com.google.common.collect.Lists;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.Arrays;
import java.util.List;

public class AdminToolsCommand implements CommandExecutor, Listener {

    private final MessageTranslator msg;
    private final List<String> aliases;
    private final List<String> aliasesWithSlash;

    public AdminToolsCommand() {
        msg = MessageTranslator.getInstance();
        aliases = ModuleLoader.getInstance().getAliases();
        aliasesWithSlash = Lists.newArrayListWithCapacity(aliases.size());
        for(String alias : aliases) {
            aliasesWithSlash.add("/"+alias);
            aliasesWithSlash.add("/admintools3:"+alias);
        }
        Bukkit.getPluginManager().registerEvents(this,AdminTools3.getInstance());
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

    @EventHandler
    public void aliasListener(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        String alias = args[0].substring(1); //cut away /
        if(!aliases.contains(alias)) {
            return;
        }
        event.setCancelled(true);
        Player p = event.getPlayer();
        if(!p.hasPermission("admintools3.use")) {
            p.sendMessage(msg.getMessageAndReplace("chatmessages.noperm",true,p,"admintools3.use"));
            return;
        }
        executeModule(p, alias, Arrays.copyOfRange(args, 1, args.length));
    }

    @EventHandler
    public void onSend(PlayerCommandSendEvent event) {
        System.out.println(event.getCommands().size());
        event.getCommands().addAll(aliasesWithSlash);
        System.out.println(event.getCommands().size());
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
