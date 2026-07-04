package tc.oc.occ.dewdrop.events;

import org.bukkit.event.HandlerList;
import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.config.AppData;

public class MatchLinkShareEvent extends DewdropEvent {

  private static final HandlerList handlers = new HandlerList();

  private final String matchLink;

  public MatchLinkShareEvent(MatchDTO match) {
    this.matchLink = AppData.Web.getMatch().replace("{matchId}", match.getMatchId());
  }

  public String getMatchLink() {
    return matchLink;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
