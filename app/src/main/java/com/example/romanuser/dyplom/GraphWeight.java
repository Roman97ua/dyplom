package com.example.romanuser.dyplom;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GraphWeight extends AppCompatActivity {

    Profile.DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    LineGraphSeries<DataPoint> series;
    EditText etWeight;
    String[] dateLabel;
    private double weight;
    Calendar ca = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(ca.getTime());
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graphweight);
        dbHelper = new Profile.DBHelper(this);
        etWeight = (EditText) findViewById(R.id.editText3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        GraphView graph = (GraphView) findViewById(R.id.graph1);

        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        Cursor cu = db.query("weight", null, null, null, null, null, null);
        DataPoint[] data = new DataPoint[cu.getCount()];
        int j = 0;
        dateLabel = new String[cu.getCount()];
        if (cu.getCount() < 3) {
            dateLabel = new String[3];
        }
        if (cu.getCount() > 0) {
            if (cu.moveToFirst()) {
                int dateColumn1 = cu.getColumnIndex("date");
                do {
                    date = cu.getString(dateColumn1);
                } while (cu.moveToNext());
                if (date.equals(formattedDate)) {
                    if (cu.moveToFirst()) {
                        int dateColumn = cu.getColumnIndex("date");
                        int weightColumn = cu.getColumnIndex("weight");
                        do {
                            weight = cu.getDouble(weightColumn);
                            final String date = cu.getString(dateColumn);

                            dateLabel[cu.getPosition()] = date;

                            data[j++] = new DataPoint(cu.getPosition(), weight);

                            series = new LineGraphSeries<>(data);


                        } while (cu.moveToNext());
                        graph.addSeries(series);
                        if (cu.getCount() == 1) {
                            dateLabel[1] = "";
                            dateLabel[2] = "";
                        }
                        if (cu.getCount() == 2) {
                            dateLabel[2] = "";
                        }
                        staticLabelsFormatter.setHorizontalLabels(dateLabel);
                        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                    }
                } else {
                    ContentValues cv3 = new ContentValues();
                    cv3.put("weight", 0);
                    cv3.put("date", formattedDate);
                    db.insert("weight", null, cv3);
                    if (cu.moveToFirst()) {
                        int dateColumn = cu.getColumnIndex("date");
                        int weightColumn = cu.getColumnIndex("weight");
                        do {
                            weight = cu.getDouble(weightColumn);
                            final String date = cu.getString(dateColumn);

                            dateLabel[cu.getPosition()] = date;


                            data[j++] = new DataPoint(cu.getPosition(), weight);

                            series = new LineGraphSeries<>(data);


                        } while (cu.moveToNext());
                        graph.addSeries(series);
                        if (cu.getCount() == 1) {
                            dateLabel[1] = "";
                            dateLabel[2] = "";
                        }
                        if (cu.getCount() == 2) {
                            dateLabel[2] = "";
                        }
                        staticLabelsFormatter.setHorizontalLabels(dateLabel);
                        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                    }
                }
            } else {
                Log.d(LOG_TAG, "0 rows");
            }
        } else {
            ContentValues cv3 = new ContentValues();
            cv3.put("weight", 0);
            cv3.put("date", formattedDate);
            db.insert("weight", null, cv3);
        }
    }

    public void onClickSaveWeight(View view) {
        ContentValues cv = new ContentValues();

        cv.put("weight", Double.parseDouble(etWeight.getText().toString()));

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updCount = db.update("weight", cv, "date = ?",
                new String[]{formattedDate});
        Log.d(LOG_TAG, "updated rows count = " + updCount);


        dbHelper = new Profile.DBHelper(this);
        etWeight = (EditText) findViewById(R.id.editText3);

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        graph.removeAllSeries();
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        Cursor cu = db.query("weight", null, null, null, null, null, null);
        DataPoint[] data = new DataPoint[cu.getCount()];
        int j = 0;
        dateLabel = new String[cu.getCount()];
        if (cu.getCount() < 3) {
            dateLabel = new String[3];
        }

        if (cu.moveToFirst()) {
            int dateColumn = cu.getColumnIndex("date");
            int weightColumn = cu.getColumnIndex("weight");
            do {
                weight = cu.getDouble(weightColumn);
                final String date = cu.getString(dateColumn);

                dateLabel[cu.getPosition()] = date;


                data[j++] = new DataPoint(cu.getPosition(), weight);

                series = new LineGraphSeries<>(data);


            } while (cu.moveToNext());
            graph.addSeries(series);
            if (cu.getCount() == 1) {
                dateLabel[1] = "";
                dateLabel[2] = "";
            }
            if (cu.getCount() == 2) {
                dateLabel[2] = "";
            }
            staticLabelsFormatter.setHorizontalLabels(dateLabel);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        } else {
            Log.d(LOG_TAG, "0 rows");
        }
    }
}