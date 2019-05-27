package com.daffodil.cardiocare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
    }

    public void packageClicker(View v){
        if(v.getId() == R.id.sp_package){
            String url = "http://www.cardiocarebd.com/index.php/packages/specialized-package";

            Intent i = new Intent(PackageActivity.this,WebActivity.class);
            i.putExtra("url",url);
            startActivity(i);
        }else if(v.getId() == R.id.ex_package){
            String url = "http://www.cardiocarebd.com/index.php/packages/executive-health-check-up";

            Intent i = new Intent(PackageActivity.this,WebActivity.class);
            i.putExtra("url",url);
            startActivity(i);
        }else if(v.getId() == R.id.gn_package){
            String url = "http://www.cardiocarebd.com/index.php/packages/general-health-check-up";

            Intent i = new Intent(PackageActivity.this,WebActivity.class);
            i.putExtra("url",url);
            startActivity(i);
        }
    }
}
