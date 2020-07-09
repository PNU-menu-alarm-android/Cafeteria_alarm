package org.techtown.menu_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class home extends AppCompatActivity {

    public static Context HCONTEXT;

    ImageButton Setting, menu;
    String username, email;

    private RecyclerView foodRecyclerView, alarmRecyclerView;
    private RecyclerView.Adapter alarmAdapter;
    private RecyclerView.Adapter foodAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Alarm> alarmArrayList;
    private ArrayList<Food> foodArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference foodReference, alarmReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        final TextView infoName = findViewById(R.id.name);

        Intent intent = getIntent();
        username = intent.getStringExtra("name").trim();
        email = intent.getStringExtra("email").trim();
        infoName.setText(username);

        Setting = findViewById(R.id.settingbutton);
        menu = findViewById(R.id.allmenubutton);
        HCONTEXT = this;

        foodRecyclerView = findViewById(R.id.menulist);
        foodRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        foodRecyclerView.setLayoutManager(layoutManager);
        foodArrayList = new ArrayList<Food>(); // food 객체를 담음

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

        // 알람 리사이클러뷰
        alarmRecyclerView = findViewById(R.id.alarmlist);
        alarmRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        alarmRecyclerView.setLayoutManager(layoutManager);
        alarmArrayList = new ArrayList<>(); // food 객체를 담음

        alarmReference = firebaseDatabase.getReference("user/"+username+"/alarm");
        alarmReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                alarmArrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    try {
                        Alarm alarm = snapshot.getValue(Alarm.class);
                        alarmArrayList.add(alarm);
                    } catch(Exception e) {
                        continue;
                    }
                }
                alarmAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        alarmAdapter = new AlarmAdapter(alarmArrayList, this);
        alarmRecyclerView.setAdapter(alarmAdapter); // 리사이클러뷰에 어댑터 연결

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, Setting.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, AllMenu.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        alarmReference = firebaseDatabase.getReference("user/" + username + "/alarm");
        alarmReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                alarmArrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    try {
                        Alarm alarm = snapshot.getValue(Alarm.class);
                        alarmArrayList.add(alarm);
                    } catch (Exception e) {
                        continue;
                    }
                }
                alarmAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        alarmAdapter = new AlarmAdapter(alarmArrayList, this);
        alarmRecyclerView.setAdapter(alarmAdapter); // 리사이클러뷰에 어댑터 연결

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
