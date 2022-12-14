package com.example.a3_mechanicalcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.CalculationViewHolder> {

    ArrayList<Calculation> calculationList = new ArrayList<>();
    private HistoryClickListener historyClickListener;

    public CalculationAdapter(ArrayList<Calculation> calculationList, HistoryClickListener historyClickListener) {
        this.calculationList = calculationList;
        this.historyClickListener = historyClickListener;
    }

    @NonNull
    @Override
    public CalculationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_calc_history, parent, false);
        return new CalculationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculationViewHolder holder, int position) {
        holder.tvHistoryCalc.setText(calculationList.get(position).getCalculationStr());
        holder.tvHistoryResult.setText("= ".concat(String.valueOf(calculationList.get(position).getResult())));
    }

    @Override
    public int getItemCount() {
        return calculationList.size();
    }

    public class CalculationViewHolder extends RecyclerView.ViewHolder{
        private TextView tvHistoryCalc, tvHistoryResult;
        private ConstraintLayout historyItem;

        public CalculationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHistoryCalc = itemView.findViewById(R.id.tvHistoryCalc);
            tvHistoryResult = itemView.findViewById(R.id.tvHistoryResult);
            historyItem = itemView.findViewById(R.id.historyItem);

            historyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    historyClickListener.onHistoryItemClick(position);
                }
            });
        }
    }

    public interface HistoryClickListener {
        void onHistoryItemClick(int position);
    }
}
