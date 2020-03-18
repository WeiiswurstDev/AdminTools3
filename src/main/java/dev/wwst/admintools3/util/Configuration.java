package dev.wwst.admintools3.util;

import dev.wwst.admintools3.AdminTools3;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration {

    private static File file;
    private static FileConfiguration customFile;

    private static AdminTools3 plugin;

    /*
     ** Finds or generates the custom config file
     */
    public static void setup(){
        plugin = (AdminTools3) Bukkit.getServer().getPluginManager().getPlugin(AdminTools3.PLUGIN_NAME);
        plugin.getLogger().log(Level.INFO, "Loading Configuration");


        file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveResource("config.yml",false);
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        plugin.getLogger().log(Level.INFO, "Done");

    }

    public static FileConfiguration get(){
        return customFile;
    }

    /*
     ** Saves the current FileConfiguration to the file on the disk
     */
    public static void save() {
        Configuration.get().options().copyDefaults(true);
        try {
            customFile.save(file);
        } catch (IOException e) { e.printStackTrace();}
    }

    public static void write(String path, Object object) {
        if(get().contains(path)) {
            get().set(path, object);
        } else {
            get().addDefault(path, object);
        }
        save();
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}