package com.example.mathe.taxicarrara;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class Configurations extends AppCompatActivity {

    private static final String NOME_PREFS = "settings";
    EditText etConsume;
    EditText etPrice;
    EditText etLucro;
    Button saveData;
    Float consume;
    Float price;
    Float lucro;
    String strConsume;
    String strPrice;
    String strLucro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
        etConsume = (EditText) findViewById(R.id.kmL);
        etPrice = (EditText) findViewById(R.id.price);
        etLucro = (EditText) findViewById(R.id.lucro);
        SharedPreferences settings = getSharedPreferences(NOME_PREFS,0);
        consume = settings.getFloat("consume", 0);
        price = settings.getFloat("price", 0);
        lucro = settings.getFloat("lucro", 0);
        etConsume.setText(String.format("%.2f", consume));
        etPrice.setText(String.format("%.2f", price));
        etLucro.setText(String.format("%.2f", lucro));
        saveData = (Button) findViewById(R.id.saveData);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strConsume = etConsume.getText().toString();
                strPrice = etPrice.getText().toString();
                strLucro = etLucro.getText().toString();
                strConsume = strConsume.replace(",",".");
                strPrice = strPrice.replace(",",".");
                strLucro = strLucro.replace(",",".");
                consume = Float.parseFloat(strConsume);
                price = Float.parseFloat(strPrice);
                lucro = Float.parseFloat(strLucro);
                SharedPreferences prefs = getSharedPreferences(NOME_PREFS,0);
                SharedPreferences.Editor editor = (prefs.edit());
                editor.putFloat("consume", consume);
                editor.putFloat("price", price);
                editor.putFloat("lucro", lucro);
                editor.commit();
            }
        });
    }
}
