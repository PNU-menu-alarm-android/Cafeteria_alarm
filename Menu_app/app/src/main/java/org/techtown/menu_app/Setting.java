package org.techtown.menu_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    Button Home, add, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("username").trim();
        final String email = intent.getStringExtra("email").trim();

        Home = findViewById(R.id.homebutton);
        add = findViewById(R.id.addmenu);
        delete = findViewById(R.id.deletemenu);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, addMenu.class);
                intent.putExtra("username", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

    }
}