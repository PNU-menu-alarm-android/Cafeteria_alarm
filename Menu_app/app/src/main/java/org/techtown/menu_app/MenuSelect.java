package org.techtown.menu_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MenuSelect extends AppCompatActivity {

    private RadioGroup week, place, time;
    private int week_id, place_id, time_id;
    private RadioButton week_bt, place_bt, time_bt;

    private Button search;

    private String selected_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_menu_select);

        Intent intent = getIntent();

        search = findViewById(R.id.search);

        week = findViewById(R.id.week);
        place = findViewById(R.id.place);
        time = findViewById(R.id.time);

        week_id = week.getCheckedRadioButtonId();
        place_id = place.getCheckedRadioButtonId();
        time_id = time.getCheckedRadioButtonId();

        week_bt = (RadioButton)findViewById(week_id);
        place_bt = (RadioButton)findViewById(place_id);
        time_bt = (RadioButton)findViewById(time_id);

        selected_group = week_bt.getText().toString() + "/"
                + place_bt.getText().toString() + "/"
                + time_bt.getText().toString();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuSelect.this, Menu.class);
                intent.putExtra("selected", selected_group);
                startActivity(intent);
                finish();
            }
        });
    }

}
