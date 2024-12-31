package tc.oc.occ.dewdrop.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import tc.oc.occ.cobweb.definitions.CreateMatchDTO;
import tc.oc.occ.cobweb.definitions.CreateParticipationDTO;
import tc.oc.occ.cobweb.definitions.CreateStatsDTO;
import tc.oc.occ.cobweb.definitions.CreateTeamDTO;
import tc.oc.occ.cobweb.definitions.CreateWinnerTeamDTO;
import tc.oc.occ.dewdrop.config.AppData;
import tc.oc.pgm.api.match.Match;
import tc.oc.pgm.api.party.Competitor;
import tc.oc.pgm.ffa.FreeForAllMatchModule;
import tc.oc.pgm.score.ScoreMatchModule;
import tc.oc.pgm.stats.PlayerStats;
import tc.oc.pgm.stats.StatsMatchModule;
import tc.oc.pgm.teams.Team;

public class MatchData {
  public static CreateMatchDTO populateNewMatch(Match pgmMatch) {
    ScoreMatchModule scoreModule = pgmMatch.getModule(ScoreMatchModule.class);
    StatsMatchModule statsModule = pgmMatch.moduleRequire(StatsMatchModule.class);

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
              .map(pair -> populateNewParticipation(pair, scoreModule))
              .peek(participation -> participation.setPrimary(true))
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        winner.get().getPlayers().stream()
            .findFirst()
            .ifPresent(player -> match.setWinnerUserUuid(player.getId()));
      }
    } else {
      match.setTeams(
          pgmMatch.getCompetitors().stream()
              .map(
                  competitor -> {
                    CreateTeamDTO team = new CreateTeamDTO();

                    team.setName(competitor.getNameLegacy());
                    team.setColor(
                        "#" + String.format("%06X", 0xFFFFFF & competitor.getFullColor().asRGB()));
                    team.setScore(
                        scoreModule != null ? (int) scoreModule.getScore(competitor) : null);

                    team.setParticipations(
                        statsModule.getParticipationStats().row((Team) competitor).entrySet()
                            .stream()
                            .map(pair -> populateNewParticipation(pair, scoreModule))
                            .peek(
                                participation ->
                                    participation.setPrimary(
                                        statsModule.getPrimaryTeam(
                                                participation.getUserUuid(), false)
                                            == (Team) competitor))
                            .collect(Collectors.toList()));

                    return team;
                  })
              .collect(Collectors.toList()));

      if (winner.isPresent()) {
        CreateWinnerTeamDTO winnerTeam = new CreateWinnerTeamDTO();

        winnerTeam.setName(winner.get().getNameLegacy());
        winnerTeam.setColor("#" + Integer.toHexString(winner.get().getFullColor().asRGB()));
        winnerTeam.setScore(scoreModule != null ? (int) scoreModule.getScore(winner.get()) : null);

        match.setWinnerTeam(winnerTeam);
      }
    }

    match.setMapSlug(pgmMatch.getMap().getId());
    match.setSeriesId(AppData.getSeriesId());

    return match;
  }

  private static CreateParticipationDTO populateNewParticipation(
      Map.Entry<UUID, PlayerStats> pair, ScoreMatchModule scoreModule) {
    UUID userUuid = pair.getKey();
    PlayerStats playerStats = pair.getValue();

    CreateParticipationDTO participation = new CreateParticipationDTO();

    participation.setUserUuid(userUuid);

    if (playerStats == null) return participation;

    participation.setDuration(playerStats.getTimePlayed().toMillis());

    CreateStatsDTO stats = new CreateStatsDTO();
    stats.setKills(playerStats.getKills());
    stats.setDeaths(playerStats.getDeaths());
    stats.setAssists(playerStats.getAssists());
    stats.setKillstreak(playerStats.getMaxKillstreak());
    stats.setDamageDealt(BigDecimal.valueOf(playerStats.getDamageDone()));
    stats.setDamageDealtBow(BigDecimal.valueOf(playerStats.getBowDamage()));
    stats.setDamageReceived(BigDecimal.valueOf(playerStats.getDamageTaken()));
    stats.setDamageReceivedBow(BigDecimal.valueOf(playerStats.getBowDamageTaken()));
    stats.setArrowsHit(playerStats.getShotsHit());
    stats.setArrowsShot(playerStats.getShotsTaken());

    stats.setWools(playerStats.getWoolsCaptured());
    stats.setWoolsTouched(playerStats.getWoolsTouched());
    stats.setMonuments(playerStats.getMonumentsDestroyed());
    stats.setCores(playerStats.getCoresLeaked());
    stats.setFlags(playerStats.getFlagsCaptured());
    stats.setFlagsPicked(playerStats.getFlagPickups());

    stats.setScore(scoreModule != null ? (int) scoreModule.getContribution(userUuid) : 0);

    participation.setStats(stats);

    return participation;
  }
}
