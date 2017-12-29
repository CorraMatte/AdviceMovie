package com.example.corra.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private String info = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtInfo = (TextView) findViewById(R.id.txtAbout);
        txtInfo.setText(getString(R.string.about_info));
    }
}
