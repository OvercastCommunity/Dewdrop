package tc.oc.occ.dewdrop.managers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tc.oc.occ.cobweb.definitions.UpsertPGMMapDTO;
import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.DewdropManager;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.utils.MapData;
import tc.oc.pgm.api.match.event.MatchLoadEvent;

public class MapManager extends DewdropManager implements Listener {
  public MapManager(Dewdrop plugin, APIManager api) {
    super(plugin, api);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onMatchLoad(MatchLoadEvent event) {
    UpsertPGMMapDTO map = MapData.populateMap(event.getMatch().getMap());
    Dewdrop.newSharedChain("main").async(() -> apiManager.upsertMap(map)).execute();
  }
}
