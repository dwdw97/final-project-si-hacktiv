package com.example.a3_mechanicalcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalculationAdapter.HistoryClickListener {

    private TextView tvCurrentCalculation, tvResult;
    private Button btnClear;
    private RecyclerView recyclerView;
    private CalculationAdapter calculationAdapter;

    private Calculation currentCalc;
    private ArrayList<Calculation> calculationList = new ArrayList<Calculation>();
    private boolean finalized = false;

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentCalculation = findViewById(R.id.tvCurrentCalculation);
        tvResult = findViewById(R.id.tvResult);
        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btnDot = findViewById(R.id.btnDot);
        btnClear = findViewById(R.id.btnClear);
        ImageButton btnBackspace = findViewById(R.id.btnBackspace);
        Button btnPercent = findViewById(R.id.btnPercent);
        Button btnDivide = findViewById(R.id.btnDivide);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnSubstract = findViewById(R.id.btnSubstract);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnEqual = findViewById(R.id.btnEqual);

        recyclerView = findViewById(R.id.rvHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        db = new SQLiteDatabaseHandler(this);
        calculationList = (ArrayList<Calculation>) db.getAllCalculation();
        calculationAdapter = new CalculationAdapter(calculationList, this);
        recyclerView.setAdapter(calculationAdapter);
        if(calculationList.size() != 0) {
            recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);
        }
//        recyclerView.getAdapter().notifyDataSetChanged();


        // NUMBER BUTTONS ONCLICKLISTENER
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvCurrentCalculation.getText().equals("0"))
                    onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(((Button) v).getText().toString());
            }
        });


        // OPERATOR BUTTONS ONCLICKLISTENER
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalized) {
                    calculationList.add(currentCalc);
                    db.addCalculation(currentCalc);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    currentCalc = new Calculation(".");
                    showCurrentCalc();
                    recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);

                    tvCurrentCalculation.setTextSize(50);
                    tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
                    tvResult.setTextSize(35);
                    tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
                    finalized = false;
                } else {
                    if (tvCurrentCalculation.getText().equals("0")) {
                        currentCalc = new Calculation(".");
                        recyclerView.getAdapter().notifyDataSetChanged();
                        tvResult.setVisibility(View.VISIBLE);
                        btnClear.setText("C");
                    } else {
                        currentCalc.addDot();
                    }
                    showCurrentCalc();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvCurrentCalculation.getText().equals("0")) {
                    currentCalc.clear();
                } else {
                    calculationList.removeAll(calculationList);
                    db.deleteAllCalculation();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                btnClear.setText("AC");
                tvCurrentCalculation.setText("0");
                tvCurrentCalculation.setTextSize(50);
                tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
                tvResult.setTextSize(35);
                tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
                tvResult.setVisibility(View.GONE);
                finalized = false;
            }
        });
        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalized) {
                    if (!tvCurrentCalculation.getText().equals("0")) {
                        currentCalc.backspace();
                        if (currentCalc.getCalculationStr().equals("")) {
                            currentCalc.clear();
                            tvCurrentCalculation.setText("0");
                            btnClear.setText("AC");
                            tvResult.setVisibility(View.GONE);
                        } else
                            showCurrentCalc();
                    }
                }
            }
        });
        btnPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalized) {
                    calculationList.add(currentCalc);
                    db.addCalculation(currentCalc);
                    recyclerView.getAdapter().notifyDataSetChanged();

                    String prevResult = tvResult.getText().toString().substring(2);
                    Double percentaged = Double.parseDouble(prevResult) * 0.01;
                    currentCalc = new Calculation(String.valueOf(percentaged));
                    showCurrentCalc();
                    recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);

                    tvCurrentCalculation.setTextSize(50);
                    tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
                    tvResult.setTextSize(35);
                    tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
                    finalized = false;
                } else {
                    if (!tvCurrentCalculation.getText().equals("0")) {
                        currentCalc.onPercentPressed();
                        showCurrentCalc();
                    }
                }
            }
        });
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorPressed(((Button)v).getText().toString());
            }
        });
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorPressed(((Button)v).getText().toString());
            }
        });
        btnSubstract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorPressed(((Button)v).getText().toString());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorPressed(((Button)v).getText().toString());
            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFinalize();
                finalized = true;
            }
        });

    }

    void onNumberPressed(String text) {
        if(finalized) {
            calculationList.add(currentCalc);
            db.addCalculation(currentCalc);
            recyclerView.getAdapter().notifyDataSetChanged();
            currentCalc = new Calculation(text);
            showCurrentCalc();
            recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);

            tvCurrentCalculation.setTextSize(50);
            tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
            tvResult.setTextSize(35);
            tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
            finalized = false;
        } else {
            if (tvCurrentCalculation.getText().equals("0")) {
                currentCalc = new Calculation(text);
                recyclerView.getAdapter().notifyDataSetChanged();
                tvResult.setVisibility(View.VISIBLE);
                btnClear.setText("C");
            } else {
                currentCalc.addNumber(text);
            }
            showCurrentCalc();
        }
    }

    void onOperatorPressed(String text) {
        if(finalized) {
            String prevResult = tvResult.getText().toString().substring(2);
            calculationList.add(currentCalc);
            db.addCalculation(currentCalc);
            recyclerView.getAdapter().notifyDataSetChanged();
            currentCalc = new Calculation(prevResult.concat(text));
            showCurrentCalc();
            recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);

            tvCurrentCalculation.setTextSize(50);
            tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
            tvResult.setTextSize(35);
            tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
            finalized = false;
        } else {
            if (tvCurrentCalculation.getText().equals("0")) {
                currentCalc = new Calculation(text);
                recyclerView.getAdapter().notifyDataSetChanged();
                tvResult.setVisibility(View.VISIBLE);
                btnClear.setText("C");
            } else {
                currentCalc.addOperator(text);
            }
            showCurrentCalc();
        }
    }

    void setFinalize() {
        tvCurrentCalculation.setTextSize(35);
        tvCurrentCalculation.setTextColor(getResources().getColor(R.color.less_black, null));

        tvResult.setTextSize(50);
        tvResult.setTextColor(getResources().getColor(R.color.black, null));
    }

    void showCurrentCalc() {
        tvCurrentCalculation.setText(currentCalc.getCalculationStr());
        Double result = currentCalc.getResult();
        if(result%1 == 0)
            tvResult.setText("= ".concat(String.valueOf(result)));
        else {
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(8);
            tvResult.setText("= ".concat(df.format(result)));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // save current calculation
        savePreference(tvCurrentCalculation.getText().toString(), finalized);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore saved calculation
        if(getPreference("calculation") != null) {
            currentCalc = new Calculation(getPreference("calculation"));
            btnClear.setText("C");
            showCurrentCalc();

            finalized = this.getSharedPreferences("AndroidCycle", Context.MODE_PRIVATE).getBoolean("finalizedState", false);
            if(finalized) {
                setFinalize();
            }
        }
    }

    void savePreference(String currentCalc, boolean finalized){
        SharedPreferences.Editor edit = this.getSharedPreferences("AndroidCycle", Context.MODE_PRIVATE).edit();
        edit.putString("calculation", currentCalc);
        edit.putBoolean("finalizedState", finalized);
        edit.apply();
    }

    String getPreference(String key){
        return this.getSharedPreferences("AndroidCycle", Context.MODE_PRIVATE).getString(key, null);
    }

    @Override
    public void onHistoryItemClick(int position) {
        if(finalized) {
            calculationList.add(currentCalc);
            db.addCalculation(currentCalc);
            recyclerView.getAdapter().notifyDataSetChanged();
            currentCalc = calculationList.get(position);
            showCurrentCalc();
            recyclerView.smoothScrollToPosition(calculationAdapter.getItemCount()-1);

            tvCurrentCalculation.setTextSize(50);
            tvCurrentCalculation.setTextColor(getResources().getColor(R.color.black, null));
            tvResult.setTextSize(35);
            tvResult.setTextColor(getResources().getColor(R.color.less_black, null));
            finalized = false;
        } else {
            currentCalc = calculationList.get(position);
            tvResult.setVisibility(View.VISIBLE);
            showCurrentCalc();
        }

    }
}