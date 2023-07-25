package tc.oc.occ.dewdrop.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import tc.oc.occ.cobweb.definitions.BasicUserDTO;
import tc.oc.occ.cobweb.definitions.UserDTO;
import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.DewdropManager;
import tc.oc.occ.dewdrop.api.APIManager;

public class PlayerManager extends DewdropManager implements Listener {
  private final Cache<UUID, UserDTO> userCache =
      CacheBuilder.newBuilder()
          .expireAfterWrite(30, TimeUnit.SECONDS)
          .expireAfterAccess(30, TimeUnit.SECONDS)
          .build();

  public PlayerManager(Dewdrop plugin, APIManager api) {
    super(plugin, api);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerLogin(AsyncPlayerPreLoginEvent ple) {
    // use cached value if exists
    if (userCache.getIfPresent(ple.getUniqueId()) != null) return;

    BasicUserDTO user = new BasicUserDTO();
    user.setUserUuid(ple.getUniqueId());
    user.setUsername(ple.getName());

    // query api and save to cache
    Dewdrop.newChain()
        .asyncFirst(() -> apiManager.upsertUser(user))
        .abortIfNull()
        .syncLast(response -> userCache.put(user.getUserUuid(), response))
        .execute();
  }
}
