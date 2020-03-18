package dev.wwst.admintools3.modules;

import com.google.common.collect.Lists;

import java.util.List;

public class ModuleLoader {

    private List<Module> modules;

    private static ModuleLoader INSTANCE;

    public ModuleLoader() {
        INSTANCE = this;
        modules = Lists.newArrayList();

        modules.add(new HealModule());

    }

    public static ModuleLoader getInstance() {
        return INSTANCE;
    }

    public List<Module> getModuleList() {
        return modules;
    }
}
