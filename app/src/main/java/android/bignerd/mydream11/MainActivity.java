package android.bignerd.mydream11;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private RecyclerView recyclerView;
    private BoardAdapter adapter;
    private View leaderBoardContainer;
    private View fetchingData;
    private List<Match> allMatches = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshScore(((EditText)findViewById(R.id.match_id)).getText().toString());
            }
        });

        recyclerView = findViewById(R.id.leaderBoardList);
        leaderBoardContainer = findViewById(R.id.leaderBoardContainer);
        fetchingData = findViewById(R.id.fetchingDataTextView);

        fetchAllData();

        adapter = new BoardAdapter(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(ScoreModel model) {
                startActivity(new Intent(MainActivity.this, TeamActivity.class));
            }
        });

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

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    matches.add(match);
                }

                allMatches.clear();
                allMatches.addAll(matches);

                processDataAndShowLeaderBoard();

                Log.d("####", "Matches: " + allMatches);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("####", "DatabaseError: " + databaseError);
            }
        });
    }

    private void processDataAndShowLeaderBoard() {
        DataProcessor processor = new DataProcessor(allMatches, this);

        List<ScoreModel> leaderBoardList = new ArrayList<>();
        leaderBoardList.add(new ScoreModel(getString(R.string.team_amar), processor.getTotalPoints(getString(R.string.team_amar))));
        leaderBoardList.add(new ScoreModel(getString(R.string.team_sachin), processor.getTotalPoints(getString(R.string.team_sachin))));
        leaderBoardList.add(new ScoreModel(getString(R.string.team_yogesh), processor.getTotalPoints(getString(R.string.team_yogesh))));
        leaderBoardList.add(new ScoreModel(getString(R.string.team_omkar), processor.getTotalPoints(getString(R.string.team_omkar))));
        leaderBoardList.add(new ScoreModel(getString(R.string.team_mahesh), processor.getTotalPoints(getString(R.string.team_mahesh))));
        leaderBoardList.add(new ScoreModel(getString(R.string.team_bhushan), processor.getTotalPoints(getString(R.string.team_bhushan))));

        Collections.sort(leaderBoardList, new ScoreComparator());

        adapter.setData(leaderBoardList);

        fetchingData.setVisibility(View.GONE);
        leaderBoardContainer.setVisibility(View.VISIBLE);
    }

    private void refreshScore(final String matchId) {
        if (TextUtils.isEmpty(matchId)) {
            return;
        }
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://cricapi.com/api/fantasySummary?apikey=JW51JHLFrRRk94uSt03BNEo7PFw2&unique_id="+matchId;

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("####", "onResponse: " + response.get("data"));
                            Match match = new Gson().fromJson(response.get("data").toString(), Match.class);
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
