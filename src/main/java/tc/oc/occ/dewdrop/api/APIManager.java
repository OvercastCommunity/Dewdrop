package tc.oc.occ.dewdrop.api;

import java.util.UUID;
import java.util.logging.Logger;
import tc.oc.occ.cobweb.ApiClient;
import tc.oc.occ.cobweb.ApiException;
import tc.oc.occ.cobweb.Configuration;
import tc.oc.occ.cobweb.api.MapsApi;
import tc.oc.occ.cobweb.api.MatchesApi;
import tc.oc.occ.cobweb.api.UsersApi;
import tc.oc.occ.cobweb.definitions.BasicUserDTO;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.cobweb.definitions.PGMMapDTO;
import tc.oc.occ.cobweb.definitions.ProfileStatsDTO;
import tc.oc.occ.cobweb.definitions.UpsertPGMMapDTO;
import tc.oc.occ.cobweb.definitions.UserDTO;
import tc.oc.occ.dewdrop.config.AppData;

public class APIManager {

  private ApiClient client;
  private final Logger logger;

  private final MatchesApi matchesApi;
  private final MapsApi mapsApi;
  private final UsersApi usersApi;

  public APIManager(Logger logger) {
    this.logger = logger;
    loadClient(AppData.API.getURL(), AppData.API.getKey());

    this.matchesApi = new MatchesApi(client);
    this.mapsApi = new MapsApi(client);
    this.usersApi = new UsersApi(client);
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

  public ProfileStatsDTO getProfileStats(UUID userUuid) {
    try {
      return usersApi.usersControllerGetStats(userUuid.toString(), null, null, null);
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

  public PGMMapDTO upsertMap(UpsertPGMMapDTO map) {
    int retries = 40;

    for (int i = 0; i < retries; ) {
      try {
        return mapsApi.mapsControllerUpsertMap(map.getMapSlug(), map);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      i += 1;
      System.out.println(
          "[Dewdrop] Failed to upsert map, retrying in "
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

  public UserDTO upsertUser(BasicUserDTO user) {
    try {
      return usersApi.usersControllerUpsertUser(user.getUserUuid().toString(), user);
    } catch (ApiException e) {
      throw new RuntimeException(e);
    }
  }
}
