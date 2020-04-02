package dev.wwst.admintools3.util;

import dev.wwst.admintools3.AdminTools3;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MessageTranslator {

    private static String[] translations = new String[] {
            "de",
            "en"
    };

    private static MessageTranslator INSTANCE;

    private AdminTools3 plugin;
    private String language;
    private String prefix;
    private YamlConfiguration cfg;
    private boolean papiEnabled = false;

    private HashMap<String, String> messages;

    public MessageTranslator(String language) {
        if(INSTANCE != null) {
            return;
        }
        INSTANCE = this;
        plugin = AdminTools3.getInstance();
        prefix = Configuration.get().getString("prefix");
        messages = new HashMap<>();
        this.language = language;

        saveDefaults();

        System.out.println(plugin.getConfigFolderPath());

        File languageFile = new File("plugins/Admintools3/messages_"+language+".yml");
        if(!languageFile.exists()) {
            plugin.getLogger().log(Level.SEVERE, "!!! The language chosen by you, "+language+", cannot be resolved!");
            plugin.getLogger().log(Level.SEVERE, "!!! Create a file called messages_"+language+".yml in the AdminTools folder to start!");
            plugin.getLogger().log(Level.SEVERE, "!!! For now, the ENGLISH language file will be loaded!");
            languageFile =new File("plugins/Admintools3/messages_en.yml");
            System.out.println(languageFile.exists());
            System.out.println(languageFile.getAbsolutePath());
            System.out.println(languageFile.getName());
        }
        cfg = YamlConfiguration.loadConfiguration(languageFile);
        Map<String, Object> values = cfg.getValues(true);
        for(String key : values.keySet()) {
            messages.put(key, values.get(key).toString());
        }
        plugin.getLogger().log(Level.INFO, "Language loaded: messages_"+language+".yml");

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiEnabled = true;
        } else {
            plugin.getLogger().log(Level.WARNING, getMessage("chatmessages.papiNotFound"));
        }
    }

    // Loading custom language files for addons
    public void loadMessageFile(String path) {
        File languageFile = new File(path);
        if(!languageFile.exists()) {
            plugin.getLogger().log(Level.SEVERE, "Could not find a config file at "+path);
            return;
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(languageFile);
        Map<String, Object> values = cfg.getValues(true);
        for(String key : values.keySet()) {
            messages.put(key, values.get(key).toString());
        }
        plugin.getLogger().log(Level.INFO, "Custom language file loaded: "+path);
    }

    private void saveDefaults() {
        for(String translation : translations) {
            plugin.saveResource("messages_"+translation+".yml", true);
            plugin.getLogger().log(Level.INFO, "Default language exported: messages_"+translation+".yml");
        }
    }

    public String getMessageAndReplace(String key, boolean addPrefix, Player player, String... replacements) {
        if(!messages.containsKey(key)) {
            return ChatColor.YELLOW+key+ ChatColor.RED +" not found!";
        }
        String message = messages.get(key);
        if(papiEnabled) {
            message = PlaceholderAPI.setPlaceholders(player,message);
        }
        for(int i = 0; message.contains("%s") && replacements != null; i++) {
            if(replacements.length <= i) {
                message = message.replaceFirst("%s", "&cNO REPLACEMENT");
            } else {
                message = message.replaceFirst("%s", replacements[i]);
            }
        }
        if(addPrefix) {
            message = prefix + message;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage(String key, boolean addPrefix, Player player) {
        return getMessageAndReplace(key,addPrefix, player,"");
    }

    public String getMessage(String key, boolean addPrefix) {
        return getMessageAndReplace(key,addPrefix, null,"");
    }

    public String getMessage(String key) {
        return getMessageAndReplace(key,false,null,"");
    }

    public YamlConfiguration getConfiguration() {
        return cfg;
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }

    public String getLanguage() {
        return language;
    }

    public static MessageTranslator getInstance() {
        return INSTANCE;
    }
}