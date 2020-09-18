package android.bignerd.mydream11;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {

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
