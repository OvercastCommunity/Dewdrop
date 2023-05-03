package tc.oc.occ.dewdrop;

import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.occ.dewdrop.cobweb.CobwebAPI;
import tc.oc.occ.dewdrop.commands.TestCommand;
import tc.oc.occ.dewdrop.matches.DewdropMatchManager;

public class Dewdrop extends JavaPlugin {

  private static Dewdrop PLUGIN;

  private CobwebAPI cobweb;
  private DewdropConfig config;
  private DewdropMatchManager matchManager;
  private BukkitCommandManager commands;

  @Override
  public void onEnable() {
    PLUGIN = this;

    this.saveDefaultConfig();
    this.reloadConfig();

    this.config = new DewdropConfig(getConfig());
    this.cobweb = new CobwebAPI(config, getLogger());
    this.matchManager = new DewdropMatchManager(this, cobweb);

    this.commands = new BukkitCommandManager(this);
    commands.registerDependency(DewdropMatchManager.class, matchManager);
    commands.registerCommand(new TestCommand());
  }

  @Override
  public void onDisable() {}

  public static Dewdrop get() {
    return PLUGIN;
  }
}
