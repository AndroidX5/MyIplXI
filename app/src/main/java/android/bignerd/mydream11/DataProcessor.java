package android.bignerd.mydream11;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataProcessor {

    private List<Match> matches;
    private Context context;

    DataProcessor(List<Match> matches, Context context) {
        this.matches = matches;
        this.context = context;
    }

    public float getTotalPoints(String teamName) {
        float total = 0f;
        List<String> playersIds = getPlayerIdsOfTeam(teamName);
        for (Match match : matches) {
            for (Match.Batting batting : match.batting) {
                for (Match.Batting.Score score : batting.scores) {
                    if (playersIds.contains(score.pid)) {
                        total = total + calculateBattingPoints(score);
                    }
                }
            }
            for (Match.Bowling bowling : match.bowling) {
                for (Match.Bowling.Score score : bowling.scores) {
                    if (playersIds.contains(score.pid)) {
                        total = total + calculateBowlingPoints(score);
                    }
                }
            }
            for (Match.Fielding fielding : match.fielding) {
                for (Match.Fielding.Score score : fielding.scores) {
                    if (playersIds.contains(score.pid)) {
                        total = total + calculateFieldingPoints(score);
                    }
                }
            }
        }
        return total;
    }

    private float calculateBattingPoints(Match.Batting.Score score) {
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
        if (PlayerUtil.getPlayerRole(score.pid) != PlayerRolesEnum.UNKNOWN &&
                PlayerUtil.getPlayerRole(score.pid) != PlayerRolesEnum.BOWLER) {
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

    private float calculateBowlingPoints(Match.Bowling.Score score) {
        float total = 0;

        total = total + (Float.parseFloat(score.wickets) * 25);
        if (Float.parseFloat(score.wickets) >= 5) {
            total = total + 16;
        } else if (Float.parseFloat(score.wickets) >= 4) {
            total = total + 8;
        }
        total = total + (Float.parseFloat(score.maiden) * 8);

        // Economy rate
        if (Integer.parseInt(score.overs) >= 2) {
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

    private float calculateFieldingPoints(Match.Fielding.Score score) {
        float total = 0;

        total = total + (score.catches * 8);
        total = total + (score.stumped * 12);

        return total;
    }

    private List<String> getPlayerIdsOfTeam(String teamName) {
        if (context.getString(R.string.team_sachin) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForSachin();
        }
        if (context.getString(R.string.team_mahesh) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForMahesh();
        }
        if (context.getString(R.string.team_yogesh) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForYogesh();
        }
        if (context.getString(R.string.team_omkar) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForOmkar();
        }
        if (context.getString(R.string.team_bhushan) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForBhushan();
        }
        if (context.getString(R.string.team_amar) .equalsIgnoreCase(teamName)) {
            return getPlayerIdsForAmar();
        }
        return Collections.emptyList();
    }

    private List<String> getPlayerIdsForSachin() {
        List<String> ids = new ArrayList<>();
        ids.add("5334"); // "Aron Finch (7.5)"
        ids.add("25913"); // "Mohammad Nabi (1)"
        ids.add("219889"); // "David Warner (22)"
        ids.add("277906"); // "Kane Williamson (1.5)"
        ids.add("290716"); // "Kedar Jadhav (2)"
        ids.add("326017"); // "Siddharth Kaul (2)"
        ids.add("422108"); // "KL Rahul (17.5)"
        ids.add("447261"); // "Deepak Chahar (6.5)"
        ids.add("604527"); // "Nitish Rana (13)"
        ids.add("719715"); // "Washington Sundar (1)"
        ids.add("793463"); // "Rashid Khan (10)"
        ids.add("1070168"); // "Prithwi Shaw (5.5)"
        ids.add("1070180"); // "Priyam Garg (1)"
        ids.add("1151278"); // "Yashsvi Jaiswal (1)"
        ids.add(""); // "Krunal Pandya (8)"
        return ids;
    }

    private List<String> getPlayerIdsForYogesh() {
        List<String> ids = new ArrayList<>();
        ids.add("30045"); // "Dinesh Karthik (1)"
        ids.add("34102"); // "Rohit Sharma (22.5)"
        ids.add("44828"); // "Faf du Plessis (1)"
        ids.add("253802"); // "Virat Kohli (23.5)"
        ids.add("267192"); // "Steve Smith (7)"
        ids.add("311158"); // "Ben Stokes (5)"
        ids.add("355269"); // "Jimmy Neesham (1)"
        ids.add("376116"); // "Umesh Yadav (1)"
        ids.add("459508"); // "Andrew Tye (1)"
        ids.add("489889"); // "Pat Cummins (6.5)"
        ids.add("493773"); // "Lockie Ferguson (1)"
        ids.add("532856"); // "Manan Vohra (1)"
        ids.add("669855"); // "Jofra Archer (8.5)"
        ids.add("677081"); // "Keemo Paul (1)"
        ids.add("1081442"); // "Mayank Markande (1)"
        return ids;
    }

    private List<String> getPlayerIdsForOmkar() {
        List<String> ids = new ArrayList<>();
        ids.add("8917"); // "Moeen Ali (1.5)"
        ids.add("28081"); // "MS Dhoni (11)"
        ids.add("33335"); // "Suresh Raina (2)"
        ids.add("40618"); // "Imran Tahir (4)"
        ids.add("276298"); // "Andre Russell (21.5)"
        ids.add("321777"); // "David Miller (2)"
        ids.add("325026"); // "Glenn Maxwell (5.5)"
        ids.add("326016"); // "Bhuvaneshwar Kumar (3)"
        ids.add("344580"); // "Shreyas Gopal (4.5)"
        ids.add("390481"); // "Harshal Patel (1)"
        ids.add("430246"); // "Yuzvendra Chahal (10.5)"
        ids.add("446507"); // "Suryakumar Yadav (11)"
        ids.add("720471"); // "Ishan Kishan (5)"
        ids.add("931581"); // "Rishabh Pant (11)"
        ids.add("1064812"); // "Rahul Chahar (5)"
        return ids;
    }

    private List<String> getPlayerIdsForBhushan() {
        List<String> ids = new ArrayList<>();
        ids.add("24598"); // "Eoin Morgan (6.5)"
        ids.add("26421"); // "R. Ashwin (2.5)"
        ids.add("32966"); // "Piyush Chawla (1.5)"
        ids.add("35582"); // "Robin Uthappa (3)"
        ids.add("51880"); // "Chris Gayle (5)"
        ids.add("230558"); // "Sunil Narine (9)"
        ids.add("234675"); // "Jadeja (14)"
        ids.add("277916"); // "Ajinkya Rahane (2.5)"
        ids.add("290630"); // "Manish Pandey (9)"
        ids.add("308967"); // "Jos Buttler (16)"
        ids.add("438362"); // "Sandeep Sharma (1)"
        ids.add("439952"); // "Chris Morris (3)"
        ids.add("559235"); // "Kuldeep Yadav (6.5)"
        ids.add("604302"); // "Nicholas Pooran (3)"
        ids.add("1204136"); // "Shubhman Gill (10)"
        return ids;
    }

    private List<String> getPlayerIdsForMahesh() {
        List<String> ids = new ArrayList<>();
        ids.add("28235"); // "Shikhar Dhawan (8)"
        ids.add("33141"); // "Ambati Rayudu (8)"
        ids.add("51439"); // "DJ Bravo (5)"
        ids.add("277912"); // "Trent Boult (5)"
        ids.add("297433"); // "Jonny Bairstow (19.5)"
        ids.add("326637"); // "Chris Lynn (3)"
        ids.add("379143"); // "Quinton de Kock (8)"
        ids.add("390484"); // "Jaydev Unadkat (1)"
        ids.add("398438"); // "Mayank Agarwal (3.5)"
        ids.add("481896"); // "Mohammad Shami (7)"
        ids.add("495551"); // "Sheldon Cottrell (1.5)"
        ids.add("554691"); // "Axar Patel (3)"
        ids.add("625383"); // "Jasprit Bumrah (12.5)"
        ids.add("642519"); // "Shreyash Iyer (10)"
        ids.add("974109"); // "Mujeeb Ur Rahman (6)"
        return ids;
    }

    private List<String> getPlayerIdsForAmar() {
        List<String> ids = new ArrayList<>();
        ids.add("8180"); // "Shane Watson (10)"
        ids.add("28222"); // "Shivam Dube (1)"
        ids.add("32242"); // "Parthiv Patel (1)"
        ids.add("44936"); // "AB de Villiers (19)"
        ids.add("47492"); // "Dale Steyn (1)"
        ids.add("230559"); // "Kieron Pollard (10)"
        ids.add("424377"); // "Krishnappa Gowtham (5)"
        ids.add("425943"); // "Sanju Samson (8.5)"
        ids.add("446763"); // "Rahul Tripathi (4)"
        ids.add("471342"); // "Hardik Pandya (18)"
        ids.add("550215"); // "Kagiso Rabada (8)"
        ids.add("700167"); // "Navdeep Saini (1)"
        ids.add("942645"); // "Khaleel Ahmed (1)"
        ids.add("1079434"); // "Riyan Parag (1)"
        ids.add("1175441"); // "Ravi Bishnoi (1)"
        return ids;
    }
}
