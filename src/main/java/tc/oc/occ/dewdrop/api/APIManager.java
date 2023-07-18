package tc.oc.occ.dewdrop.api;

import java.util.logging.Logger;
import tc.oc.occ.cobweb.ApiClient;
import tc.oc.occ.cobweb.ApiException;
import tc.oc.occ.cobweb.Configuration;
import tc.oc.occ.cobweb.api.MatchesApi;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.config.AppData;

public class APIManager {

  private ApiClient client;
  private final MatchesApi matchesApi;
  private final Logger logger;

  public APIManager(Logger logger) {
    this.logger = logger;
    loadClient(AppData.API.getURL(), AppData.API.getKey());
    this.matchesApi = new MatchesApi(client);
  }

  private void loadClient(String apiURL, String apiKey) {
    this.client = Configuration.getDefaultApiClient();
    this.client.setBasePath(apiURL).setBearerToken(apiKey);

    logger.info("Created new Cobweb client instance...");
  }

  public ApiClient getClient() {
    return client;
  }

  public MatchDTO getMatch(String matchId) {
    try {
      return matchesApi.matchesControllerGetMatch(matchId);
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
  }

  public MatchDTO createMatch(CreateMatchDTO match) {
    int retries = 40;

    for (int i = 0; i < retries; ) {
      try {
        return matchesApi.matchesControllerCreateMatch(match);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      i += 1;
      System.out.println(
          "[Dewdrop] Failed to report match end, retrying in "
              + (i * 5)
              + "s ("
              + i
              + "/"
              + retries
              + ")");
      try {
        Thread.sleep(i * 5000L);
      } catch (InterruptedException ignore) {
      }
    }
    return null;
  }
}
