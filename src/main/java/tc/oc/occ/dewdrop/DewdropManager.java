package tc.oc.occ.dewdrop;

import java.util.logging.Logger;
import tc.oc.occ.dewdrop.api.APIManager;

public abstract class DewdropManager {

  protected final Dewdrop plugin;
  protected final APIManager apiManager;
  protected final Logger logger;

  public DewdropManager(Dewdrop plugin, APIManager apiManager) {
    this.plugin = plugin;
    this.logger = plugin.getLogger();
    this.apiManager = apiManager;
  }
}
