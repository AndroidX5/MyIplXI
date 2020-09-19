package android.bignerd.mydream11;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

    private static List<Match> matches = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();

    private static DataProcessor INSTANCE = null;

    private DataProcessor(List<Match> matches, List<Player> players) {
        DataProcessor.matches.clear();
        DataProcessor.matches.addAll(matches);

        DataProcessor.players.clear();
        DataProcessor.players.addAll(players);
    }

    static DataProcessor getInstance(List<Match> matches, List<Player> players) {
        if (INSTANCE == null) {
            INSTANCE = new DataProcessor(matches, players);
        }
        return INSTANCE;
    }

    static List<Player> getPlayers() {
        return players;
    }

    public static List<Match> getMatches() {
        return matches;
    }

    float getTotalPoints(String teamName) {
        return getTotalBattingPoints(teamName) + getTotalBowlingPoints(teamName) +
                getTotalFieldingPoints(teamName) + getPlaying11PointsPlayer(teamName);
    }

    static float getTotalPlayerPoints(String playerId) {
        return getBattingPointsPlayer(playerId) + getBowlingPointsPlayer(playerId) +
                getFieldingPointsPlayer(playerId) + getPlaying11PointsPlayer(playerId);
    }

    long getLastUpdatedTime() {
        long millis = 0L;
        for (Match match : matches) {
            if (match.updated > millis) {
                millis = match.updated;
            }
        }
        return millis;
    }

    float getTotalBattingPoints(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (String playerId : playersIds) {
            total = total + getBattingPointsPlayer(playerId);
        }
        return total;
    }

    float getTotalBowlingPoints(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (String playerId : playersIds) {
            total = total + getBowlingPointsPlayer(playerId);
        }
        return total;
    }

    float getTotalFieldingPoints(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (String playerId : playersIds) {
            total = total + getFieldingPointsPlayer(playerId);
        }
        return total;
    }

    float getTotalPlaying11Points(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (String playerId : playersIds) {
            total = total + getPlaying11PointsPlayer(playerId);
        }
        return total;
    }

    static float getBattingPointsPlayer(String playerId) {
        float total = 0f;
        for (Match match : matches) {
            if (match.batting != null) {
                for (Match.Batting batting : match.batting) {
                    for (Match.Batting.Score score : batting.scores) {
                        if (playerId.equals(score.pid) && match.activePlayers.contains(score.pid)) {
                            total = total + calculateBattingPoints(score);
                        }
                    }
                }
            }
        }
        return total;
    }

    static float getBowlingPointsPlayer(String playerId) {
        float total = 0f;
        for (Match match : matches) {
            if (match.bowling != null) {
                for (Match.Bowling bowling : match.bowling) {
                    for (Match.Bowling.Score score : bowling.scores) {
                        if (playerId.equals(score.pid) && match.activePlayers.contains(score.pid)) {
                            total = total + calculateBowlingPoints(score);
                        }
                    }
                }
            }
        }
        return total;
    }

    static float getFieldingPointsPlayer(String playerId) {
        float total = 0f;
        for (Match match : matches) {
            if (match.fielding != null) {
                for (Match.Fielding fielding : match.fielding) {
                    for (Match.Fielding.Score score : fielding.scores) {
                        if (playerId.equals(score.pid) && match.activePlayers.contains(score.pid)) {
                            total = total + calculateFieldingPoints(score);
                        }
                    }
                }
            }
        }
        return total;
    }

    static float getPlaying11PointsPlayer(String playerId) {
        float total = 0f;
        for (Match match : matches) {
            if (match.team != null) {
                for (Match.Team team : match.team) {
                    for (Match.Team.Player player : team.players) {
                        if (playerId.equals(player.pid) && match.activePlayers.contains(player.pid)) {
                            total = total + 4;
                        }
                    }
                }
            }
        }
        return total;
    }

    private static float calculateBattingPoints(Match.Batting.Score score) {
        float total = 0;

        total = total + score.runs;
        total = total + score.fours;
        total = total + (score.sixes * 2);

        if (score.runs >= 100) {
            total = total + 16;
        } else if (score.runs >= 50) {
            total = total + 8;
        }

        // Strike rate
        Player player = null;
        for (Player player1 : players) {
            if (score.pid.equals(player1.id)) {
                player = player1;
            }
        }
        if (player != null && !player.isBowler && score.balls >= 10) {
            float strikeRate = score.sr;
            if (strikeRate < 50) {
                total = total - 6;
            } else if (strikeRate < 60) {
                total = total - 4;
            } else if (strikeRate < 70) {
                total = total - 2;
            }
        }

        // duck
        if (score.runs == 0 && player != null && !player.isBowler) {
            total = total - 2;
        }

        return total;
    }

    private static float calculateBowlingPoints(Match.Bowling.Score score) {
        float total = 0;

        total = total + (Float.parseFloat(score.wickets) * 25);
        if (Float.parseFloat(score.wickets) >= 5) {
            total = total + 16;
        } else if (Float.parseFloat(score.wickets) >= 4) {
            total = total + 8;
        }
        total = total + (Float.parseFloat(score.maiden) * 8);

        // Economy rate
        float overs = Float.parseFloat(score.overs);

        int deliveries = (((int) overs) * 6) + (int) (overs % (int) overs * 10);

        if (deliveries >= 12) {
            float economy = Float.parseFloat(score.economy);
            if (economy > 11) {
                total = total - 6;
            } else if (economy > 10) {
                total = total - 4;
            } else if (economy > 9) {
                total = total - 2;
            } else if (economy > 6) {
                total = total + 0;
            } else if (economy > 5) {
                total = total + 2;
            } else if (economy > 4) {
                total = total + 4;
            } else if (economy >= 0) {
                total = total + 6;
            }
        }

        return total;
    }

    private static float calculateFieldingPoints(Match.Fielding.Score score) {
        float total = 0;

        total = total + (score.catches * 8);
        total = total + (score.stumped * 12);

        return total;
    }

    private static List<String> getPlayerIdsOfTeam(String teamName) {
        List<String> playersId = new ArrayList<>();
        for (Player player : players) {
            if (player.team.equals(teamName)) {
                playersId.add(player.id);
            }
        }
        return playersId;
    }
}
