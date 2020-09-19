package android.bignerd.mydream11;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Match {
    @SerializedName("team")
    public List<Team> team;
    @SerializedName("batting")
    public List<Batting> batting;
    @SerializedName("bowling")
    public List<Bowling> bowling;
    @SerializedName("fielding")
    public List<Fielding> fielding;
    public long updated;
    public List<String> activePlayers;

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setActivePlayers(List<String> players) {
        this.activePlayers = players;
    }

    public static class Team {
        @SerializedName("name")
        public String name;
        @SerializedName("players")
        public List<Player> players;

        static class Player {
            @SerializedName("pid")
            public String pid;
            @SerializedName("name")
            public String name;
        }
    }

    public static class Batting {
        @SerializedName("scores")
        public List<Score> scores;
        static class Score {
            @SerializedName("pid")
            public String pid;
            @SerializedName("batsman")
            public String name;
            @SerializedName("R")
            public int runs;
            @SerializedName("B")
            public int balls;
            @SerializedName("4s")
            public int fours;
            @SerializedName("6s")
            public int sixes;
            @SerializedName("SR")
            public int sr;
        }
    }


    public static class Bowling {
        @SerializedName("scores")
        public List<Score> scores;
        static class Score {
            @SerializedName("pid")
            public String pid;
            @SerializedName("bowler")
            public String name;
            @SerializedName("O")
            public String overs;
            @SerializedName("M")
            public String maiden;
            @SerializedName("R")
            public String runs;
            @SerializedName("W")
            public String wickets;
            @SerializedName("Econ")
            public String economy;
        }
    }

    public static class Fielding {
        @SerializedName("scores")
        public List<Score> scores;
        static class Score {
            @SerializedName("pid")
            public String pid;
            @SerializedName("name")
            public String name;
            @SerializedName("catch")
            public int catches;
            @SerializedName("stumped")
            public int stumped;
        }
    }
}
