package com.hacktiv.ecommerce.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hacktiv.ecommerce.Adapter.ItemAdapter;
import com.hacktiv.ecommerce.Admin.AddStockActivity;
import com.hacktiv.ecommerce.Model.Item;
import com.hacktiv.ecommerce.R;

import java.util.ArrayList;

public class ProductCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        // catch data from previous Activity
        String category = getIntent().getStringExtra("Category");

        TextView tvProductCategory = findViewById(R.id.tvProductCategory);
        tvProductCategory.setText(category);

        Spinner spinFilter = findViewById(R.id.spinnerFilter);
        RecyclerView recyclerView = findViewById(R.id.rvProduct);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager((layoutManager));

        if(category.equals("Clothing") || category.equals("Electronic")) {
            ArrayAdapter arrayAdapter;
            if (category.equals("Clothing")) {
                spinFilter.setVisibility(View.VISIBLE);
                arrayAdapter = ArrayAdapter.createFromResource(ProductCategoryActivity.this, R.array.Clothing_filter, R.layout.spinner_item);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinFilter.setAdapter(arrayAdapter);
            } else if (category.equals("Electronic")) {
                spinFilter.setVisibility(View.VISIBLE);
                arrayAdapter = ArrayAdapter.createFromResource(ProductCategoryActivity.this, R.array.Electronic_filter, R.layout.spinner_item);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinFilter.setAdapter(arrayAdapter);
            }

            spinFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<Item> items = new ArrayList<>();

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Items");
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String filter = spinFilter.getSelectedItem().toString();
                            if (snapshot.child(category).child(filter).exists()) {
                                recyclerView.setVisibility(View.VISIBLE);
                                for (DataSnapshot dataSnapshot : snapshot.child(category).child(filter).getChildren()) {
                                    Item item = dataSnapshot.getValue(Item.class);
                                    items.add(item);
                                    ItemAdapter itemAdapter = new ItemAdapter(ProductCategoryActivity.this, items);
                                    recyclerView.setAdapter(itemAdapter);
                                }
                            } else
                                recyclerView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ProductCategoryActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            ArrayList<Item> items = new ArrayList<>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Items");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(category).exists()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        for (DataSnapshot dataSnapshot : snapshot.child(category).getChildren()) {
                            Item item = dataSnapshot.getValue(Item.class);
                            items.add(item);
                            ItemAdapter itemAdapter = new ItemAdapter(ProductCategoryActivity.this, items);
                            recyclerView.setAdapter(itemAdapter);
                        }
                    } else
                        recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}