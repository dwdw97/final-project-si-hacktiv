package com.hacktiv.ecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hacktiv.ecommerce.R;
import com.hacktiv.ecommerce.User.ProductCategoryActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private String[] categories;
    private TypedArray categoryIcons;
    private Context context;

    public CategoryAdapter(String[] categories, TypedArray categoryIcons) {
        this.categories = categories;
        this.categoryIcons = categoryIcons;
    }

    public CategoryAdapter(Context context, String[] categories, TypedArray categoryIcons) {
        this.categories = categories;
        this.categoryIcons = categoryIcons;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.tvCategoryName.setText(categories[position+1]);
        holder.ivCategoryImg.setImageResource(categoryIcons.getResourceId(position, 0));
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivitiesWithData(categories[holder.getAdapterPosition()+1]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.length-1;
    }

    private void switchActivitiesWithData(String category) {
        // pass data between Activities
        Intent switchActivityIntent = new Intent(context, ProductCategoryActivity.class);
        switchActivityIntent.putExtra("Category", category);
        context.startActivity(switchActivityIntent);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCategoryImg;
        TextView tvCategoryName;
        CardView cvItem;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCategoryImg = itemView.findViewById(R.id.ivCardImg);
            tvCategoryName = itemView.findViewById(R.id.tvCardName);
            cvItem = itemView.findViewById(R.id.cvItem);
        }
    }
}
