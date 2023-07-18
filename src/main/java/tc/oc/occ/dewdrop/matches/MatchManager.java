package tc.oc.occ.dewdrop.matches;

import static net.kyori.adventure.text.Component.empty;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.DewdropManager;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.config.AppData;
import tc.oc.occ.dewdrop.utils.MatchData;
import tc.oc.occ.dewdrop.utils.Messages;
import tc.oc.pgm.api.match.event.MatchFinishEvent;
import tc.oc.pgm.api.match.event.MatchStatsEvent;

public class MatchManager extends DewdropManager implements Listener {

  public MatchManager(Dewdrop plugin, APIManager api) {
    super(plugin, api);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onMatchFinish(MatchFinishEvent event) {
    CreateMatchDTO request = MatchData.populateNewMatch(event.getMatch());
    if (request == null) return;

    MatchDTO response = apiManager.createMatch(request);
    if (response == null || AppData.Web.getMatch() == null) return;

    Bukkit.getScheduler()
        .scheduleAsyncDelayedTask(
            this.plugin,
            () -> {
              Bukkit.getServer()
                  .getPluginManager()
                  .callEvent(new MatchStatsEvent(event.getMatch(), true, true));

              event.getMatch().sendMessage(Messages.matchLink(response));
              event.getMatch().sendMessage(empty());
            },
            130L);
  }
}
