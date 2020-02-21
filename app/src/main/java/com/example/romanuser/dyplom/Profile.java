package com.example.romanuser.dyplom;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * Created by RomanUser on 21.10.2017.
 */

public class Profile extends Activity {
    EditText etHeight;
    EditText etWeight;
    double active;
    double decrease;
    EditText etDate;
    String sex;
    DBHelper dbHelper;
    Button btnSave;
    String[] data = {"Мінімальна активність", "Слабка активність", "Середня активність", "Висока активність", "Екстра-активність"};
    String[] data1 = {"Набрати вагу", "Зберегти вагу", "Похудати"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Активність");
        spinner.setSelection(2);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) active=1.2;
                if(position==1) active=1.375;
                if(position==2) active=1.55;
                if(position==3) active=1.725;
                if(position==4) active=1.9;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter1);
        spinner1.setPrompt("Ціль");
        spinner1.setSelection(1);
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position==0) decrease=1.2;
                if(position==1) decrease=1;
                if(position==2) decrease=0.8;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RadioButton manRadioButton = (RadioButton)findViewById(R.id.rbM);
        manRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton womanRadioButton = (RadioButton)findViewById(R.id.rbW);
        womanRadioButton.setOnClickListener(radioButtonClickListener);

        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etDate = (EditText) findViewById(R.id.etDate);

        btnSave = (Button) findViewById(R.id.btnSave);
        dbHelper = new DBHelper(this);

    }
    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.rbM: sex="Man";
                    break;
                case R.id.rbW: sex="Woman";
                    break;

                default:
                    break;
            }
        }
    };
    public void onClickSave(View view) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        String weight = etWeight.getText().toString();
        String height = etHeight.getText().toString();
        String age = etDate.getText().toString();

        cv.put("weight", weight);
        cv.put("height", height);
        cv.put("age", age);
        cv.put("active", active);
        cv.put("sex", sex);
        cv.put("decrease", decrease);
        Cursor c = db.query("profile", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            db.update("profile", cv, "id = ?",
                    new String[]{"1"});
        } else {
            db.insert("profile", null, cv);
        }
        Intent intent8 = new Intent(this, Plan.class);
        startActivity(intent8);
    }

    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {

            super(context, "mydb", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table profile ("
                    + "id integer primary key autoincrement,"
                    + "weight numeric not null,"
                    + "height numeric not null,"
                    + "active numeric not null,"
                    + "decrease numeric not null,"
                    + "sex text not null,"
                    + "age numeric not null" + ");");

            db.execSQL("create table calories ("
                    + "date text primary key,"
                    + "calories numeric,"
                    + "proteins numeric,"
                    + "fats numeric,"
                    + "carbohydrates numeric" + ");");
            db.execSQL("create table products ("
                    + "_id integer primary key autoincrement,"
                    + "name text,"
                    + "calories numeric not null,"
                    + "proteins numeric not null,"
                    + "fats numeric not null,"
                    + "carbohydrates numeric not null" + ");");
            db.execSQL("create table products1 ("
                    + "_id integer primary key autoincrement,"
                    + "name text,"
                    + "calories numeric not null,"
                    + "proteins numeric not null,"
                    + "fats numeric not null,"
                    + "carbohydrates numeric not null" + ");");
            db.execSQL("create table weight ("
                    + "date text primary key,"
                    + "weight numeric not null" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
