package com.hacktiv.ecommerce.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hacktiv.ecommerce.AboutUsActivity;
import com.hacktiv.ecommerce.Admin.AdminActivity;
import com.hacktiv.ecommerce.R;
import com.hacktiv.ecommerce.Staff.StaffActivity;
import com.hacktiv.ecommerce.User.UserActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.getLogin()) {
            Intent intent;
            switch(sessionManager.getLevel()) {
                case "admin":
                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                    break;
                case "staff":
                    intent = new Intent(LoginActivity.this, StaffActivity.class);
                    break;
                default:
                    intent = new Intent(LoginActivity.this, UserActivity.class);
                    break;
            }
            startActivity(intent);
            finish();
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnSignup = findViewById(R.id.tvSignup);
        TextView btnAboutus = findViewById(R.id.tvAboutus);

        // =============== onClickListeners ==============
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.txtLoginEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.txtLoginPass)).getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    SessionManager sessionManager = new SessionManager(getApplicationContext());

                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Please wait while we are checking your credentials");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                sessionManager.setLogin(true);
                                sessionManager.setUID(uid);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accounts");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            String level = snapshot.child(uid).child("level").getValue(String.class);

                                            Intent intent;
                                            switch (level) {
                                                case "admin":
                                                    sessionManager.setLevel(level);
                                                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                    break;
                                                case "staff":
                                                    sessionManager.setLevel(level);
                                                    intent = new Intent(LoginActivity.this, StaffActivity.class);
                                                    break;
                                                default:
                                                    sessionManager.setLevel(level);
                                                    intent = new Intent(LoginActivity.this, UserActivity.class);
                                            }
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });
    }
}