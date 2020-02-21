package com.example.romanuser.dyplom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class Graph extends AppCompatActivity {

    int x = 1;
    Profile.DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    BarGraphSeries<DataPoint> series;
TextView tx1,tx2,tx3;
    String[] dateLabel;
    private double calories;
    private double carbohydrates;
    private double proteins;
    private double fats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.graph);
        tx1=(TextView) findViewById(R.id.textView23);
        tx2=(TextView) findViewById(R.id.textView22);
        tx3=(TextView) findViewById(R.id.textView21);
        dbHelper = new Profile.DBHelper(this);
        Intent i = getIntent();
        double calories1= Double.parseDouble(i.getStringExtra("calorie"));
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        GraphView graph = (GraphView) findViewById(R.id.graph);

        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        Cursor cu = db.query("calories", null, null, null, null, null, null);
        DataPoint[] data = new DataPoint[4*cu.getCount()];
        int j = 0;
        dateLabel = new String[cu.getCount()];
        if(cu.getCount()<3){
            dateLabel = new String[3];
        }

        if (cu.moveToFirst()) {
            int caloriesColumn = cu.getColumnIndex("calories");
            int proteinsColumn = cu.getColumnIndex("proteins");
            int fatsColumn = cu.getColumnIndex("fats");
            int carbohydratesColumn = cu.getColumnIndex("carbohydrates");
            int dateColumn = cu.getColumnIndex("date");
            do {

                calories = calories1-cu.getDouble(caloriesColumn);
                proteins = calories + cu.getDouble(proteinsColumn);
                fats = proteins + cu.getDouble(fatsColumn);
                carbohydrates = fats + cu.getDouble(carbohydratesColumn);
                final String date = cu.getString(dateColumn);

                dateLabel[cu.getPosition()] = date;

                data[j++] = new DataPoint(cu.getPosition(), carbohydrates);
                data[j++] = new DataPoint(cu.getPosition(), fats);
                data[j++] = new DataPoint(cu.getPosition(), proteins);
                data[j++] = new DataPoint(cu.getPosition(), calories);

                series = new BarGraphSeries<>(data);

            } while (cu.moveToNext());
            graph.addSeries(series);
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    x++;
                    if(x==2)tx1.setTextColor(Color.rgb( 255 / 4,  x * Math.abs(255 / 4), 100));
                    if(x==3)tx2.setTextColor(Color.rgb( 255 / 4,  x * Math.abs(255 / 4), 100));
                    if(x==4)tx3.setTextColor(Color.rgb( 255 / 4,  x * Math.abs(255 / 4), 100));

                    return Color.rgb( 255 / 4,  x * Math.abs(255 / 4), 100);
                }
            });

            series.setSpacing(50);

            series.setValuesOnTopColor(Color.RED);
            if(cu.getCount()==1){
                dateLabel[1] = "";
                dateLabel[2] = "";
            }
            if(cu.getCount()==2){
                dateLabel[2] = "";
            }
            staticLabelsFormatter.setHorizontalLabels(dateLabel);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        } else {
            Log.d(LOG_TAG, "0 rows");
        }
    }
}