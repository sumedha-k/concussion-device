package com.example.concussionapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EEGData {
    private LineData data;
    private LineDataSet dataSet;
    private List<Entry> entries;

    public EEGData(LineData dataIn) {
        data = dataIn;
        dataSet = (LineDataSet) data.getDataSetByIndex(0);
        entries = dataSet.getValues();
    }

    public static void toCSV(LineData dataIn, String dir) {
        LineDataSet dataSet = (LineDataSet) dataIn.getDataSetByIndex(0);
        List<Entry> entries = dataSet.getValues();
        List<String> output = new ArrayList<String>();

        for(int i = 0; i < entries.size(); i++) {
            Entry etr = entries.get(i);
            String temp = etr.getX() + "," + etr.getY();
            output.add(temp);
        }

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date now = new Date();
        String fileName = dir +"/" + dtf.format(now) + ".csv";
        File out = new File(fileName);

        try {
            FileWriter csvWriter = new FileWriter(fileName);

            csvWriter.append("Time");
            csvWriter.append(",");
            csvWriter.append("Signal");
            csvWriter.append("\n");

            for(String str : output) {
                csvWriter.append(str);
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static LineData fromCSV(File fileIn) {
        List<String[]> content = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(fileIn))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                content.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 1; i < content.size(); i++) {
            String[] str = content.get(i);
            entries.add(new Entry(Float.parseFloat(str[0]), Float.parseFloat(str[1])));
        }

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        set.setValues(entries);

        LineData out = new LineData();
        out.addDataSet(set);

        return out;
    }
}

