package org.techtown.menu_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {


    ImageButton Menu, Setting;
    String name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        final TextView infoName = findViewById(R.id.name);

        Intent intent = getIntent();
        name = intent.getStringExtra("name").trim();
        email = intent.getStringExtra("email").trim();
        infoName.setText(name);

        Setting = findViewById(R.id.settingbutton);
        Menu = findViewById(R.id.allmenubutton);

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Setting.class);
                intent.putExtra("username", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Menu.class);
                intent.putExtra("username", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }
}
