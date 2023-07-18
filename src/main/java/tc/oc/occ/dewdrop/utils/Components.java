package tc.oc.occ.dewdrop.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

public class Components {
  public static Component link(Style style, String url) {
    return Component.text(url, style)
        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url))
        .hoverEvent(
            Component.text("Click to visit ", NamedTextColor.YELLOW)
                .append(Component.text(url, style)));
  }
}
