package dev.wwst.admintools3.util;

import com.google.common.collect.Lists;
import dev.wwst.admintools3.AdminTools3;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDataStorage {

    private  File file;
    private  FileConfiguration customFile;

    private  AdminTools3 plugin;

    /*
     ** Finds or generates the custom config file
     */
    public PlayerDataStorage(String path){
        plugin = (AdminTools3) Bukkit.getServer().getPluginManager().getPlugin(AdminTools3.PLUGIN_NAME);
        plugin.getLogger().log(Level.INFO, "Loading Storage: "+path);

        File storageFolder = new File(plugin.getDataFolder()+"/storage");
        if(!storageFolder.exists()) storageFolder.mkdirs();

        file = new File(plugin.getDataFolder()+"/storage", path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        plugin.addDataStorage(this);
    }

    public FileConfiguration getConfig(){
        return customFile;
    }

    public boolean getPlayerData(Player p) {
        return customFile.getBoolean(p.getUniqueId().toString());
    }

    public List<UUID> getAllData() {
        List<UUID> toReturn = Lists.newArrayList();
        for(String key : customFile.getKeys(false)) {
            if(customFile.getBoolean(key)) toReturn.add(UUID.fromString(key));
        }
        return toReturn;
    }
    /*
     ** Saves the current FileConfiguration to the file on the disk
     */
    public void save() {
        Configuration.get().options().copyDefaults(true);
        try {
            customFile.save(file);
            AdminTools3.getInstance().getLogger().log(Level.INFO, "Storage file "+file.getName()+" saved.");
        } catch (IOException e) { e.printStackTrace();}
    }

    public void write(String path, Object object) {
        if(customFile.contains(path)) {
            customFile.set(path, object);
        } else {
            customFile.addDefault(path, object);
        }
        save();
    }

    public void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
