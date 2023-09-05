package tc.oc.occ.dewdrop.commands;

import static net.kyori.adventure.text.Component.text;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tc.oc.occ.cobweb.definitions.ProfileStatsDTO;
import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.utils.Messages;

public class StatsCommand extends BaseCommand implements Listener {

  private static final String VIEW_PERMISSION = "occ.stats.view";

  private final APIManager apiManager;

  public StatsCommand(Dewdrop plugin, APIManager apiManager) {
    this.apiManager = apiManager;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @Subcommand("stats|statistics")
  @Description("View your statistics")
  @CommandPermission(VIEW_PERMISSION)
  public void viewStats(Player sender) {
    sendStats(sender);
  }

  @EventHandler
  public void onStatsOverride(PlayerCommandPreprocessEvent event) {
    if (!event.getMessage().startsWith("/stats")) return;
    sendStats(event.getPlayer());
  }

  public void sendStats(Player sender) {
    Audience viewer = Dewdrop.get().audienceOf(sender);

    ProfileStatsDTO stats = apiManager.getProfileStats(sender.getUniqueId());
    Dewdrop.newSharedChain("stats")
        .asyncFirst(() -> apiManager.getProfileStats(sender.getUniqueId()))
        .syncLast(
            profileStats -> {
              if (profileStats == null) {
                viewer.sendMessage(
                    text(
                        "Uh oh! Could not load your stats. Please contact an admin.",
                        NamedTextColor.RED));
                return;
              }
              ;

              Messages.stats(profileStats).forEach(viewer::sendMessage);
            })
        .execute();
  }
}
