package com.hacktiv.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class AddStaffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        Button btnAdd = findViewById(R.id.btnStaffRegister);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String name = ((TextView)findViewById(R.id.txtStaffName)).getText().toString().trim();
                String email = ((TextView)findViewById(R.id.txtStaffEmail)).getText().toString().trim();
                String password1 = ((TextView)findViewById(R.id.txtStaffPass1)).getText().toString().trim();
                String password2 = ((TextView)findViewById(R.id.txtStaffPass2)).getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
                    Toast.makeText(AddStaffActivity.this, "There can't be any empty field!", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(password1, password2)) {
                    Toast.makeText(AddStaffActivity.this, "Both passwords must be the same!", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(AddStaffActivity.this, "Your email is not valid", Toast.LENGTH_LONG).show();
                } else if (password1.length() > 15) {
                    Toast.makeText(AddStaffActivity.this, "Password maximum length is 15 characters", Toast.LENGTH_SHORT).show();
                } else if (password1.length() < 8) {
                    Toast.makeText(AddStaffActivity.this, "Password minimal length is 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(AddStaffActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                storeNewUsersData(uid, name, email, password1);

                                Toast.makeText(AddStaffActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddStaffActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean isValidEmail(CharSequence email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void storeNewUsersData(String uid, String name, String email, String password) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Accounts");

        Accounts newUser = new Accounts(email, name, password, "staff");
        reference.child(uid).setValue(newUser);
    }
}