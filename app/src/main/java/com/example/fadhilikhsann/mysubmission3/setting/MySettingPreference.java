package com.example.fadhilikhsann.mysubmission3.setting;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fadhilikhsann.mysubmission3.R;

public class MySettingPreference extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting_preference);
        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new MyPreferenceFragment()).commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.setting);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
