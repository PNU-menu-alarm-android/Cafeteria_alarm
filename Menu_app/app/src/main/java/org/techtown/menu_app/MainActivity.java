package org.techtown.menu_app;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText email_login, pwd_login;
    Button login;
    TextView sign_up;
    FirebaseAuth firebaseAuth;
    String name=null, email, pwd;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sign_up = findViewById(R.id.Sign);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sign_up.class);
                startActivity(intent);
            }
        });

        firebaseAuth = firebaseAuth.getInstance();
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_login = findViewById(R.id.loginEmail);
                pwd_login = findViewById(R.id.loginPW);

                email = email_login.getText().toString().trim();
                pwd = pwd_login.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DataSnapshot dataSnapshot1 = dataSnapshot.child("user");
                                    boolean find = false;
                                    for (DataSnapshot ds : dataSnapshot1.getChildren()){
                                        User user = ds.getValue(User.class);
                                        if (user.getEmail().equals(email)){
                                            name = user.getName();
                                            find = true;
                                            Intent intent = new Intent(MainActivity.this, home.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                            break;
                                        }
                                    }
                                    if (find == false) {
                                        Toast.makeText(MainActivity.this, "Email 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                        email_login.setText("");
                                        pwd_login.setText("");
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(MainActivity.this, "회원정보를 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                    email_login.setText("");
                                    pwd_login.setText("");
                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, "Email 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            email_login.setText("");
                            pwd_login.setText("");
                        }
                    }
                });
            }
        });
    }

}
