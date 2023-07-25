package tc.oc.occ.dewdrop.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.CreateParticipationDTO;
import tc.oc.occ.cobweb.definitions.CreateStatsDTO;
import tc.oc.occ.cobweb.definitions.CreateTeamDTO;
import tc.oc.occ.cobweb.definitions.CreateWinnerTeamDTO;
import tc.oc.occ.dewdrop.config.AppData;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.ffa.FreeForAllMatchModule;
import tc.oc.pgm.score.ScoreMatchModule;
import tc.oc.pgm.stats.PlayerStats;
import tc.oc.pgm.stats.StatsMatchModule;
import tc.oc.pgm.teams.Team;

public class MatchData {
  public static @Nullable CreateMatchDTO populateNewMatch(Match pgmMatch) {
    ScoreMatchModule scoreModule = pgmMatch.getModule(ScoreMatchModule.class);
    StatsMatchModule statsModule = pgmMatch.getModule(StatsMatchModule.class);

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
          statsModule.getStats().entrySet().stream()
              .map(MatchData::populateNewParticipation)
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        MatchPlayer player = winner.get().getPlayers().stream().findFirst().get();
        match.setWinnerUserUuid(player.getBukkit().getUniqueId());
      }
    } else {
      match.setTeams(
          pgmMatch.getCompetitors().stream()
              .map(
                  competitor -> {
                    CreateTeamDTO team = new CreateTeamDTO();

                    team.setName(competitor.getNameLegacy());
                    team.setColor("#" + Integer.toHexString(competitor.getFullColor().asRGB()));
                    team.setScore(
                        scoreModule != null
                            ? BigDecimal.valueOf(scoreModule.getScore(competitor))
                            : null);

                    team.setParticipations(
                        statsModule.getParticipationStats().row((Team) competitor).entrySet()
                            .stream()
                            .map(MatchData::populateNewParticipation)
                            .collect(Collectors.toList()));

                    return team;
                  })
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        CreateWinnerTeamDTO winnerTeam = new CreateWinnerTeamDTO();

        winnerTeam.setName(winner.get().getNameLegacy());
        winnerTeam.setColor("#" + Integer.toHexString(winner.get().getFullColor().asRGB()));
        winnerTeam.setScore(
            scoreModule != null ? BigDecimal.valueOf(scoreModule.getScore(winner.get())) : null);

        match.setWinnerTeam(winnerTeam);
      }
    }

    match.setMapSlug(pgmMatch.getMap().getId());
    match.setSeriesId(BigDecimal.valueOf(AppData.getSeriesId()));

    return match;
  }

  private static CreateParticipationDTO populateNewParticipation(
      Map.Entry<UUID, PlayerStats> pair) {
    UUID userUuid = pair.getKey();
    PlayerStats playerStats = pair.getValue();

    CreateParticipationDTO participation = new CreateParticipationDTO();

    participation.setUserUuid(userUuid);

    if (playerStats == null) return participation;

    CreateStatsDTO stats = new CreateStatsDTO();
    stats.setKills(BigDecimal.valueOf(playerStats.getKills()));
    stats.setDeaths(BigDecimal.valueOf(playerStats.getDeaths()));
    stats.setKillstreak(BigDecimal.valueOf(playerStats.getMaxKillstreak()));
    stats.setDamageDealt(BigDecimal.valueOf(playerStats.getDamageDone()));
    stats.setDamageDealtBow(BigDecimal.valueOf(playerStats.getBowDamage()));
    stats.setDamageReceived(BigDecimal.valueOf(playerStats.getDamageTaken()));
    stats.setDamageReceivedBow(BigDecimal.valueOf(playerStats.getBowDamageTaken()));
    stats.setArrowsHit(BigDecimal.valueOf(playerStats.getShotsHit()));
    stats.setArrowsShot(BigDecimal.valueOf(playerStats.getShotsTaken()));

    stats.setWools(BigDecimal.valueOf(playerStats.getWoolsCaptured()));
    stats.setMonuments(BigDecimal.valueOf(playerStats.getMonumentsDestroyed()));
    stats.setCores(BigDecimal.valueOf(playerStats.getCoresLeaked()));
    stats.setFlags(BigDecimal.valueOf(playerStats.getFlagsCaptured()));
    stats.setHills(BigDecimal.valueOf(0)); // Revisit this later

    stats.setScore(BigDecimal.valueOf(0)); // Revisit this later

    participation.setStats(stats);

    return participation;
  }
}
