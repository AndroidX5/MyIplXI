package android.bignerd.mydream11;

import java.util.Comparator;

public class ScoreComparator implements Comparator<ScoreModel> {
    @Override
    public int compare(ScoreModel o1, ScoreModel o2) {
        if (Float.parseFloat(o1.getScore()) < Float.parseFloat(o2.getScore())) {
            return 1;
        } else if (Float.parseFloat(o1.getScore()) > Float.parseFloat(o2.getScore())) {
            return -1;
        }
        return 0;
    }
}
