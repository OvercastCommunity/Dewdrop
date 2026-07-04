package tc.oc.occ.dewdrop.utils;

import static net.kyori.adventure.text.Component.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

public class Components {
  public static Component link(Style style, String url) {
    return text(url, style)
        .clickEvent(ClickEvent.openUrl(url))
        .hoverEvent(text("Click to visit ", NamedTextColor.YELLOW).append(text(url, style)));
  }
}
