package android.bignerd.mydream11;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataProcessor {

    private static List<Match> matches;
    private static List<Player> players;

    private static DataProcessor INSTANCE = null;
    private DataProcessor(List<Match> matches, List<Player> players) {
        DataProcessor.matches = matches;
        DataProcessor.players = players;
    }

    public static DataProcessor getInstance(List<Match> matches, List<Player> players) {
        if(INSTANCE == null) {
            INSTANCE = new DataProcessor(matches, players);
        }
        return INSTANCE;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static List<Match> getMatches() {
        return matches;
    }

    public float getTotalPoints(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (String playerId: playersIds) {
            total = getPlayerPoints(total, playerId);
        }
        return total;
    }

    public static float getPlayerPoints(float total, String playersIds) {
        for (Match match : matches) {
            for (Match.Batting batting : match.batting) {
                for (Match.Batting.Score score : batting.scores) {
                    if (playersIds.equals(score.pid)) {
                        total = total + calculateBattingPoints(score);
                    }
                }
            }

            for (Match.Bowling bowling : match.bowling) {
                for (Match.Bowling.Score score : bowling.scores) {
                    if (playersIds.equals(score.pid)) {
                        total = total + calculateBowlingPoints(score);
                    }
                }
            }
            if (match.fielding != null) {
                for (Match.Fielding fielding : match.fielding) {
                    for (Match.Fielding.Score score : fielding.scores) {
                        if (playersIds.equals(score.pid)) {
                            total = total + calculateFieldingPoints(score);
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
        if (score.runs == 0) {
            total = total - 2;
        }

        // Strike rate
        Player player = null;
        for (Player player1 : players) {
            if (score.pid.equals(player1.id)) {
                player = player1;
            }
        }
        if (player != null && !player.isBowler) {
            float strikeRate = score.sr;
            if (strikeRate < 50) {
                total = total - 6;
            } else if (strikeRate < 60) {
                total = total - 4;
            } else if (strikeRate < 70) {
                total = total - 2;
            }
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

        int deliveries = (((int) overs) * 6) + (int) (overs % (int) overs * 10);;
        if (deliveries >= 12) {
            float economy = Float.parseFloat(score.economy);
            if (economy > 11) {
                total = total - 6;
            } else if (economy > 10) {
                total = total - 4;
            } else if (economy > 9) {
                total = total - 2;
            } else if (economy > 5 && economy < 6) {
                total = total + 2;
            } else if (economy > 4) {
                total = total + 4;
            } else {
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

    private List<String> getPlayerIdsOfTeam(String teamName) {
        List<String> playersId = new ArrayList<>();
        for (Player player : players) {
            if (player.team.equals(teamName)) {
                playersId.add(player.id);
            }
        }
        return playersId;
    }
}
