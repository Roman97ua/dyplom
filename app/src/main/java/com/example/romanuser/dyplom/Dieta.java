package com.example.romanuser.dyplom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dieta extends ActionBarActivity {
        private Toast mToastToShow;

        private EditText respText;
        private ListView listview;
        String[] ListElements = new String[] {};
        List<String> ListElements1 = new ArrayList<String>();
        List<String> ListElements2 = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        final List<String> ListElementsArrayList= new ArrayList<String>(Arrays.asList(ListElements));
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dieta);
            listview = (ListView) findViewById(R.id.mobile_list);

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (Dieta.this, android.R.layout.simple_list_item_1, ListElementsArrayList);
            listview.setAdapter(adapter);


            Button btnGo = (Button) findViewById(R.id.btnGo);
            respText = (EditText) findViewById(R.id.edtResp);
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String siteUrl = "http://bonduelle.ua/harchuvannja/dijety/";
                    (new ParseURL()).execute(new String[]{siteUrl});


                }
            });
            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mToastToShow=Toast.makeText(Dieta.this, ListElements1.get(position), Toast.LENGTH_LONG);
                    CountDownTimer toastCountDown;

                    int toastDurationInMilliSeconds = 1000000;

                    toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
                        public void onTick(long millisUntilFinished) {
                            mToastToShow.show();
                        }
                        public void onFinish() {
                            mToastToShow.cancel();
                        }
                    };

                    // Show the toast and starts the countdown
                    mToastToShow.show();
                    toastCountDown.start();
                    return false;
                }
            });
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent appInfo = new Intent(Dieta.this, DietaInfo.class);
                    appInfo.putExtra("link", ListElements2.get(position));
                    startActivity(appInfo);
                }
            });
        }







        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


private class ParseURL extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer buffer = new StringBuffer();
        try {
            Document doc = Jsoup.connect(strings[0]).get();

            Elements topicList = doc.select("div.title-text");
            Elements topicList3 = doc.select("a.modern-box-inner");
            buffer.append("Topic list\r\n");



            for (Element topic : topicList) {
                String data = topic.text();
                ListElementsArrayList.add(topic.text());
                buffer.append("Data [" + data + "] \r\n");
            }


            for (Element topic : topicList3) {
                String data = topic.attr("href");
                ListElements1.add(topic.text());
                ListElements2.add(data);
                buffer.append("A [" + data + "] \r\n");
            }

            adapter.notifyDataSetChanged();
        } catch (Throwable t) {

            t.printStackTrace();
        }

        return buffer.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
}
