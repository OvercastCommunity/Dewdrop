package tc.oc.occ.dewdrop.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import tc.oc.occ.cobweb.definitions.UpsertPGMMapDTO;
import tc.oc.pgm.api.map.Gamemode;
import tc.oc.pgm.api.map.MapInfo;
import tc.oc.pgm.api.map.MapTag;

public class MapData {
  private static List<String> conquestGamemodes =
      Stream.of(
              Gamemode.CAPTURE_THE_FLAG,
              Gamemode.KING_OF_THE_FLAG,
              Gamemode.KING_OF_THE_HILL,
              Gamemode.CONTROL_THE_POINT,
              Gamemode.DEATHMATCH)
          .map(Gamemode::getAcronym)
          .collect(Collectors.toList());

  public static UpsertPGMMapDTO populateMap(MapInfo pgmMap) {
    UpsertPGMMapDTO map = new UpsertPGMMapDTO();

    map.setName(pgmMap.getName());
    map.setSlug(pgmMap.getId());
    map.setTeamCount(pgmMap.getMaxPlayers().size());
    map.setPlayerCount(pgmMap.getMaxPlayers().stream().reduce(0, Integer::sum));
    map.setUniform(
        new HashSet<Integer>(new ArrayList<Integer>(pgmMap.getMaxPlayers())).size() <= 1);

    List<String> mapTags =
        pgmMap.getTags().stream()
            .filter(MapTag::isGamemode)
            .map(mapTag -> PlainTextComponentSerializer.plainText().serialize(mapTag.getAcronym()))
            .collect(Collectors.toList());

    if (mapTags.stream().anyMatch(MapData.conquestGamemodes::contains)) mapTags.add("CONQ");

    map.setMapTags(mapTags);
    return map;
  }
}
