package com.example.mathe.taxicarrara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class RouteCalc extends AppCompatActivity {

    private static final String NOME_PREFS = "settings";
    Bundle bundle;
    TextView tvDistance;
    TextView tvPrice;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_calc);

        intent = getIntent();
        bundle = intent.getExtras();

        tvDistance = (TextView) findViewById(R.id.distance);
        tvPrice = (TextView) findViewById(R.id.price);

        requisicaoVolley(bundle);
    }

    private void calculaPreco(float distance){
        distance = distance * (float) 1.60934;
        SharedPreferences settings = getSharedPreferences(NOME_PREFS,0);
        float consume = settings.getFloat("consume",0);
        float price = settings.getFloat("price", 0);
        float lucro = settings.getFloat("lucro", 0);
        float total;
        total = ((distance / consume) * price) *  (1 + (lucro/100));
        tvDistance.setText(String.format("%.2f", distance) + " Km");
        tvPrice.setText("R$ " + String.format("%.2f",total));
    }

    private void requisicaoVolley(Bundle bundle){
        RequestQueue mRequestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

        final String url = "https://www.mapquestapi.com/directions/v2/route?key=pdmZhMsC7TbNkMXcJLhSSuVdJg9raiqN&from="+ URLEncoder.encode(bundle.getString("from"))+"&to="+URLEncoder.encode(bundle.getString("to"))+"&outFormat=json&ambiguities=ignore&routeType=fastest&doReverseGeocode=false&enhancedNarrative=false&avoidTimedConditions=false";
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response){
                        try {
                            calculaPreco(Float.parseFloat(response.getJSONObject("route").getString("distance")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Erro volley", error.toString());
                    }
                }
        );
        mRequestQueue.add(request);
    }
}
