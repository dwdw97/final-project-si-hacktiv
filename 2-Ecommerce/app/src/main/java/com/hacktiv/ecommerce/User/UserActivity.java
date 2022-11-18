package com.hacktiv.ecommerce.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hacktiv.ecommerce.Login.SessionManager;
import com.hacktiv.ecommerce.Login.LoginActivity;
import com.hacktiv.ecommerce.Adapter.CategoryAdapter;
import com.hacktiv.ecommerce.R;

public class UserActivity extends AppCompatActivity {
    FloatingActionButton btnLogout;
    ViewFlipper flipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        flipper = findViewById(R.id.flipper);
        btnLogout = findViewById(R.id.btnUserLogout);

        int[] imgFlipper = {R.drawable.flipper_slide1,R.drawable.flipper_slide2,R.drawable.flipper_slide3};
        for (int i=0;i<imgFlipper.length;i++){
            showImage(imgFlipper[i]);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        String[] categories = getResources().getStringArray(R.array.item_categories);
        TypedArray categoryIcons = getResources().obtainTypedArray(R.array.category_icons);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories, categoryIcons);

        recyclerView.setAdapter(categoryAdapter);

        // ONCLICKLISTENERS
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void showImage(int img) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);

        flipper.addView(imageView);
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);

        flipper.setInAnimation(this, android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }


    private void logOut() {
        SessionManager sessionManager = new SessionManager(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keluar");
        builder.setMessage("Apakah anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                sessionManager.setLogin(false);
                sessionManager.setUID("");
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}