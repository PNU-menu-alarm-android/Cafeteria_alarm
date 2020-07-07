package org.techtown.menu_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {

    public static Context CONTEXT;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;

    Button Home, add, delete, sync;
    String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        CONTEXT = this;

        Intent intent = getIntent();
        username = intent.getStringExtra("username").trim();
        email = intent.getStringExtra("email").trim();

        Home = findViewById(R.id.homebutton);
        add = findViewById(R.id.addmenu);
        delete = findViewById(R.id.deletemenu);
        sync = findViewById(R.id.synchronization);
        recyclerView = findViewById(R.id.menulist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // food 객체를 담음

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("user/"+username+"/Food");
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    Log.d("Setting", "ok");
                    try {
                        Food food = snapshot.getValue(Food.class);
                        arrayList.add(food);
                    } catch(Exception e) {
                        continue;
                    }
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

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

        /*
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
        });*/
    }

    @Override
    public void onResume()
    {
        super.onResume();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("user/"+username+"/Food");
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    Log.d("Setting", "ok");
                    try {
                        Food food = snapshot.getValue(Food.class);
                        arrayList.add(food);
                    } catch(Exception e) {
                        continue;
                    }
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
    }
}