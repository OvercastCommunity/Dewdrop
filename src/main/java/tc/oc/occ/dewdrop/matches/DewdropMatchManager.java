package tc.oc.occ.dewdrop.matches;

import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.DewdropManager;
import tc.oc.occ.dewdrop.cobweb.CobwebAPI;

public class DewdropMatchManager extends DewdropManager {

  public DewdropMatchManager(Dewdrop plugin, CobwebAPI api) {
      super(plugin, api);
      
      registerListener(new MatchListener(this));
  }

  
}
