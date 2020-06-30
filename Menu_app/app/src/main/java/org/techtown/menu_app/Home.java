package org.techtown.menu_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        TextView infoName = findViewById(R.id.name);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        infoName.setText(name);
    }
}
