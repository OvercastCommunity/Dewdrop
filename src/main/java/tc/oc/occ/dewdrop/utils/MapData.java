package tc.oc.occ.dewdrop.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import tc.oc.occ.cobweb.definitions.UpsertPGMMapDTO;
import tc.oc.pgm.api.map.Gamemode;
import tc.oc.pgm.api.map.MapInfo;

public class MapData {
  private static final Set<Gamemode> CONQUEST_GAMEMODES =
      EnumSet.of(
          Gamemode.CAPTURE_THE_FLAG,
          Gamemode.KING_OF_THE_FLAG,
          Gamemode.KING_OF_THE_HILL,
          Gamemode.CONTROL_THE_POINT,
          Gamemode.DEATHMATCH);

  /** Translates PGM gamemodes into Cobweb map tags */
  public static Set<String> getMapTagsFromMap(MapInfo map) {
    Set<String> mapTags = new HashSet<String>();

    for (Gamemode gamemode : map.getGamemodes()) {
      switch (gamemode) {
        case ARCADE:
          mapTags.add("FUN");
          break;
        case BEDWARS:
          mapTags.add("BW");
          break;
        case BLITZ_RAGE:
          mapTags.add("BLITZ");
          mapTags.add("RAGE");
          break;
        case FLAG_FOOTBALL:
          mapTags.add("FF");
          break;
        case BLITZ:
        case BRIDGE:
        case CONTROL_THE_POINT:
        case CAPTURE_THE_FLAG:
        case CAPTURE_THE_WOOL:
        case DESTROY_THE_CORE:
        case DESTROY_THE_MONUMENT:
        case KING_OF_THE_HILL:
        case KING_OF_THE_FLAG:
        case RAGE:
        case DEATHMATCH:
          mapTags.add(gamemode.getId().toUpperCase());
          break;

          // A/D, FFA, Infection, Mixed, Payload, Race for Wool, Scorebox, Skywars and SG are
          // missing as Cobweb map tags
      }
    }

    if (map.getGamemodes().stream().anyMatch(CONQUEST_GAMEMODES::contains)) mapTags.add("CONQ");
    return mapTags;
  }

  public static UpsertPGMMapDTO populateMap(MapInfo pgmMap) {
    UpsertPGMMapDTO map = new UpsertPGMMapDTO();

    map.setName(pgmMap.getName());
    map.setMapSlug(pgmMap.getId());
    map.setTeamCount(pgmMap.getMaxPlayers().size());
    map.setPlayerCount(pgmMap.getMaxPlayers().stream().reduce(0, Integer::sum));
    map.setUniform(
        new HashSet<Integer>(new ArrayList<Integer>(pgmMap.getMaxPlayers())).size() <= 1);

    map.setMapTags(new ArrayList<>(getMapTagsFromMap(pgmMap)));
    return map;
  }
}
