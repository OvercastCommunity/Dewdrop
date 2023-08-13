package tc.oc.occ.dewdrop.commands;

import static net.kyori.adventure.text.Component.text;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import tc.oc.occ.cobweb.definitions.ProfileStatsDTO;
import tc.oc.occ.dewdrop.Dewdrop;
import tc.oc.occ.dewdrop.api.APIManager;
import tc.oc.occ.dewdrop.utils.Messages;

@CommandAlias("dewdrop")
public class StatsCommand extends BaseCommand {

  private static final String VIEW_PERMISSION = "occ.dewdrop.stats";

  @Dependency private APIManager apiManager;

  @Subcommand("stats|statistics")
  @Description("View your statistics")
  @CommandPermission(VIEW_PERMISSION)
  public void viewStats(Player sender) {
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
