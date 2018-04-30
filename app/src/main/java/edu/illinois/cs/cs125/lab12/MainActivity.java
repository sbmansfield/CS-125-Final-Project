package edu.illinois.cs.cs125.lab12;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "MP7:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;
    public TextView restaurantInfo;
    public String queryInfo;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);

        restaurantInfo = findViewById(R.id.textView);
        restaurantInfo.setText("Click the search button to search for a nearby restaurant in Champaign!");

        final SearchView searchView = findViewById(R.id.searchText);

        // Attach the handler to our UI button
        final Button startAPICall = findViewById(R.id.search_restaurant);
        startAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                queryInfo = searchView.getQuery().toString().replaceAll(" ", "+");
                Log.d(TAG, "Start API button clicked");
                startAPICall();
            }
        });
    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the Yelp API.
     */
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.yelp.com/v3/businesses/search?term=" + queryInfo + "&location=Champaign,61820",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                JSONObject result = response.getJSONArray("businesses").getJSONObject(0);
                                String name = result.get("name").toString();

                                JSONArray categories = result.getJSONArray("categories");
                                final String foodType = categories.getJSONObject(0).get("title").toString();

                                final String rating = result.get("rating").toString();

                                final String price = result.get("price").toString();

                                final JSONArray addressArray = result.getJSONObject("location").getJSONArray("display_address");
                                final String street = addressArray.get(0).toString();
                                final String city = addressArray.get(1).toString();
                                final String address = street + ", " + city;

                                final String phoneNumber = result.get("display_phone").toString();

                                final String url = result.get("image_url").toString();

                                //create the first restaurant info button
                                Button restaurantName = findViewById(R.id.restaurant1);
                                restaurantName.setVisibility(View.VISIBLE);
                                restaurantName.setText(name);
                                restaurantName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Food Type: " + foodType + "\n"
                                                + "Rating: " + rating + "\n"
                                                + "Price: " + price + "\n"
                                                + "Address: " + address + "\n"
                                                + "Phone Number: " + phoneNumber + "\n";

                                        info.setText(displayInfo);

                                        ImageView image = findViewById(R.id.imageView);
                                        image.setVisibility(View.VISIBLE);
                                        try {
                                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
                                            image.setImageBitmap(bitmap);
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + BuildConfig.API_KEY);
                            Log.d(TAG, params.toString());
                            return params;
                        }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
