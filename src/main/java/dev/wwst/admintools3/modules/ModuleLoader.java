package dev.wwst.admintools3.modules;

import com.google.common.collect.Lists;
import dev.wwst.admintools3.AdminTools3;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;

import java.util.List;
import java.util.logging.Level;

public class ModuleLoader {

    private final List<Module> modules;
    private final List<String> aliases;
    private final List<String> commandAliases;

    private static ModuleLoader INSTANCE;

    public ModuleLoader() {
        INSTANCE = this;
        modules = Lists.newArrayList();
        aliases = Lists.newArrayList();
        commandAliases = Lists.newArrayList();

        registerModule(new HealModule());
        registerModule(new KillModule());
        registerModule(new EnderchestModule());
        registerModule(new WorkbenchModule());
        registerModule(new FlyModule());
        registerModule(new MuteModule());
        registerModule(new FreezeModule());
        registerModule(new InvSeeModule());
        registerModule(new VanishModule());
        registerModule(new GamemodeModule());
        registerModule(new ChatClearModule());
        registerModule(new CommandSpyModule());
        registerModule(new MaxHeartsModule());
        registerModule(new InvClearModule());
        registerModule(new DifficultyChangeModule());
        registerModule(new WorldTPModule());
        registerModule(new PvpChangeModule());
        registerModule(new SunnyWeatherModule());
        registerModule(new SetSpawnModule());

        AdminTools3.getInstance().getLogger().log(Level.INFO,modules.size()+" modules loaded.");
    }

    public static ModuleLoader getInstance() {
        return INSTANCE;
    }

    public List<Module> getModuleList() {
        return modules;
    }

    public void registerModule(Module module) {
        for(Module m : modules) {
            if(m.getName().equals(module.getName())) return;
        }
        modules.add(module);
        aliases.addAll(module.getAliases());
        if(module.shouldRegisterCommands()) {
            commandAliases.addAll(module.getAliases());
        }
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getCommandAliases() {
        return aliases;
    }
}