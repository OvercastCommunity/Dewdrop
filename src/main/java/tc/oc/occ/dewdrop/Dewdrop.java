package tc.oc.occ.dewdrop;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.commands.TestCommand;
import tc.oc.occ.dewdrop.managers.MapManager;
import tc.oc.occ.dewdrop.managers.MatchManager;
import tc.oc.occ.dewdrop.managers.PlayerManager;

public class Dewdrop extends JavaPlugin {

  private static Dewdrop PLUGIN;

  private APIManager apiManager;

  private MapManager mapManager;
  private MatchManager matchManager;
  private PlayerManager playerManager;

  private BukkitCommandManager commands;

  @Override
  public void onEnable() {
    PLUGIN = this;

    this.saveDefaultConfig();
    this.reloadConfig();

    this.apiManager = new APIManager(getLogger());

    this.mapManager = new MapManager(this, apiManager);
    this.matchManager = new MatchManager(this, apiManager);
    this.playerManager = new PlayerManager(this, apiManager);

    this.commands = new BukkitCommandManager(this);
    commands.registerDependency(APIManager.class, apiManager);
    commands.registerCommand(new TestCommand());
  }

  @Override
  public void onDisable() {}

  public static Dewdrop get() {
    return PLUGIN;
  }
}
