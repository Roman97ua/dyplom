package com.example.romanuser.dyplom;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Spysok extends Activity implements AdapterView.OnItemClickListener {
    final String LOG_TAG = "myLogs";
    private static final int CM_DELETE_ID = 1;
    ListView lvData;
    DB db;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    Button btnProfile;
    Profile.DBHelper dbHelper;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spysok);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        db = new DB(this);
        db.open();
        dbHelper = new Profile.DBHelper(this);
        cursor = db.getAllData();
        startManagingCursor(cursor);

        String[] from = new String[] { DB.COLUMN_NAME, DB.COLUMN_CALORIES, DB.COLUMN_PROTEINS, DB.COLUMN_FATS, DB.COLUMN_CARBOHYDRATES};
        int[] to = new int[] { R.id.tvName, R.id.tvCalories, R.id.tvProteins, R.id.tvFats, R.id.tvCarbohydrates };

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        scAdapter.notifyDataSetChanged();
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        registerForContextMenu(lvData);

        lvData.setOnItemClickListener(this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            db.delRec(acmi.id);
            cursor.requery();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("select * from products where _id = " + (position+1), null);
        Intent intent9 = new Intent(this, Weight.class);
        if (c.moveToFirst()) {

            int caloriesColIndex = c.getColumnIndex("calories");

            do {
                intent9.putExtra("calories", c.getString(caloriesColIndex));

            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        dbHelper.close();
        String posIntent = String.valueOf(position);
        intent9.putExtra("position", posIntent);
        startActivity(intent9);
    }

    public void onClickProduct(View view) {
        Intent intent10 = new Intent(this, Product.class);
        startActivity(intent10);
    }
}
