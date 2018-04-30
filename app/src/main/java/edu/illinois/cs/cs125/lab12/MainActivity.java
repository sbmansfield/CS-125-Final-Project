package edu.illinois.cs.cs125.lab12;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Restaurant[] restaurants = new Restaurant[3];

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
                                JSONObject[] results = new JSONObject[3];
                                for (int i = 0; i < results.length; i++) {
                                    results[i] = response.getJSONArray("businesses").getJSONObject(i);
                                    restaurants[i].setName(results[i].get("name").toString());
                                    JSONArray categories = results[i].getJSONArray("categories");
                                    restaurants[i].setFoodType(categories.getJSONObject(0).get("title").toString());
                                    restaurants[i].setRating(results[i].get("rating").toString());
                                    restaurants[i].setPrice(results[i].get("price").toString());

                                    JSONArray addressArray = results[i].getJSONObject("location").getJSONArray("display_address");
                                    restaurants[i].setStreet(addressArray.get(0).toString());
                                    restaurants[i].setCity(addressArray.get(1).toString());
                                    restaurants[i].setAddress();
                                    restaurants[i].setPhoneNumber(results[i].get("display_phone").toString());
                                }



                                //create the first restaurant info button
                                Button restaurant1Name = findViewById(R.id.restaurant1);
                                restaurant1Name.setVisibility(View.VISIBLE);
                                restaurant1Name.setText(restaurants[0].name);
                                restaurant1Name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Food Type: " + restaurants[0].foodType + "\n"
                                                + "Rating: " + restaurants[0].rating + "\n"
                                                + "Price: " + restaurants[0].price + "\n"
                                                + "Address: " + restaurants[0].address + "\n"
                                                + "Phone Number: " + restaurants[0].phoneNumber + "\n";

                                        info.setText(displayInfo);
                                    }
                                });

                                //create the first restaurant info button
                                Button restaurant2Name = findViewById(R.id.restaurant2);
                                restaurant2Name.setVisibility(View.VISIBLE);
                                restaurant2Name.setText(restaurants[1].name);
                                restaurant2Name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText2);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Food Type: " + restaurants[1].foodType + "\n"
                                                + "Rating: " + restaurants[1].rating + "\n"
                                                + "Price: " + restaurants[1].price + "\n"
                                                + "Address: " + restaurants[1].address + "\n"
                                                + "Phone Number: " + restaurants[1].phoneNumber + "\n";

                                        info.setText(displayInfo);
                                    }
                                });

                                //create the first restaurant info button
                                Button restaurant3Name = findViewById(R.id.restaurant3);
                                restaurant3Name.setVisibility(View.VISIBLE);
                                restaurant3Name.setText(restaurants[2].name);
                                restaurant3Name.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        Log.d(TAG, "Start API button clicked");
                                        TextView info = findViewById(R.id.editText);
                                        info.setVisibility(View.VISIBLE);
                                        String displayInfo = "Food Type: " + restaurants[2].foodType + "\n"
                                                + "Rating: " + restaurants[2].rating + "\n"
                                                + "Price: " + restaurants[2].price + "\n"
                                                + "Address: " + restaurants[2].address + "\n"
                                                + "Phone Number: " + restaurants[2].phoneNumber + "\n";

                                        info.setText(displayInfo);
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

class Restaurant {
    public String name, foodType, rating, price, street, city, address, phoneNumber;
    public Restaurant(){}
    public Restaurant(String setName) {
        name = setName;
    }

    public boolean setName(String setName) {
        if (setName == null) {
            return false;
        } else {
            name = setName;
            return true;
        }
    }

    public boolean setRating(String setRating) {
        if (setRating == null) {
            return false;
        } else {
            rating = setRating;
            return true;
        }
    }

    public boolean setFoodType(String setFoodType) {
        if (setFoodType == null) {
            return false;
        } else {
            foodType = setFoodType;
            return true;
        }
    }

    public boolean setPrice(String setPrice) {
        if (setPrice == null) {
            return false;
        } else {
            price = setPrice;
            return true;
        }
    }

    public boolean setStreet(String setStreet) {
        if (setStreet == null) {
            return false;
        } else {
            price = setStreet;
            return true;
        }
    }

    public boolean setCity(String setCity) {
        if (setCity == null) {
            return false;
        } else {
            price = setCity;
            return true;
        }
    }

    public boolean setAddress() {
        if (street == null || city == null) {
            return false;
        } else {
            address = street + ", " + city;
            return true;
        }
    }

    public boolean setPhoneNumber(String setPhoneNumber) {
        if (setPhoneNumber == null) {
            return false;
        } else {
            price = setPhoneNumber;
            return true;
        }
    }
}
