package com.example.romanuser.dyplom;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by RomanUser on 21.10.2017.
 */

public class Weight extends Activity {
    Profile.DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    TextView tvCalories,tvCarbohydrates,tvFats,tvProteins,tvName;
    EditText etWeight;
    Intent intent14;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight);

        tvCalories = (TextView) findViewById(R.id.tvCalories);
        tvCarbohydrates = (TextView) findViewById(R.id.tvCarbohydrates);
        tvFats = (TextView) findViewById(R.id.tvFats);
        tvProteins = (TextView) findViewById(R.id.tvProteins);
        tvName = (TextView) findViewById(R.id.tvName);
        etWeight=(EditText) findViewById(R.id.editText2);
        dbHelper = new Profile.DBHelper(this);
    }

    public void onClickWeight(View view) {
        startActivity(intent14);
    }

    public void onClickSave(View view) {
        Intent intent12 = getIntent();
        int id = Integer.parseInt(intent12.getStringExtra("position"));

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from products where _id = " + (id+1), null);
        intent14 = new Intent(this, Plan.class);
        if (c.moveToFirst()) {

            int nameColIndex = c.getColumnIndex("name");
            int caloriesColIndex = c.getColumnIndex("calories");
            int proteinsColIndex = c.getColumnIndex("proteins");
            int fatsColIndex = c.getColumnIndex("fats");
            int carbohydratesColIndex = c.getColumnIndex("carbohydrates");

            do {
                int calories=(Integer.parseInt(etWeight.getText().toString())*c.getInt(caloriesColIndex))/100;
                int proteins=(Integer.parseInt(etWeight.getText().toString())*c.getInt(proteinsColIndex))/100;
                int fats=(Integer.parseInt(etWeight.getText().toString())*c.getInt(fatsColIndex))/100;
                int carbohydrates=(Integer.parseInt(etWeight.getText().toString())*c.getInt(carbohydratesColIndex))/100;
                tvCalories.setText(String.valueOf(calories));
                tvProteins.setText(String.valueOf(proteins));
                tvFats.setText(String.valueOf(fats));
                tvCarbohydrates.setText(String.valueOf(carbohydrates));
                tvName.setText(c.getString(nameColIndex));

                intent14.putExtra("id", String.valueOf(id));
                intent14.putExtra("name", c.getString(nameColIndex));
                intent14.putExtra("calories", String.valueOf(calories));
                intent14.putExtra("proteins", String.valueOf(proteins));
                intent14.putExtra("fats", String.valueOf(fats));
                intent14.putExtra("carbohydrates", String.valueOf(carbohydrates));

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        dbHelper.close();
    }
}
