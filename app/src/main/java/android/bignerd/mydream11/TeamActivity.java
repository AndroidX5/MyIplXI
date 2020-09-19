package android.bignerd.mydream11;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BoardAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_activity);

        recyclerView = findViewById(R.id.teamList);
        adapter = new BoardAdapter(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ScoreModel model) {
                // TODO
            }
        });

        List<ScoreModel> list = getLeadersData();
        Collections.sort(list, new ScoreComparator());

        adapter.setData(list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @NonNull
    private List<ScoreModel> getLeadersData() {
        List<ScoreModel> list = new ArrayList<>();
        list.add(new ScoreModel("Warner", 300));
        list.add(new ScoreModel("Sachin", 126));
        list.add(new ScoreModel("Rohit", 333));
        list.add(new ScoreModel("Kohli", 100));
        list.add(new ScoreModel("MSD", 100));
        list.add(new ScoreModel("Raina", 55));
        list.add(new ScoreModel("Dravid", 23));
        list.add(new ScoreModel("KL", 545));
        list.add(new ScoreModel("Gayle", 22));
        list.add(new ScoreModel("Russel", 54));
        list.add(new ScoreModel("Narine", 111));
        list.add(new ScoreModel("Umesh", 345));
        list.add(new ScoreModel("Pandya", 553));
        list.add(new ScoreModel("Rahane", 253));
        list.add(new ScoreModel("Ashwin", 643));
        return list;
    }
}
