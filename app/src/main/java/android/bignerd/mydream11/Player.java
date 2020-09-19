package android.bignerd.mydream11;

public class Player {
    public String id;
    public String name;
    public String team;
    public boolean isActive;
    public boolean isBowler;
    public int[] matches;

    public Player() {
    }

    public Player(String id, String name, String teamName, boolean isActive, boolean isBowler) {
        this.id = id;
        this.name = name;
        this.team = teamName;
        this.isActive = isActive;
        this.isBowler = isBowler;
    }
}
