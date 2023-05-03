package tc.oc.occ.dewdrop.cobweb;

import java.util.logging.Logger;
import tc.oc.occ.cobweb.ApiClient;
import tc.oc.occ.cobweb.Configuration;
import tc.oc.occ.dewdrop.DewdropConfig;

public class CobwebAPI {

  private ApiClient client;
  private final Logger logger;

  public CobwebAPI(DewdropConfig config, Logger logger) {
    this.logger = logger;
    loadClient(config.getApiURL());
  }

  private void loadClient(String apiURL) {
    this.client = Configuration.getDefaultApiClient();
    this.client.setBasePath(apiURL);
    logger.info("Created new Cobweb client instance...");
  }

  public ApiClient getClient() {
    return client;
  }
}
