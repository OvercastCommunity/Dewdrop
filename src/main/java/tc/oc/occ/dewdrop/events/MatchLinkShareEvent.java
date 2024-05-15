package tc.oc.occ.dewdrop.events;

import tc.oc.occ.cobweb.definitions.MatchDTO;
import tc.oc.occ.dewdrop.config.AppData;

public class MatchLinkShareEvent extends DewdropEvent {

  private String matchLink;

  public MatchLinkShareEvent(MatchDTO match) {
    this.matchLink = AppData.Web.getMatch().replace("{matchId}", match.getMatchId());
  }

  public String getMatchLink() {
    return matchLink;
  }
}
