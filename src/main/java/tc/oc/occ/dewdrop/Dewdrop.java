package tc.oc.occ.dewdrop;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.commands.StatsCommand;
import tc.oc.occ.dewdrop.managers.MapManager;
import tc.oc.occ.dewdrop.managers.MatchManager;
import tc.oc.occ.dewdrop.managers.PlayerManager;

public class Dewdrop extends JavaPlugin {

  private static Dewdrop PLUGIN;
  private static TaskChainFactory taskChainFactory;

  private APIManager apiManager;

  private MapManager mapManager;
  private MatchManager matchManager;
  private PlayerManager playerManager;

  private BukkitAudiences adventure;
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

    this.adventure = BukkitAudiences.create(this);

    this.commands = new BukkitCommandManager(this);
    commands.registerDependency(APIManager.class, apiManager);
    commands.registerCommand(new StatsCommand(this, apiManager));

    taskChainFactory = BukkitTaskChainFactory.create(this);
  }

  @Override
  public void onDisable() {}

  public static Dewdrop get() {
    return PLUGIN;
  }

  public static <T> TaskChain<T> newChain() {
    return taskChainFactory.newChain();
  }

  public static <T> TaskChain<T> newSharedChain(String name) {
    return taskChainFactory.newSharedChain(name);
  }

  public Audience audienceOf(Player sender) {
    return adventure.player(sender);
  }
}
