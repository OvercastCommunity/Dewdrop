package tc.oc.occ.dewdrop.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Description;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.api.APIManager;

public class TestCommand extends BaseCommand {

  @Dependency private APIManager apiManager;

  @CommandAlias("test")
  @Description("Test")
  public void test(CommandSender sender, String matchId) {
    MatchDTO match = apiManager.getMatch(matchId);
    if (match != null) {
      sender.sendMessage(
          ChatColor.YELLOW
              + "Match"
              + ChatColor.GRAY
              + ": "
              + ChatColor.AQUA
              + ChatColor.BOLD.toString()
              + match.toString());
    }
  }
}
