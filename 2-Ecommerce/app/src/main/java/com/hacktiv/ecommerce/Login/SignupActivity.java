package com.hacktiv.ecommerce.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hacktiv.ecommerce.Model.Accounts;
import com.hacktiv.ecommerce.R;


public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btnRegis = findViewById(R.id.btnRegister);
        TextView btnLogin = findViewById(R.id.tvLogin);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String name = ((EditText) findViewById(R.id.txtRegisName)).getText().toString().trim();
                String email = ((EditText) findViewById(R.id.txtRegisEmail)).getText().toString().trim();
                String password1 = ((EditText) findViewById(R.id.txtRegisPass)).getText().toString().trim();
                String password2 = ((EditText) findViewById(R.id.txtRegisConfirm)).getText().toString().trim();

                //jika ada yang kosong
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                    Toast.makeText(SignupActivity.this, "There can't be any empty field!", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(password1, password2)) {
                    Toast.makeText(SignupActivity.this, "Both passwords must be the same!", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignupActivity.this, "Your email is not valid", Toast.LENGTH_LONG).show();
                } else if (password1.length() > 15) {
                    Toast.makeText(SignupActivity.this, "Password maximum length is 15 characters", Toast.LENGTH_SHORT).show();
                } else if (password1.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Password minimal length is 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setTitle("Registration");
                    progressDialog.setMessage("Please wait while we are checking your credentials");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                storeNewUsersData(uid, name, email, password1);

                                Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean isValidEmail(CharSequence email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void storeNewUsersData(String uid, String name, String email, String password) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Accounts");

        Accounts newUser = new Accounts(email, name, password, "user");
        reference.child(uid).setValue(newUser);
    }
}