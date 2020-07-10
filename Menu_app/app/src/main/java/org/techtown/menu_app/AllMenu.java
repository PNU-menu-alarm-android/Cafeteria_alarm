package org.techtown.menu_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class AllMenu extends AppCompatActivity {
    Button Home;
    private RecyclerView menuRecyclerView;
    private RecyclerView.Adapter menuAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Food> menuArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference menuReference;
    private RadioGroup time;
    private int time_id;
    private RadioButton time_bt;
    private Spinner week_sp, place_sp;

    ArrayList<String> week_list, place_list;
    ArrayAdapter<String> weekAdapter, placeAdapter;

    private Button search;

    private String selected_week, selected_place, selected_time, selected_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        menuRecyclerView = findViewById(R.id.menulist);
        menuRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        menuRecyclerView.setLayoutManager(layoutManager);
        menuArrayList = new ArrayList<Food>(); // food 객체를 담음

        search = findViewById(R.id.search);

        week_list = new ArrayList<>();
        week_list.add("월");
        week_list.add("화");
        week_list.add("수");
        week_list.add("목");
        week_list.add("금");
        week_list.add("토");

        weekAdapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, week_list);

        week_sp = (Spinner)findViewById(R.id.week);
        week_sp.setAdapter(weekAdapter);
        week_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_week = week_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        place_list = new ArrayList<>();
        place_list.add("금정회관교직원식당");
        place_list.add("금정회관학생식당");
        place_list.add("문창회관교직원식당");
        place_list.add("문창회관학생식당");
        place_list.add("샛벌회관식당");
        place_list.add("학생회관교직원식당");
        place_list.add("학생회관학생식당");

        placeAdapter = new ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, place_list);

        place_sp = (Spinner)findViewById(R.id.place);
        place_sp.setAdapter(placeAdapter);
        place_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_place = place_list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        time = findViewById(R.id.time);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_id = time.getCheckedRadioButtonId();
                time_bt = (RadioButton)findViewById(time_id);

                selected_time = time_bt.getText().toString().trim();

                selected_group = selected_week + "/" + selected_place +
                        "/" + selected_time;

                firebaseDatabase = FirebaseDatabase.getInstance();
                menuReference = firebaseDatabase.getReference(selected_group);
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
