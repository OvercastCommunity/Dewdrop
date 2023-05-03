package tc.oc.occ.dewdrop.matches;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tc.oc.occ.dispense.events.map.PGMMapLoadEvent;

public class MatchListener implements Listener {
    
    private final DewdropMatchManager manager;
    
    public MatchListener(DewdropMatchManager manager) {
        this.manager = manager;
    }
    
    @EventHandler
    public void onMatchStart(PGMMapLoadEvent event) {
        //TODO:
    }

}
