package android.bignerd.mydream11;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends FragmentActivity {

    DateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a", Locale.getDefault());

    private RecyclerView recyclerView;
    private BoardAdapter adapter;
    private View leaderBoardContainer;
    private View fetchingData;
    private List<Match> allMatches = new ArrayList<>();
    private List<Player> allPlayers = new ArrayList<>();
    private TextView lastUpdated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshScore(((EditText) findViewById(R.id.match_id)).getText().toString());
            }
        });

        leaderBoardContainer = findViewById(R.id.leaderBoardContainer);
        fetchingData = findViewById(R.id.fetchingDataTextView);

        fetchAllData();

        adapter = new BoardAdapter(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ScoreModel model) {
                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                intent.putExtra("TEAM_NAME", model.getName());
                startActivity(intent);
            }
        });

        lastUpdated = findViewById(R.id.lastUpdated);
        recyclerView = findViewById(R.id.leaderBoardList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void fetchAllData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("matches");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("####", "Matches: " + allMatches);

                List<Match> matches = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    matches.add(match);
                }

                allMatches.clear();
                allMatches.addAll(matches);

                Log.d("####", "Matches: " + allMatches);

                DatabaseReference players = FirebaseDatabase.getInstance().getReference("players");

                // Attach a listener to read the data at our posts reference
                players.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("####", "Matches: " + allMatches);

                        List<Player> players = new ArrayList<>();

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Player player = postSnapshot.getValue(Player.class);
                            players.add(player);
                        }

                        allPlayers.clear();
                        allPlayers.addAll(players);

                        processDataAndShowLeaderBoard();

                        Log.d("####", "Matches: " + allPlayers);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("####", "DatabaseError: " + databaseError);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("####", "DatabaseError: " + databaseError);
            }
        });

    }

    private void processDataAndShowLeaderBoard() {
        DataProcessor processor = DataProcessor.getInstance(allMatches, allPlayers);

        List<ScoreModel> leaderBoardList = new ArrayList<>();

        Set<String> teamNames = new HashSet<>();
        for (Player player : allPlayers) {
            teamNames.add(player.team);
        }

        for (String team : teamNames) {
            float battingPoints = processor.getTotalBattingPoints(team);
            float bowlingPoints = processor.getTotalBowlingPoints(team);
            float fieldingPoints = processor.getTotalFieldingPoints(team);
            float total = battingPoints + bowlingPoints + fieldingPoints;

            leaderBoardList.add(
                    new ScoreModel(team, String.valueOf(battingPoints),
                            String.valueOf(bowlingPoints),
                            String.valueOf(fieldingPoints),
                            String.valueOf(total))
            );
        }
        Collections.sort(leaderBoardList, new ScoreComparator());

        adapter.setData(leaderBoardList);

        fetchingData.setVisibility(View.GONE);
        leaderBoardContainer.setVisibility(View.VISIBLE);

        Date date = new Date();
        date.setTime(processor.getLastUpdatedTime());

        lastUpdated.setText(getString(R.string.last_updated, simpleDateFormat.format(date)));
    }

    private void refreshScore(final String matchId) {
        if (TextUtils.isEmpty(matchId)) {
            return;
        }
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://cricapi.com/api/fantasySummary?apikey=JW51JHLFrRRk94uSt03BNEo7PFw2&unique_id=" + matchId;

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("####", "onResponse: " + response.get("data"));
                            Match match = new Gson().fromJson(response.get("data").toString(), Match.class);

                            List<String> activePlayerIds = new ArrayList<>();
                            for (Player player : allPlayers) {
                                if (player.isActive) {
                                    activePlayerIds.add(player.id);
                                }
                            }
                            match.setActivePlayers(activePlayerIds);
                            match.setUpdated(System.currentTimeMillis());

                            database.child("matches").child(matchId).setValue(match);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("####", "onErrorResponse: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
