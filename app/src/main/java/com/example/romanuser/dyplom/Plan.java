package com.example.romanuser.dyplom;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by RomanUser on 21.10.2017.
 */

public class Plan extends Activity implements AdapterView.OnItemClickListener {
    final String LOG_TAG = "myLogs";
    Profile.DBHelper dbHelper;
    private static final int CM_DELETE_ID = 1;
    Cursor c7;
    ListView lvData1;
    SimpleCursorAdapter scAdapter;
    TextView tvCalories, tvProteins, tvFats, tvCarbohydrates, tvDate;
    Button btnProfile, btnGraph, btnPrevious, btnNext;
    Calendar ca = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(ca.getTime());
    double calories;
    double calories1;
    double proteins, id;
    String name;
    double fats;
    double carbohydrates;
    String date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan);
        dbHelper = new Profile.DBHelper(this);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvProteins = (TextView) findViewById(R.id.tvProteins);
        tvFats = (TextView) findViewById(R.id.tvFats);
        tvCarbohydrates = (TextView) findViewById(R.id.tvCarbohydrates);
        tvCalories = (TextView) findViewById(R.id.tvCalories);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnGraph = (Button) findViewById(R.id.btnGraph);


        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cu = db1.query("profile", null, null, null, null, null, null);
        Cursor cuDate = db1.query("calories", null, null, null, null, null, null);


        if (cu.getCount() > 0) {
            if (cuDate.moveToFirst()) {
                int dateColumn = cuDate.getColumnIndex("date");
                do {
                    date = cuDate.getString(dateColumn);
                } while (cuDate.moveToNext());
                if (date.equals(formattedDate)) {
                    if (cu.moveToFirst()) {
                        int weightColIndex = cu.getColumnIndex("weight");
                        int heightColIndex = cu.getColumnIndex("height");
                        int activeColIndex = cu.getColumnIndex("active");
                        int decreaseColIndex = cu.getColumnIndex("decrease");
                        int sexColIndex = cu.getColumnIndex("sex");
                        int ageColIndex = cu.getColumnIndex("age");

                        Log.i(LOG_TAG, "!!!!!weightColIndex "+Double.parseDouble(cu.getString(weightColIndex)));
                        Log.i(LOG_TAG, "!!!!!heightColIndex "+Double.parseDouble(cu.getString(heightColIndex)));
                        Log.i(LOG_TAG, "!!!!!ageColIndex "+Double.parseDouble(cu.getString(ageColIndex)));
                        Log.i(LOG_TAG, "!!!!!activeColIndex "+Double.parseDouble(cu.getString(activeColIndex)));
                        Log.i(LOG_TAG, "!!!!!decreaseColIndex "+Double.parseDouble(cu.getString(decreaseColIndex)));
                        do {
                            if (cu.getString(sexColIndex).equals("Man")) {
                                calories1 = (10 * Double.parseDouble(cu.getString(weightColIndex)) + 6.25 * Double.parseDouble(cu.getString(heightColIndex)) - 5 * Double.parseDouble(cu.getString(ageColIndex)) + 5) * Double.parseDouble(cu.getString(activeColIndex)) * Double.parseDouble(cu.getString(decreaseColIndex));
                            }
                            if (cu.getString(sexColIndex).equals("Woman")) {
                                calories1 = (10 * Double.parseDouble(cu.getString(weightColIndex)) + 6.25 * Double.parseDouble(cu.getString(heightColIndex)) - 5 * Double.parseDouble(cu.getString(ageColIndex)) - 161) * Double.parseDouble(cu.getString(activeColIndex)) * Double.parseDouble(cu.getString(decreaseColIndex));
                            }
                            Log.d(LOG_TAG, "calories1===================" + calories);
                            tvCalories.setText(String.valueOf(calories1));
                            //tvCalories.setText(String.valueOf(calories));
                        } while (cu.moveToNext());
                    }
                } else {
                    db1.delete("products1", null, null);
                    ContentValues cv = new ContentValues();
                    Log.d(LOG_TAG, "--- Insert in products: ---");
                    cv.put("date", formattedDate);
                    tvDate.setText(formattedDate);
                    long rowID = db.insert("calories", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                    ContentValues cv3 = new ContentValues();
                    cv3.put("calories", calories);
                    tvCalories.setText(String.valueOf(calories));
                    cv3.put("date", formattedDate);
                    cv3.put("proteins", 0);
                    cv3.put("fats", 0);
                    cv3.put("carbohydrates", 0);
                    db.update("calories", cv3, "date = ?",
                            new String[]{formattedDate});
                    if (cu.moveToFirst()) {
                        int weightColIndex = cu.getColumnIndex("weight");
                        int heightColIndex = cu.getColumnIndex("height");
                        int ageColIndex = cu.getColumnIndex("age");
                        do {
                            calories1 = 10 * Double.parseDouble(cu.getString(weightColIndex)) + 6.25 * Double.parseDouble(cu.getString(heightColIndex)) - 5 * Double.parseDouble(cu.getString(ageColIndex)) + 5;
                            tvCalories.setText(String.valueOf(calories1));
                            //tvCalories.setText(String.valueOf(calories));
                        } while (cu.moveToNext());
                    }

                }
            } else {
                Log.d(LOG_TAG, "0 rows");
            }

        } else {

            ContentValues cv = new ContentValues();
            Log.d(LOG_TAG, "--- Insert in products: ---");
            cv.put("date", formattedDate);
            tvDate.setText(formattedDate);
            long rowID = db.insert("calories", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            ContentValues cv3 = new ContentValues();
            cv3.put("calories", calories);
            tvCalories.setText(String.valueOf(calories));
            Log.d(LOG_TAG, "calories2===================" + calories);
            cv3.put("date", formattedDate);
            cv3.put("proteins", 0);
            cv3.put("fats", 0);
            cv3.put("carbohydrates", 0);
            db.update("calories", cv3, "date = ?",
                    new String[]{formattedDate});
            Intent intent7 = new Intent(this, Profile.class);
            startActivity(intent7);
        }


        System.out.println("Current time => " + ca.getTime());
        cu.close();

        Log.d(LOG_TAG, "--- Rows in profile: ---");
        Log.d(LOG_TAG, "result            -------------------------------   " + calories);


        Cursor c = db1.rawQuery("select * from calories where date = '" + (formattedDate) + "'", null);

        if (c.moveToFirst()) {
            int caloriesColIndex = c.getColumnIndex("calories");
            do {
                ContentValues cv4 = new ContentValues();
                calories = calories1;
                cv4.put("calories", calories);
                tvCalories.setText(String.valueOf(calories));
                db.update("calories", cv4, "date = ?",
                        new String[]{formattedDate});
                calories = Double.parseDouble(c.getString(caloriesColIndex));
                Log.d(LOG_TAG, "row inserted, ID = " + calories);
                Log.d(LOG_TAG, "calories3===================" + calories);
                tvDate.setText(formattedDate);
            } while (c.moveToNext());
        }

        c.close();


        ContentValues cv1 = new ContentValues();
        Intent intent5 = getIntent();

        Cursor c1 = db1.rawQuery("select * from calories where date = '" + (formattedDate) + "'", null);
        if (c1.moveToFirst()) {

            int proteinsColIndex = c1.getColumnIndex("proteins");
            int fatsColIndex = c1.getColumnIndex("fats");
            int carbohydratesColIndex = c1.getColumnIndex("carbohydrates");
            do {
                proteins = Double.parseDouble(c1.getString(proteinsColIndex));
                fats = Double.parseDouble(c1.getString(fatsColIndex));
                carbohydrates = Double.parseDouble(c1.getString(carbohydratesColIndex));
                Log.d(LOG_TAG, "row inserted, ID = " + calories);
                Log.d(LOG_TAG, "row inserted, ID = " + proteins);
                Log.d(LOG_TAG, "row inserted, ID = " + fats);
                Log.d(LOG_TAG, "row inserted, ID = " + carbohydrates);
                tvProteins.setText(String.valueOf(proteins));
                tvFats.setText(String.valueOf(fats));
                tvCarbohydrates.setText(String.valueOf(carbohydrates));

                c7 = db1.query("products1", null, null, null, null, null, null);

                String[] from = new String[]{"name", "calories", "proteins", "fats", "carbohydrates"};
                int[] to = new int[]{R.id.tvName, R.id.tvCalories, R.id.tvProteins, R.id.tvFats, R.id.tvCarbohydrates};

                scAdapter = new SimpleCursorAdapter(this, R.layout.item1, c7, from, to);
                scAdapter.notifyDataSetChanged();

                lvData1 = (ListView) findViewById(R.id.lvData1);
                lvData1.setAdapter(scAdapter);

                registerForContextMenu(lvData1);

                lvData1.setOnItemClickListener(this);
            } while (c1.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c1.close();


        if (intent5.getStringExtra("proteins") != null) {
            id = Double.valueOf(intent5.getStringExtra("id"));
            name = intent5.getStringExtra("name");
            proteins += Double.valueOf(intent5.getStringExtra("proteins"));
            cv1.put("proteins", proteins);
            tvProteins.setText(String.valueOf(proteins));

            fats += Double.valueOf(intent5.getStringExtra("fats"));
            cv1.put("fats", fats);
            tvFats.setText(String.valueOf(fats));

            carbohydrates += Double.valueOf(intent5.getStringExtra("carbohydrates"));
            cv1.put("carbohydrates", carbohydrates);
            tvCarbohydrates.setText(String.valueOf(carbohydrates));

            calories -= Double.valueOf(intent5.getStringExtra("calories"));
            Log.d(LOG_TAG, "calories5===================" + calories);
            cv1.put("calories", calories);
            tvCalories.setText(String.valueOf(calories));

            db.update("calories", cv1, "date = ?",
                    new String[]{formattedDate});


            Log.d(LOG_TAG,
                    "id======================= = " + id);

            ContentValues cv = new ContentValues();


            Log.d(LOG_TAG, "--- Insert in products: ---");

            cv.put("name", intent5.getStringExtra("name"));
            cv.put("calories", intent5.getStringExtra("calories"));
            cv.put("proteins", intent5.getStringExtra("proteins"));
            cv.put("fats", intent5.getStringExtra("fats"));
            cv.put("carbohydrates", intent5.getStringExtra("carbohydrates"));

            db.insert("products1", null, cv);

            c7 = db1.query("products1", null, null, null, null, null, null);

            String[] from = new String[]{"name", "calories", "proteins", "fats", "carbohydrates"};
            int[] to = new int[]{R.id.tvName, R.id.tvCalories, R.id.tvProteins, R.id.tvFats, R.id.tvCarbohydrates};

            scAdapter = new SimpleCursorAdapter(this, R.layout.item1, c7, from, to);
            scAdapter.notifyDataSetChanged();

            lvData1 = (ListView) findViewById(R.id.lvData1);
            lvData1.setAdapter(scAdapter);

            registerForContextMenu(lvData1);

            lvData1.setOnItemClickListener(this);

            dbHelper.close();
        }
        dbHelper.close();
        db.close();
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {

            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            SQLiteDatabase db1 = dbHelper.getWritableDatabase();
            ContentValues cv1 = new ContentValues();


            Cursor c = db1.rawQuery("select * from products1 where _id = " + (acmi.id), null);
            if (c.moveToFirst()) {

                int caloriesColIndex = c.getColumnIndex("calories");
                int proteinsColIndex = c.getColumnIndex("proteins");
                int fatsColIndex = c.getColumnIndex("fats");
                int carbohydratesColIndex = c.getColumnIndex("carbohydrates");

                do {
                    proteins -= Double.valueOf(c.getString(proteinsColIndex));
                    cv1.put("proteins", proteins);
                    tvProteins.setText(String.valueOf(proteins));

                    fats -= Double.valueOf(c.getString(fatsColIndex));
                    cv1.put("fats", fats);
                    tvFats.setText(String.valueOf(fats));

                    carbohydrates -= Double.valueOf(c.getString(carbohydratesColIndex));
                    cv1.put("carbohydrates", carbohydrates);
                    tvCarbohydrates.setText(String.valueOf(carbohydrates));

                    calories += Double.valueOf(c.getString(caloriesColIndex));
                    cv1.put("calories", calories);
                    tvCalories.setText(String.valueOf(calories));

                    db1.update("calories", cv1, "date = ?",
                            new String[]{formattedDate});
                } while (c.moveToNext());
            } else
                Log.d(LOG_TAG, "0 rows");
            c.close();


            db1.delete("products1", "_id = " + acmi.id, null);

            c7.requery();


            c7 = db1.query("products1", null, null, null, null, null, null);

            String[] from = new String[]{"name", "calories", "proteins", "fats", "carbohydrates"};
            int[] to = new int[]{R.id.tvName, R.id.tvCalories, R.id.tvProteins, R.id.tvFats, R.id.tvCarbohydrates};

            scAdapter = new SimpleCursorAdapter(this, R.layout.item1, c7, from, to);
            scAdapter.notifyDataSetChanged();

            lvData1 = (ListView) findViewById(R.id.lvData1);
            lvData1.setAdapter(scAdapter);

            registerForContextMenu(lvData1);

            lvData1.setOnItemClickListener(this);


            dbHelper.close();
            return true;
        }

        return super.onContextItemSelected(item);
    }


    public void onClickProfile(View view) {
        Intent intent6 = new Intent(this, Profile.class);
        startActivity(intent6);
    }

    public void onClickGraph(View view) {
        Intent intent7 = new Intent(this, Graph.class);
        intent7.putExtra("calorie", String.valueOf(calories1));

        startActivity(intent7);
    }

    public void onClickBreakfast(View view) {
        Intent intent7 = new Intent(this, Spysok.class);
        startActivity(intent7);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onClickGraphWeight(View view) {
        Intent intent7 = new Intent(this, GraphWeight.class);
        startActivity(intent7);
    }

    public void onClickDieta(View view) {
        Intent intent7 = new Intent(this, Dieta.class);
        startActivity(intent7);
    }
}