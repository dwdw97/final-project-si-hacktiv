package com.hacktiv.ecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hacktiv.ecommerce.Model.Item;
import com.hacktiv.ecommerce.R;
import com.hacktiv.ecommerce.User.DetailProductActivity;
import com.hacktiv.ecommerce.User.ProductCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    Context context;
    ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        holder.tvCardName.setText(items.get(position).getItemName());
        Picasso.with(context)
                .load(items.get(position).getImgUrl())
                .fit()
                .centerCrop()
                .into(holder.ivCardImg);
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivitiesWithData(items.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    private void switchActivitiesWithData(Item item) {
        // pass data between Activities
        Intent switchActivityIntent = new Intent(context, DetailProductActivity.class);
        switchActivityIntent.putExtra("Item", item);
        context.startActivity(switchActivityIntent);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView cvItem;
        ImageView ivCardImg;
        TextView tvCardName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cvItem = itemView.findViewById(R.id.cvItem);
            ivCardImg = itemView.findViewById(R.id.ivCardImg);
            tvCardName = itemView.findViewById(R.id.tvCardName);
        }
    }
}
