package tc.oc.occ.dewdrop.utils;

import static net.kyori.adventure.text.Component.text;
import static tc.oc.occ.dewdrop.utils.Components.link;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.config.AppData;

public class Messages {
  public static Component matchLink(MatchDTO match) {
    String url = AppData.Web.getMatch().replace("{matchId}", match.getMatchId());

    return text("Match link: ", NamedTextColor.WHITE)
        .append(link(Style.style(NamedTextColor.BLUE, TextDecoration.UNDERLINED), url));
  }
}
