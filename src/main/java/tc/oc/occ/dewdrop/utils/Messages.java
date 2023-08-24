package tc.oc.occ.dewdrop.utils;

import static net.kyori.adventure.text.Component.text;
import static tc.oc.occ.dewdrop.utils.Components.link;

import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.cobweb.definitions.ProfileStatsDTO;
import tc.oc.occ.dewdrop.config.AppData;
import tc.oc.pgm.util.text.TextFormatter;

public class Messages {
  public static Component matchLink(MatchDTO match) {
    String url = AppData.Web.getMatch().replace("{matchId}", match.getMatchId());

    return text("Match link: ", NamedTextColor.WHITE)
        .append(link(Style.style(NamedTextColor.BLUE, TextDecoration.UNDERLINED), url));
  }

  public static ArrayList<Component> stats(ProfileStatsDTO stats) {
    ArrayList<Component> rows = new ArrayList<Component>();

    Component div = text(" | ", NamedTextColor.GRAY);

    rows.add(
        TextFormatter.horizontalLineHeading(
            null, text("All-Time Stats", NamedTextColor.DARK_GREEN), NamedTextColor.WHITE));

    Component kills = getStatComponent("Kills", stats.getKills(), NamedTextColor.GREEN);
    Component deaths = getStatComponent("Deaths", stats.getDeaths(), NamedTextColor.RED);
    Component killstreak =
        getStatComponent("Max. Killstreak", stats.getKillstreak(), NamedTextColor.GREEN);

    Component arrowsShot =
        getStatComponent("Arrows Shot", stats.getArrowsShot(), NamedTextColor.DARK_AQUA);
    Component arrowsHit =
        getStatComponent("Arrows Hit", stats.getArrowsHit(), NamedTextColor.DARK_AQUA);

    // PVP
    rows.add(text("PvP Stats", NamedTextColor.AQUA, TextDecoration.BOLD));
    rows.add(
        text().append(kills).append(div).append(deaths).append(div).append(killstreak).build());
    rows.add(text().append(arrowsShot).append(div).append(arrowsHit).build());
    rows.add(text(""));

    // Objectives
    Component wools = getStatComponent("Wools", stats.getWools(), NamedTextColor.GOLD);
    Component flags = getStatComponent("Flags", stats.getFlags(), NamedTextColor.GOLD);
    Component mons = getStatComponent("Monuments", stats.getMonuments(), NamedTextColor.GOLD);
    Component cores = getStatComponent("Cores", stats.getCores(), NamedTextColor.GOLD);

    Component woolTouches =
        getStatComponent("Wools Touched", stats.getWoolsTouched(), NamedTextColor.GOLD);

    Component flagPicks =
        getStatComponent("Flags Picked", stats.getFlagsPicked(), NamedTextColor.GOLD);

    rows.add(text("Objectives", NamedTextColor.AQUA, TextDecoration.BOLD));
    rows.add(
        text()
            .append(wools)
            .append(div)
            .append(flags)
            .append(div)
            .append(mons)
            .append(div)
            .append(cores)
            .build());
    rows.add(text().append(woolTouches).append(div).append(flagPicks).build());
    rows.add(text(""));

    // Matches
    Component wins = getStatComponent("Wins", stats.getWins(), NamedTextColor.GREEN);
    Component ties = getStatComponent("Ties", stats.getTies(), NamedTextColor.YELLOW);
    Component losses =
        getStatComponent(
            "Losses", stats.getMatches() - stats.getWins() - stats.getTies(), NamedTextColor.RED);

    rows.add(text("Matches", NamedTextColor.AQUA, TextDecoration.BOLD));
    rows.add(text().append(wins).append(div).append(ties).append(div).append(losses).build());

    return rows;
  }

  private static Component getStatComponent(String field, long value, NamedTextColor color) {
    return text()
        .append(text(field, NamedTextColor.GRAY))
        .append(text(": ", NamedTextColor.GRAY))
        .append(text(value, color))
        .build();
  }
}
