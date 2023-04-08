package com.example.gpapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Charts extends AppCompatActivity {
    BarChart barChart;
    PieChart pieChart;
    Button Change,Reset;
    private ArrayList<BarEntry> barEntries;
    private ArrayList<PieEntry> pieEntries;
    static int index;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        barEntries = new ArrayList<>();
        pieEntries = new ArrayList<>();
        init();
        Change=(Button) findViewById(R.id.change_button);
        Change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Change(index++);
            }
        });

        Reset=(Button) findViewById(R.id.reset_button);
        Reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init(){
        index=1;
        Change(index++);
    }

    public void Change(int index){
        generate_data(index);
    }

    public void generate_data(int index){
        for (int i = 1; i <= 10; i++) {

            float value = (float) (index* i * 10.0);
            BarEntry barEntry = new BarEntry(i, value);
            PieEntry pieEntry = new PieEntry(i, value);

            if(index==2){
                barEntries.add(i-1,barEntry);
                pieEntries.add(i-1,pieEntry);
            }
            else{
                barEntries.set(i-1,barEntry);
                pieEntries.set(i-1,pieEntry);
            }

        }
        create_barchart(barEntries);
        create_piechart(pieEntries);
    }

    public void create_barchart(ArrayList<BarEntry> barEntries){
        BarDataSet barDataSet = new BarDataSet(barEntries, "Emplooyes");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setDrawValues(false);
        barChart.setData(new BarData(barDataSet));
        barChart.animateY(5000);
        barChart.getDescription().setText("Empolyees Chart");
        barChart.getDescription().setTextColor(Color.BLUE);
    }

    public void create_piechart(ArrayList<PieEntry> pieEntries){
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Student");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(5000, 5000);
        pieChart.getDescription().setText("Empolyees Chart");
        pieChart.getDescription().setEnabled(false);
    }

}