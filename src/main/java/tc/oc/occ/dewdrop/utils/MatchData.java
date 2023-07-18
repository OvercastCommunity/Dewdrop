package tc.oc.occ.dewdrop.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import tc.oc.occ.cobweb.definitions.BasicUserDTO;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.CreatePGMMapDTO;
import tc.oc.occ.cobweb.definitions.CreateParticipationDTO;
import tc.oc.occ.cobweb.definitions.CreateStatsDTO;
import tc.oc.occ.cobweb.definitions.CreateTeamDTO;
import tc.oc.occ.cobweb.definitions.CreateWinnerTeamDTO;
import tc.oc.occ.cobweb.definitions.SeriesDTO;
import tc.oc.occ.dewdrop.config.AppData;
import tc.oc.pgm.api.map.MapInfo;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.ffa.FreeForAllMatchModule;
import tc.oc.pgm.score.ScoreMatchModule;
import tc.oc.pgm.stats.PlayerStats;
import tc.oc.pgm.stats.StatsMatchModule;

public class MatchData {
  public static @Nullable CreateMatchDTO populateNewMatch(Match pgmMatch) {
    ScoreMatchModule scoreModule = pgmMatch.getModule(ScoreMatchModule.class);

    CreateMatchDTO match = new CreateMatchDTO();

    match.setCreatedAt(Instant.now().toString());
    match.setEndedAt(Instant.now().toString());
    match.setStartedAt(Instant.now().minus(pgmMatch.getDuration()).toString());

    match.setStatus(CreateMatchDTO.StatusEnum.ENDED);
    match.setServerName(AppData.getServerName());

    // Forget winning ties
    Optional<Competitor> winner = pgmMatch.getWinners().stream().findFirst();

    if (pgmMatch.hasModule(FreeForAllMatchModule.class)) {
      match.setParticipations(
          pgmMatch.getPlayers().stream()
              .map(MatchData::populateNewParticipation)
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        MatchPlayer player = winner.get().getPlayers().stream().findFirst().get();

        BasicUserDTO winnerUser = new BasicUserDTO();
        winnerUser.setUserUuid(player.getBukkit().getUniqueId());
        winnerUser.setUsername(player.getBukkit().getName());

        match.setWinnerUser(winnerUser);
      }
    } else {
      match.setTeams(
          pgmMatch.getCompetitors().stream()
              .map(
                  competitor -> {
                    CreateTeamDTO team = new CreateTeamDTO();

                    team.setName(competitor.getNameLegacy());
                    team.setColor(
                        "#" + Integer.toHexString(competitor.getFullColor().asRGB()).substring(2));
                    team.setScore(
                        scoreModule != null
                            ? BigDecimal.valueOf(scoreModule.getScore(competitor))
                            : null);

                    team.setParticipations(
                        competitor.getPlayers().stream()
                            .map(MatchData::populateNewParticipation)
                            .collect(Collectors.toList()));

                    return team;
                  })
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        CreateWinnerTeamDTO winnerTeam = new CreateWinnerTeamDTO();

        winnerTeam.setName(winner.get().getNameLegacy());
        winnerTeam.setColor(
            "#" + Integer.toHexString(winner.get().getFullColor().asRGB()).substring(2));
        winnerTeam.setScore(
            scoreModule != null ? BigDecimal.valueOf(scoreModule.getScore(winner.get())) : null);

        match.setWinnerTeam(winnerTeam);
      }
    }

    SeriesDTO series = new SeriesDTO();
    series.setSeriesId(BigDecimal.valueOf(AppData.Series.getSeriesId()));
    series.setName(AppData.Series.getName());
    series.setService(SeriesDTO.ServiceEnum.valueOf(AppData.Series.getService()));

    match.setSeries(series);

    CreatePGMMapDTO map = new CreatePGMMapDTO();
    MapInfo pgmMap = pgmMatch.getMap();
    map.setName(pgmMap.getName());
    map.setSlug(pgmMap.getId());
    map.setTeamCount(BigDecimal.valueOf(pgmMap.getMaxPlayers().size()));
    map.setPlayerCount(BigDecimal.valueOf(pgmMap.getMaxPlayers().stream().reduce(0, Integer::sum)));
    map.setUniform(
        new HashSet<Integer>(new ArrayList<Integer>(pgmMap.getMaxPlayers())).size() <= 1);

    // TODO: Set map tags on maps

    match.setMap(map);

    return match;
  }

  private static CreateParticipationDTO populateNewParticipation(MatchPlayer matchPlayer) {
    StatsMatchModule statsModule = matchPlayer.getMatch().getModule(StatsMatchModule.class);

    CreateParticipationDTO participation = new CreateParticipationDTO();

    BasicUserDTO user = new BasicUserDTO();
    user.setUserUuid(matchPlayer.getBukkit().getUniqueId());
    user.setUsername(matchPlayer.getBukkit().getName());
    participation.setUser(user);

    if (statsModule == null) return participation;

    CreateStatsDTO stats = new CreateStatsDTO();
    PlayerStats playerStats = statsModule.getPlayerStat(matchPlayer);
    if (playerStats != null) {
      stats.setKills(BigDecimal.valueOf(playerStats.getKills()));
      stats.setDeaths(BigDecimal.valueOf(playerStats.getDeaths()));
      stats.setKillstreak(BigDecimal.valueOf(playerStats.getMaxKillstreak()));
      stats.setDamageDealt(BigDecimal.valueOf(playerStats.getDamageDone()));
      stats.setDamageDealtBow(BigDecimal.valueOf(playerStats.getBowDamage()));
      stats.setDamageReceived(BigDecimal.valueOf(playerStats.getDamageTaken()));
      stats.setDamageReceivedBow(BigDecimal.valueOf(playerStats.getBowDamageTaken()));
      stats.setArrowsHit(BigDecimal.valueOf(playerStats.getShotsHit()));
      stats.setArrowsShot(BigDecimal.valueOf(playerStats.getShotsTaken()));

      stats.setWools(BigDecimal.valueOf(0)); // Revisit this later
      stats.setMonuments(BigDecimal.valueOf(0)); // Revisit this later
      stats.setCores(BigDecimal.valueOf(0)); // Revisit this later
      stats.setFlags(BigDecimal.valueOf(playerStats.getFlagsCaptured()));
      stats.setHills(BigDecimal.valueOf(0)); // Revisit this later

      stats.setScore(BigDecimal.valueOf(0)); // Revisit this later
    }
    ;

    participation.setStats(stats);

    return participation;
  }
}
