package com.example.concussionapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpandedDay extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LineChart mChart;
    private Thread thread;
    private boolean plotData = false;

    private float valueMax = 120f;
    private float valueMin = 0f;

    private final float SAMPLE_RATE = 90;
    private final float SAMPLE_PERIOD = 1/SAMPLE_RATE;

    public ExpandedDay() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_day); // remove because of onCreateView?

        TextView dateText = findViewById(R.id.dateText);
        final Intent intent = getIntent();
        String input = intent.getStringExtra("DATE");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("MMMM dd, yyyy");
        String output = sdf.format(d);
        dateText.setText(output);

        mChart = (LineChart) findViewById(R.id.lineChartExpanded);
        mChart.getDescription().setEnabled(true);
        mChart.setNoDataText("No data currently");
        mChart.setNoDataTextColor(Color.WHITE);

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(Color.BLACK);
        mChart.setPinchZoom(true);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend leg = mChart.getLegend();
        leg.setForm(Legend.LegendForm.LINE);
        leg.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setDrawGridLines(true);
        y1.setAxisMaximum(valueMax);
        y1.setAxisMinimum(valueMin);

        YAxis y2 = mChart.getAxisRight();
        y2.setEnabled(false);

        getData();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_expanded_day, container, false); // changed this to R.layout.activity_expanded_day, but still did not display black when i had it uncommented

        mChart = (LineChart) view.findViewById(R.id.lineChartExpanded);
        mChart.getDescription().setEnabled(true);
        mChart.setNoDataText("No data currently");
        mChart.setNoDataTextColor(Color.WHITE);

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setBackgroundColor(Color.BLACK);
        mChart.setPinchZoom(true);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend leg = mChart.getLegend();
        leg.setForm(Legend.LegendForm.LINE);
        leg.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
        x1.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setDrawGridLines(true);
        y1.setAxisMaximum(valueMax);
        y1.setAxisMinimum(valueMin);

        YAxis y2 = mChart.getAxisRight();
        y2.setEnabled(false);

        getData();

        return view;
    }

    private void getData() {
        LineData data = mChart.getData();

        if(data != null) {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

            if(set == null) {
                set = createSet();
                data.addDataSet(set);
            }


            for(int i = 0; i < 200; i++) {
                data.addEntry(new Entry( set.getEntryCount(), (float)(Math.random()*120)), 0);
            }
            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(150f);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "EEG Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(getResources().getColor(R.color.bmes_color));
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }
}
