package android.bignerd.mydream11;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_activity);

        String teamName = getIntent().getStringExtra("TEAM_NAME");
        ((TextView)findViewById(R.id.title)).setText(teamName);
        RecyclerView recyclerView = findViewById(R.id.teamList);
        // TODO
        BoardAdapter adapter = new BoardAdapter(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ScoreModel model) {
                // TODO
            }
        });

        List<ScoreModel> list = getLeadersData(teamName);
        Collections.sort(list, new ScoreComparator());

        adapter.setData(list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @NonNull
    private List<ScoreModel> getLeadersData(String teamName) {
        List<ScoreModel> list = new ArrayList<>();
        List<Player> players = DataProcessor.getPlayers();

        for (Player player: players) {
            if (player.team.equals(teamName)) {
                list.add(new ScoreModel(player.name, DataProcessor.getPlayerPoints(0, player.id)));
            }
        }
        return list;
    }
}
