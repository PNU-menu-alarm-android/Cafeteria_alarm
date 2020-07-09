package org.techtown.menu_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Menu extends AppCompatActivity {

    ImageButton Home;
    String selected;
    public static Context MCONTEXT;
    private RecyclerView menuRecyclerView;
    private RecyclerView.Adapter menuAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> menuArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference menuReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Intent intent = getIntent();
        selected = intent.getStringExtra("selected").trim();

        MCONTEXT = this;

        menuRecyclerView = findViewById(R.id.menulist);
        menuRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(layoutManager);
        menuArrayList = new ArrayList<Food>(); // food 객체를 담음

        firebaseDatabase = FirebaseDatabase.getInstance();
        menuReference = firebaseDatabase.getReference(selected);
        menuReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                menuArrayList.clear(); // 기존 배열리스트가 존재하지 않게 초기화
                String menu_name = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추출
                    try {
                        menu_name = snapshot.getValue(String.class);
                        if (menu_name.equals("x")) { menu_name = "식단 없음"; }
                        else if(menu_name.equals("")) { continue; }
                        Food food = new Food(menu_name);
                        menuArrayList.add(food);
                    } catch(Exception e) {
                        continue;
                    }
                }
                menuAdapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던 중 에러 발생 시
                Log.e("Setting", String.valueOf(databaseError.toException()));
            }
        });

        menuAdapter = new CustomAdapter(menuArrayList, this);
        menuRecyclerView.setAdapter(menuAdapter); // 리사이클러뷰에 어댑터 연결

        Home = findViewById(R.id.homebutton);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}