package tc.oc.occ.dewdrop;

import org.bukkit.configuration.Configuration;

public class DewdropConfig {

    // api
    private String apiURL;
    
    // series
    private int seriesId;
    private String seriesName;
    private String seriesService;

    // server
    private String serverName;
    
    public DewdropConfig(Configuration config) {
        reload(config);
    }

    public void reload(Configuration config) {
        this.apiURL = config.getString("api.url");
        this.seriesId = config.getInt("series.id");
        this.seriesName = config.getString("series.name");
        this.seriesService = config.getString("series.service");
        this.serverName = config.getString("server-name");
    }

    public String getApiURL() {
        return apiURL;
    }
    
    public int getSeriesId() {
        return seriesId;
    }
    
    public String getSeriesName() {
        return seriesName;
    }
    
    public String getSeriesService() {
        return seriesService;
    }

    public String getServerName() {
        String envName = System.getenv("SERVER_NAME");
        return envName != null ? envName : serverName;
    }

}
