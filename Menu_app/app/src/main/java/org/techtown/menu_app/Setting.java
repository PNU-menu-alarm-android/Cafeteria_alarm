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

public class Setting extends AppCompatActivity {

    public static Context CONTEXT;

    private FirebaseDatabase firebaseDatabase, database;
    private DatabaseReference firebaseReference, reference, userReference;
    Button Home, Setting_account, Setting_menu, sync;
    String username, email, menu_name;

    List<String> menus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        CONTEXT = this;

        Intent intent = getIntent();
        username = intent.getStringExtra("username").trim();
        email = intent.getStringExtra("email").trim();

        Home = findViewById(R.id.homebutton);
        Setting_account = findViewById(R.id.Setting_account);
        Setting_menu = findViewById(R.id.Setting_menu);
        sync = findViewById(R.id.synchronization);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((home)home.HCONTEXT).onResume();
                finish();
            }
        });

/*
        Setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, addMenu.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

 */

        Setting_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Setting_menu.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                userReference = firebaseDatabase.getReference("user");
                userReference.child(username).child("alarm").setValue(null);
                firebaseReference = firebaseDatabase.getReference("user/"+username+"/Food");
                firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 list를 추
                            Food food = snapshot.getValue(Food.class);
                            menu_name = food.getName();
                            menus.add(menu_name);
                        }

                        for (final String food : menus) {
                            List<String> whens = Arrays.asList("월", "화", "수", "목", "금", "토");
                            List<String> places = Arrays.asList("금정회관교직원식당", "금정회관학생식당", "문창회관교직원식당", "문창회관학생식당",
                                    "샛벌회관식당", "학생회관교직원식당", "학생회관학생식당");
                            List<String> times = Arrays.asList("조식", "중식", "석식");
                            database = FirebaseDatabase.getInstance();

                            for (final String when : whens) {
                                for (final String place : places) {
                                    for (final String time : times) {
                                        reference = database.getReference(when + "/" + place + "/" + time);
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot in_dataSnapshot) {
                                                for (DataSnapshot in_snapshot : in_dataSnapshot.getChildren()) {
                                                    String menu = (String) in_snapshot.getValue();
                                                    if (food.equals(menu)) {
                                                        String alarm_cont = when + "요일 '" + place + "'에서 '" + time + "'으로 '"
                                                                + menu + "'이/가 나옵니다.";
                                                        Alarm update_alarm = new Alarm(alarm_cont);
                                                        Map<String, Object> alarm_updates = new Hashtable<>();
                                                        alarm_updates.put(when + place + time + menu, update_alarm);
                                                        userReference.child(username).child("alarm").updateChildren(alarm_updates);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.e("sync", "alarm addition error");
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던 중 에러 발생 시
                        Log.e("Setting", String.valueOf(databaseError.toException()));
                    }
                });
                Toast.makeText(Setting.this, "알람 동기화를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                ((home)home.HCONTEXT).onResume();
            }
        });
    }
}