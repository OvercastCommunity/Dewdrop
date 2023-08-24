package tc.oc.occ.dewdrop.config;

import tc.oc.occ.dewdrop.Dewdrop;

public class AppData {
  public static class API {

    public static String getURL() {
      return Dewdrop.get().getConfig().getString("api.url");
    }

    public static String getKey() {
      return Dewdrop.get().getConfig().getString("api.key");
    }
  }

  public static int getSeriesId() {
    return Dewdrop.get().getConfig().getInt("series-id");
  }

  public static class Web {

    public static String getMatch() {
      return Dewdrop.get().getConfig().getString("web.match", null);
    }

    public static String getProfile() {
      return Dewdrop.get().getConfig().getString("web.profile", null);
    }
  }

  public static String getServerName() {
    return Dewdrop.get()
        .getConfig()
        .getString("server-name", String.valueOf(System.getenv("SERVER_NAME")));
  }
}
