package com.example.romanuser.dyplom;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by RomanUser on 21.10.2017.
 */
public class Product extends Activity{

    Button btnAdd;
    EditText etName, etCalories, etProteins, etFats, etCarbohydrates;

    Profile.DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        etName = (EditText) findViewById(R.id.etName);
        etCalories = (EditText) findViewById(R.id.etCalories);
        etProteins = (EditText) findViewById(R.id.etProteins);
        etFats = (EditText) findViewById(R.id.etFats);
        etCarbohydrates = (EditText) findViewById(R.id.etCarbohydrates);

        dbHelper = new Profile.DBHelper(this);

    }

    public void onClickAdd(View view) {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String calories = etCalories.getText().toString();
        String proteins = etProteins.getText().toString();
        String fats = etFats.getText().toString();
        String carbohydrates = etCarbohydrates.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("name", name);
        cv.put("calories", calories);
        cv.put("proteins", proteins);
        cv.put("fats", fats);
        cv.put("carbohydrates", carbohydrates);

        db.insert("products", null, cv);
    }
}