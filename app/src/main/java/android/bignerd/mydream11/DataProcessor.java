package android.bignerd.mydream11;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

    private List<Match> matches = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private static DataProcessor INSTANCE = null;

    private DataProcessor() {
    }

    static DataProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataProcessor();
        }
        return INSTANCE;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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
        for (Match match: matches) {
            for (Player player: match.activePlayers) {
                if (player.team.equalsIgnoreCase(teamName)) {
                    total = total + getBattingPointsPlayer(match, player.id);
                }
            }
        }
        return total;
    }

    float getTotalBowlingPoints(String teamName) {
        float total = 0f;
        for (Match match: matches) {
            for (Player player: match.activePlayers) {
                if (player.team.equalsIgnoreCase(teamName)) {
                    total = total + getBowlingPointsPlayer(match, player.id);
                }
            }
        }
        return total;
    }

    float getTotalFieldingPoints(String teamName) {
        float total = 0f;
        for (Match match: matches) {
            for (Player player: match.activePlayers) {
                if (player.team.equalsIgnoreCase(teamName)) {
                    total = total + getFieldingPointsPlayer(match, player.id);
                }
            }
        }
        return total;
    }

    float getTotalPlaying11Points(String teamName) {
        float total = 0f;
        for (Match match: matches) {
            for (Player player: match.activePlayers) {
                if (player.team.equalsIgnoreCase(teamName)) {
                    total = total + getPlaying11PointsPlayer(match, player.id);
                }
            }
        }
        return total;
    }

    float getBattingPointsPlayer(Match match, String playerId) {
        float total = 0f;
        if (match.batting != null) {
            for (Match.Batting batting : match.batting) {
                for (Match.Batting.Score score : batting.scores) {
                    if (playerId.equals(score.pid) && isPlayerActiveForTheMatch(match, score.pid)) {
                        total = total + calculateBattingPoints(match, score);
                    }
                }
            }
        }
        return total;
    }

    float getBowlingPointsPlayer(Match match, String playerId) {
        float total = 0f;
        if (match.bowling != null) {
            for (Match.Bowling bowling : match.bowling) {
                for (Match.Bowling.Score score : bowling.scores) {
                    if (playerId.equals(score.pid) && isPlayerActiveForTheMatch(match, score.pid)) {
                        total = total + calculateBowlingPoints(score);
                    }
                }
            }
        }
        return total;
    }

    float getFieldingPointsPlayer(Match match, String playerId) {
        float total = 0f;
        if (match.fielding != null) {
            for (Match.Fielding fielding : match.fielding) {
                for (Match.Fielding.Score score : fielding.scores) {
                    if (playerId.equals(score.pid) && isPlayerActiveForTheMatch(match, score.pid)) {
                        total = total + calculateFieldingPoints(score);
                    }
                }
            }
        }
        return total;
    }

    float getPlaying11PointsPlayer(Match match, String playerId) {
        float total = 0f;
        if (match.team != null) {
            for (Match.Team team : match.team) {
                for (Match.Team.Player player : team.players) {
                    if (playerId.equals(player.pid) && isPlayerActiveForTheMatch(match, player.pid)) {
                        total = total + 4;
                    }
                }
            }
        }
        return total;
    }

    private static boolean isPlayerActiveForTheMatch(Match match, String playerId) {
        for (Player player: match.activePlayers) {
            if (player.id.equals(playerId)) {
                return true;
            }
        }

        return false;
    }

    private static float calculateBattingPoints(Match match, Match.Batting.Score score) {
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
        for (Player player1 : match.activePlayers) {
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
        if (score.runs == 0 && player != null && !player.isBowler
                && !(score.dismissalInfo!= null && (score.dismissalInfo.equalsIgnoreCase("not out") || score.dismissalInfo.isEmpty()))) {
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
            } else if (economy >= 9) {
                total = total - 2;
            } else if (economy > 6) {
                total = total + 0;
            } else if (economy >= 5) {
                total = total + 2;
            } else if (economy >= 4) {
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
}
