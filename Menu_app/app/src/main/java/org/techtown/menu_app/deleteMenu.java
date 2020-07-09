package org.techtown.menu_app;

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

public class deleteMenu extends AppCompatActivity {

    Button delete;
    EditText name;
    String menu_name, email, username;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletemenu);

        Intent intent = getIntent();
        email = intent.getStringExtra("email").trim();
        username = intent.getStringExtra("username").trim();

        delete = findViewById(R.id.delete);
        name = findViewById(R.id.name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while(true){
                    name = findViewById(R.id.name);
                    menu_name = name.getText().toString().trim();
                    try {
                        if(firebaseReference.child("user").child(username).child("Food").child(menu_name) == null) {
                            Toast.makeText(deleteMenu.this, "해당 메뉴가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            name.setText("");
                            continue;
                        }
                        else
                            firebaseReference.child("user").child(username).child("Food").child(menu_name).removeValue();
                    } catch (Exception e) {
                        Toast.makeText(deleteMenu.this, "해당 메뉴가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        continue;
                    }
                    Toast.makeText(deleteMenu.this, menu_name + " 제거 완료", Toast.LENGTH_SHORT).show();
                    break;
                }
                finish();
            }
        });
    }
}