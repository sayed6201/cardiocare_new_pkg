package com.daffodil.cardiocare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DSLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_company);
    }
    public void clickerWeb(View v){
        Intent i = new Intent(DSLActivity.this,WebActivity.class);
        i.putExtra("url","http://daffodilsoft.com/");
        startActivity(i);
    }
}
