package dev.wwst.admintools3;

import dev.wwst.admintools3.gui.GUIManager;
import dev.wwst.admintools3.modules.ModuleLoader;
import dev.wwst.admintools3.util.Configuration;
import dev.wwst.admintools3.util.MessageTranslator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class AdminTools3 extends JavaPlugin {

    private static AdminTools3 INSTANCE;
    private final String configurationFolderPath = "plugins//AdminTools3";

    public static final String PLUGIN_NAME = "Admintools3";

    @Override
    public void onEnable() {
        AdminTools3.INSTANCE = this;
        getDataFolder().mkdirs();
        Configuration.setup();
        new MessageTranslator(Configuration.get().getString("language"));
        new ModuleLoader();
        new GUIManager();

        getCommand("admingui").setExecutor(new AdminToolsCommand());

        if(!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().log(Level.WARNING, "!! ProtocolLib is not installed.");
            getLogger().log(Level.WARNING, "!! AdminTools will run just fine, however, you lose support for ActionBars.");
            getLogger().log(Level.WARNING, "!! Download ProtocolLib here: https://www.spigotmc.org/resources/protocollib.1997/");
        }
    }

    @Override
    public void onDisable() {

    }

    public static AdminTools3 getInstance() {
        return INSTANCE;
    }

    public String getConfigFolderPath() {
        return configurationFolderPath;
    }
}