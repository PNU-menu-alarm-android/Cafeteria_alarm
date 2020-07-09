package org.techtown.menu_app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class addMenu extends AppCompatActivity {

    Button add;
    EditText name;
    String menu_name, email, username;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmenu);

        Intent intent = getIntent();
        email = intent.getStringExtra("email").trim();
        username = intent.getStringExtra("username").trim();

        add = findViewById(R.id.add);
        name = findViewById(R.id.name);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(true){
                    name = findViewById(R.id.name);
                    menu_name = name.getText().toString().trim();
                    try {
                        Food update_food = new Food(menu_name);
                        Map<String, Object> menu_updates = new Hashtable<>();
                        name = findViewById(R.id.name);
                        menu_name = name.getText().toString().trim();
                        menu_updates.put(menu_name, update_food);
                        firebaseReference.child("user").child(username).child("Food").updateChildren(menu_updates);
                    } catch (Exception e) {
                        Toast.makeText(addMenu.this, "메뉴를 추가하는 도중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        continue;
                    }
                    Toast.makeText(addMenu.this, menu_name+" 추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                }
                //((Setting)Setting.CONTEXT).onResume();
                finish();
            }
        });
    }
}