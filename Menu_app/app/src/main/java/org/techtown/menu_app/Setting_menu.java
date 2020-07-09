package org.techtown.menu_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Setting_menu extends AppCompatActivity {

    public static Context CONTEXT;

    private RecyclerView foodRecyclerView;
    private RecyclerView.Adapter foodAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> foodArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference foodReference;
    Button add, delete;
    String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_setting);

        CONTEXT = this;

        Intent intent = getIntent();
        username = intent.getStringExtra("username").trim();
        email = intent.getStringExtra("email").trim();

        add = findViewById(R.id.addmenu);
        delete = findViewById(R.id.deletemenu);

        foodRecyclerView = findViewById(R.id.menulist);
        foodRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        foodRecyclerView.setLayoutManager(layoutManager);
        foodArrayList = new ArrayList<>(); // food 객체를 담음

        firebaseDatabase = FirebaseDatabase.getInstance();
        foodReference = firebaseDatabase.getReference("user/"+username+"/Food");
        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                foodArrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    try {
                        Food food = snapshot.getValue(Food.class);
                        foodArrayList.add(food);
                    } catch(Exception e) {
                        continue;
                    }
                }
                foodAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        foodAdapter = new CustomAdapter(foodArrayList, this);
        foodRecyclerView.setAdapter(foodAdapter); // 리사이클러뷰에 어댑터 연결

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_menu.this, addMenu.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_menu.this, deleteMenu.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        foodReference = firebaseDatabase.getReference("user/" + username + "/Food");
        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                foodArrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    Log.d("Setting", "ok");
                    try {
                        Food food = snapshot.getValue(Food.class);
                        foodArrayList.add(food);
                    } catch (Exception e) {
                        continue;
                    }
                }
                foodAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        foodAdapter = new CustomAdapter(foodArrayList, this);
        foodRecyclerView.setAdapter(foodAdapter); // 리사이클러뷰에 어댑터 연결
    }
}