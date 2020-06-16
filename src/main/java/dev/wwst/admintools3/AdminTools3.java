package dev.wwst.admintools3;

import com.google.common.collect.Lists;
import dev.wwst.admintools3.events.AdminChatEvent;
import dev.wwst.admintools3.events.JoinQuitEvent;
import dev.wwst.admintools3.gui.GUIManager;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.*;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public final class AdminTools3 extends JavaPlugin {

    private static AdminTools3 INSTANCE;

    public static final String PLUGIN_NAME = "Admintools3";

    private List<PlayerDataStorage> toSave;

    @Override
    public void onEnable() {
        AdminTools3.INSTANCE = this;
        getDataFolder().mkdirs();
        Configuration.setup();
        toSave = Lists.newArrayList();
        new MessageTranslator(Configuration.get().getString("language"));
        new ModuleLoader();
        new GUIManager();

        getCommand("admingui").setExecutor(new AdminToolsCommand());
        getCommand("playerinfo").setExecutor(new PlayerInfoCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AdminChatEvent(), this);

        if(Configuration.get().getBoolean("join-leave-messages.replace-messages")) {
            pm.registerEvents(new JoinQuitEvent(), this);
        }

        if(!pm.isPluginEnabled("ProtocolLib")) {
            getLogger().log(Level.WARNING, MessageTranslator.getInstance().getMessage("chatmessages.protocolLibNotFound"));
        }
        if(!XMaterial.isNewVersion()) {
            getLogger().log(Level.INFO, MessageTranslator.getInstance().getMessage("chatmessages.legacyVersion"));
        }

        PaperLib.suggestPaper(this);

        Metrics metrics = new Metrics(this, 6930);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Configuration.get().getString("language", "en");
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("custom_join_and_leave_messages", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Configuration.get().getString("join-leave-messages.replace-messages", "false");
            }
        }));


        new UpdateChecker(this, 76747).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("You are up to date!");
            } else {
                getLogger().info("!!! There is a new update available! Download at https://www.spigotmc.org/resources/admintools.76747/ !!!");
            }
        });
    }

    @Override
    public void onDisable() {
        for(PlayerDataStorage pds : toSave) {
            pds.save();
        }
        getLogger().log(Level.INFO, "AdminTools3 was disabled.");
    }

    public static AdminTools3 getInstance() {
        return INSTANCE;
    }

    public String getConfigFolderPath() {
        return "plugins//AdminTools3";
    }

    public void addDataStorage(PlayerDataStorage pds) {
        toSave.add(pds);
    }
}