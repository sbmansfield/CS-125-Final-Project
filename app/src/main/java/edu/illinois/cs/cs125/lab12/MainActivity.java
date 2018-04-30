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
        //restaurantInfo.setText("Click the search button to search for a nearby restaurant in Champaign!");

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

                                restaurantInfo.setText("Click the buttons below to display more information on each restaurant: ");

                                //create the first restaurant info button
                                JSONObject result = response.getJSONArray("businesses").getJSONObject(0);
                                final String[] information = getRestaurantInfo(result);

                                Button restaurant1 = findViewById(R.id.restaurant1);
                                restaurant1.setVisibility(View.VISIBLE);
                                restaurant1.setText(information[0]);
                                restaurant1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Type: " + information[1] + "\n"
                                                + "Rating: " + information[2] + "\n"
                                                + "Price: " + information[3] + "\n"
                                                + "Address: " + information[4] + "\n"
                                                + "Phone Number: " + information[5];

                                        info.setText(displayInfo);

                                        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                                                .execute(information[6]);
                                    }
                                });

                                //create the second restaurant info button
                                JSONObject result2 = response.getJSONArray("businesses").getJSONObject(1);
                                final String[] information2 = getRestaurantInfo(result2);

                                Button restaurant2 = findViewById(R.id.restaurant2);
                                restaurant2.setVisibility(View.VISIBLE);
                                restaurant2.setText(information2[0]);
                                restaurant2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Type: " + information2[1] + "\n"
                                                + "Rating: " + information2[2] + "\n"
                                                + "Price: " + information2[3] + "\n"
                                                + "Address: " + information2[4] + "\n"
                                                + "Phone Number: " + information2[5];

                                        info.setText(displayInfo);

                                        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                                                .execute(information2[6]);
                                    }
                                });

                                //create the third restaurant info button
                                JSONObject result3 = response.getJSONArray("businesses").getJSONObject(2);
                                final String[] information3 = getRestaurantInfo(result3);

                                Button restaurant3 = findViewById(R.id.restaurant3);
                                restaurant3.setVisibility(View.VISIBLE);
                                restaurant3.setText(information3[0]);
                                restaurant3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Type: " + information3[1] + "\n"
                                                + "Rating: " + information3[2] + "\n"
                                                + "Price: " + information3[3] + "\n"
                                                + "Address: " + information3[4] + "\n"
                                                + "Phone Number: " + information3[5];

                                        info.setText(displayInfo);

                                        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                                                .execute(information3[6]);
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

    String[] getRestaurantInfo(JSONObject result) {
        String[] informationArray = new String[7];
        try {
            Log.d(TAG, result.toString(2));
            //JSONObject result = response.getJSONArray("businesses").getJSONObject(0);
            String name = result.get("name").toString();
            informationArray[0] = name;

            JSONArray categories = result.getJSONArray("categories");
            String foodType = categories.getJSONObject(0).get("title").toString();
            informationArray[1] = foodType;

            String rating = result.get("rating").toString();
            informationArray[2] = rating;

            String price = result.get("price").toString();
            informationArray[3] = price;

            JSONArray addressArray = result.getJSONObject("location").getJSONArray("display_address");
            String street = addressArray.get(0).toString();
            String city = addressArray.get(1).toString();
            String address = street + ", " + city;
            informationArray[4] = address;

            String phoneNumber = result.get("display_phone").toString();
            informationArray[5] = phoneNumber;

            String url = result.get("image_url").toString();
            informationArray[6] = url;

        } catch (JSONException ignored) { }

    return informationArray;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
