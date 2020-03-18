package dev.wwst.admintools3.util;

import dev.wwst.admintools3.AdminTools3;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

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

    private HashMap<String, String> messages;

    public MessageTranslator(String language) {
        if(INSTANCE != null) {
            return;
        }
        INSTANCE = this;
        plugin = AdminTools3.getInstance();
        prefix = Configuration.get().getString("prefix");
        this.language = language;

        saveDefaults();

        File languageFile = new File(plugin.getConfigFolderPath(), "messages_"+language+".yml");
        if(!languageFile.exists()) {
            plugin.getLogger().log(Level.SEVERE, "!!! The language chosen by you, "+language+", cannot be resolved!");
            plugin.getLogger().log(Level.SEVERE, "!!! Create a file called messages_"+language+".yml in the AdminTools folder to start!");
            plugin.getLogger().log(Level.SEVERE, "!!! For now, the ENGLISH language file will be loaded!");

            languageFile =new File(plugin.getConfigFolderPath(), "messages_en.yml");
        }
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(languageFile);
        Map<String, Object> values = cfg.getValues(true);
        messages = new HashMap<>();
        for(String key : values.keySet()) {
            messages.put(key, values.get(key).toString());
        }
        plugin.getLogger().log(Level.INFO, "Language loaded: messages_"+language+".yml");
    }

    private void saveDefaults() {
        for(String translation : translations) {
            plugin.saveResource("messages_"+translation+".yml", true);
            plugin.getLogger().log(Level.INFO, "Default language exported: messages_"+translation+".yml");
        }
    }

    public String getMessageAndReplace(String key, boolean addPrefix, String... replacements) {
        if(!messages.containsKey(key)) {
            return ChatColor.YELLOW+key+ ChatColor.RED +" not found!";
        }
        String message = messages.get(key);
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

    public String getMessage(String key, boolean addPrefix) {
        return getMessageAndReplace(key,addPrefix, "");
    }

    public String getMessage(String key) {
        return getMessageAndReplace(key,false,"");
    }

    public static MessageTranslator getInstance() {
        return INSTANCE;
    }
}