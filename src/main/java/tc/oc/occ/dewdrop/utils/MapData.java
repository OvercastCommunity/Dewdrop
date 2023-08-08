package tc.oc.occ.dewdrop.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tc.oc.occ.cobweb.definitions.UpsertPGMMapDTO;
import tc.oc.pgm.api.map.Gamemode;
import tc.oc.pgm.api.map.MapInfo;

public class MapData {
  private static List<String> conquestGamemodes =
      Stream.of("CTF", "KOTF", "KOTH", "CP", "TDM").collect(Collectors.toList());

  /** Translates PGM gamemodes into Cobweb map tags */
  public static Set<String> getMapTagsFromMap(MapInfo map) {
    Set<String> mapTags = new HashSet<String>();

    for (Gamemode gamemode : map.getGamemodes()) {
      switch (gamemode.getId()) {
        case "arcade":
          mapTags.add("FUN");
          break;
        case "bedwars":
          mapTags.add("BW");
          break;
        case "br":
          mapTags.add("BLITZ");
          mapTags.add("RAGE");
          break;
        case "ffb":
          mapTags.add("FF");
          break;
        case "blitz":
        case "bridge":
        case "cp":
        case "ctf":
        case "ctw":
        case "dtc":
        case "dtm":
        case "koth":
        case "kotf":
        case "rage":
        case "tdm":
          mapTags.add(gamemode.getId().toUpperCase());
          break;

          // A/D, FFA, Infection, Mixed, Payload, Race for Wool, Scorebox, Skywars and SG are
          // missing as Cobweb map tags
      }
    }

    if (mapTags.stream().anyMatch(MapData.conquestGamemodes::contains)) mapTags.add("CONQ");
    return mapTags;
  }

  public static UpsertPGMMapDTO populateMap(MapInfo pgmMap) {
    UpsertPGMMapDTO map = new UpsertPGMMapDTO();

    map.setName(pgmMap.getName());
    map.setSlug(pgmMap.getId());
    map.setTeamCount(pgmMap.getMaxPlayers().size());
    map.setPlayerCount(pgmMap.getMaxPlayers().stream().reduce(0, Integer::sum));
    map.setUniform(
        new HashSet<Integer>(new ArrayList<Integer>(pgmMap.getMaxPlayers())).size() <= 1);

    map.setMapTags(new ArrayList<>(getMapTagsFromMap(pgmMap)));
    return map;
  }
}
