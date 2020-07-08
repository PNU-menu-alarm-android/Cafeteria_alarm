package org.techtown.menu_app;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {
    private EditText et_email, et_pwd, et_name;
    private Button up;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDatabase.getReference();
    String email, pwd, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        et_email = findViewById(R.id.emailText);
        et_pwd = findViewById(R.id.passwordText);
        et_name = findViewById(R.id.nameText);
        up = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                email = et_email.getText().toString().trim();
                pwd = et_pwd.getText().toString().trim();


                firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, email, pwd);
                            firebaseReference.child("user").child(name).setValue(user);
                            Toast.makeText(Sign_up.this, "회원 가입 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(Sign_up.this, "Email이 중복이거나 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            et_email.setText("");
                            et_pwd.setText("");
                            return;
                        }
                    }
                });
            }
        });
    }
}
