package com.fci.crescendo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class result_back extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataview);
        MainActivity main =new MainActivity();
        main.get("api url");
    }
}
