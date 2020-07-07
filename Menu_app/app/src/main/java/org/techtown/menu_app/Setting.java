package org.techtown.menu_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Setting extends AppCompatActivity {

    Button Home, add, delete, sync;
    String username, email;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Intent intent = getIntent();
        username = intent.getStringExtra("username").trim();
        email = intent.getStringExtra("email").trim();

        Home = findViewById(R.id.homebutton);
        add = findViewById(R.id.addmenu);
        delete = findViewById(R.id.deletemenu);
        sync = findViewById(R.id.synchronization);

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
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, deleteMenu.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot dataSnapshot1 = dataSnapshot.child("user").child(username).child("menu");
                        for (DataSnapshot ds : dataSnapshot1.getChildren()){
                            User user = ds.getValue(User.class);
                            if (user.getEmail().equals(email)){
                                while(true) {
                                    name = findViewById(R.id.name);
                                    menu_name = name.getText().toString().trim();
                                    try {
                                        firebaseReference.child("user").child(user.getName()).child("menu").child(menu_name).removeValue();
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(deleteMenu.this, "메뉴를 추가하는 도중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}