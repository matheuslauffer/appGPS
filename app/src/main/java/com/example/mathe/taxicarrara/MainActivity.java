package com.example.mathe.taxicarrara;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button config;
    EditText from;
    EditText to;
    Bundle bundle;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doInitControls();
    }

    @Override
    protected void onResume(){
        super.onResume();
        doInitControls();
    }

    public void doInitControls(){
        button = (Button) findViewById(R.id.routeCalc);
        from = (EditText) findViewById(R.id.from);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    requisicaoVolley();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to = (EditText) findViewById(R.id.to);
                bundle = new Bundle();
                intent = new Intent(MainActivity.this, RouteCalc.class);
                bundle.putString("from", from.getText().toString());
                bundle.putString("to", to.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        config = (Button) findViewById(R.id.conf);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Configurations.class);
                startActivity(intent);
            }
        });
    }

    public void  requisicaoVolley(){
        RequestQueue mRequestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final String url = "https://www.mapquestapi.com/geocoding/v1/reverse?key=pdmZhMsC7TbNkMXcJLhSSuVdJg9raiqN&location="+URLEncoder.encode(String.valueOf(location.getLatitude()))+"%2C"+URLEncoder.encode(String.valueOf(location.getLongitude()))+"&outFormat=json&thumbMaps=false";
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonzao = response.getJSONArray("results");
                            JSONArray jsonzinho =  jsonzao.getJSONObject(0).getJSONArray("locations");
                            String rua = jsonzinho.getJSONObject(0).getString("street");
                            String cidade = jsonzinho.getJSONObject(0).getString("adminArea5");
                            String estado = jsonzinho.getJSONObject(0).getString("adminArea3");
                            from.setText(rua+", "+cidade+", "+estado);
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
