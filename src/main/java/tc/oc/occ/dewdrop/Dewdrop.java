package tc.oc.occ.dewdrop;

import org.bukkit.plugin.java.JavaPlugin;

public class Dewdrop extends JavaPlugin {

    private static Dewdrop PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;

        this.saveDefaultConfig();
        this.reloadConfig();

        // TODO:
    }

    @Override
    public void onDisable() {

    }

    public static Dewdrop get() {
        return PLUGIN;
    }

}
