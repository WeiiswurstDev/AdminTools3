package dev.wwst.admintools3;

import com.google.common.collect.Lists;
import dev.wwst.admintools3.gui.GUIManager;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class AdminTools3 extends JavaPlugin {

    private static AdminTools3 INSTANCE;
    private final String configurationFolderPath = "plugins//AdminTools3";

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

        if(!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().log(Level.WARNING, MessageTranslator.getInstance().getMessage("chatmessages.protocolLibNotFound"));
        }
        if(XMaterial.isNewVersion()) {
            getLogger().log(Level.INFO, MessageTranslator.getInstance().getMessage("chatmessages.legacyVersion"));
        }

        Metrics metrics = new Metrics(this, 6930);

        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> Configuration.get().getString("language","en")));
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
        return configurationFolderPath;
    }

    public void addDataStorage(PlayerDataStorage pds) {
        toSave.add(pds);
    }
}