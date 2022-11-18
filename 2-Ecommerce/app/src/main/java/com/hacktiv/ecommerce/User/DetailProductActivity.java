package com.hacktiv.ecommerce.User;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hacktiv.ecommerce.Model.Item;
import com.hacktiv.ecommerce.R;
import com.squareup.picasso.Picasso;

public class DetailProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        // catch data from previous Activity
        Item item = (Item)getIntent().getSerializableExtra("Item");

        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvProductPrice = findViewById(R.id.tvProductPrice);
        TextView tvProductQty = findViewById(R.id.tvProductQuantity);
        TextView tvProductDesc = findViewById(R.id.tvProductDescription);
        ImageView ivProductPic = findViewById(R.id.ivProductPic);

        // set values
        Picasso.with(this)
                .load(item.getImgUrl())
                .fit()
                .centerCrop()
                .into(ivProductPic);

        tvProductName.setText(item.getItemName());
        tvProductPrice.setText("Rp"+item.getItemPrice());
        tvProductQty.setText(Integer.toString(item.getItemQuantity()));
        tvProductDesc.setText(item.getItemDescription());
    }
}