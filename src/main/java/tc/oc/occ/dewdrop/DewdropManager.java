package tc.oc.occ.dewdrop;

import java.util.logging.Logger;

import org.bukkit.event.Listener;

import tc.oc.occ.dewdrop.cobweb.CobwebAPI;

public abstract class DewdropManager {
    
    private final Dewdrop plugin;
    protected final CobwebAPI api;
    protected final Logger logger;
    
    public DewdropManager(Dewdrop plugin, CobwebAPI api) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.api = api;
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
