package com.example.suitcase2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup_Page extends AppCompatActivity {
    EditText email_signup, password_signup, confirmPassword_signup;
    Button btn_signup;
    TextView txt_login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email_signup = findViewById(R.id.sigunp_email);
        password_signup = findViewById(R.id.sigunp_password);
        confirmPassword_signup = findViewById(R.id.confirm_password);
        btn_signup = findViewById(R.id.signup_btn);
        txt_login = findViewById(R.id.login_txt);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_signup.getText().toString().trim();
                String password = password_signup.getText().toString().trim();
                String cPassword = confirmPassword_signup.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(Signup_Page.this, "Please Enter Email First", Toast.LENGTH_SHORT).show();
                }
                if (password.isEmpty()) {
                    Toast.makeText(Signup_Page.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                if (cPassword.isEmpty()) {
                    Toast.makeText(Signup_Page.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }
                if (password.equals(cPassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), Login_Page.class);
                                        startActivity(intent);
                                        Toast.makeText(Signup_Page.this, "Signup Complete", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Signup_Page.this, "Signup Not Complete", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup_Page.this, Login_Page.class);
                startActivity(intent);
            }
        });
    }
}
