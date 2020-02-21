package com.example.romanuser.dyplom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DietaInfo extends ActionBarActivity {
    private static final String TAG = "DietaInfo";
    private Toast mToastToShow;

    private EditText respText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        Button btnGo = (Button) findViewById(R.id.btnGo);
        respText = (EditText) findViewById(R.id.edtResp);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siteUrl = getIntent().getStringExtra("link");
                Log.i(TAG, siteUrl);
                (new DietaInfo.ParseURL1()).execute(new String[]{siteUrl});


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


    private class ParseURL1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                Document doc = Jsoup.connect(strings[0]).get();

                Elements topicList = doc.select("h2");
                Elements topicList2 = doc.select("a.modern-box-inner");
                Elements topicList3 = doc.select("p");


                for (Element topic : topicList3) {
                    String data = topic.text();
                    Log.i(TAG, data);
                    if((topic.text().startsWith("ПЕРШИЙ ДЕНЬ"))||(topic.text().startsWith("Перший сніданок"))||(topic.text().startsWith("Другий сніданок"))||(topic.text().startsWith("ДРУГИЙ ДЕНЬ"))||(topic.text().startsWith("ТРЕТІЙ ДЕНЬ"))||
                            (topic.text().startsWith("I сніданок"))||(topic.text().startsWith("II сніданок"))||(topic.text().startsWith("Сніданок"))||(topic.text().startsWith("Обід"))||(topic.text().startsWith("Підвечірок"))||(topic.text().startsWith("Вечеря"))) {
                        StringBuffer sb = new StringBuffer();
                        Pattern p = Pattern.compile("[А-Я]");
                        Matcher m = p.matcher(topic.text());
                        m.appendTail(sb);
                        int i=0;
                        while(m.find()) {
                            sb.insert(m.start()+i, "\n");
                            i++;
                        }
                        buffer.append(sb.toString());
                    }
                }
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
            respText.setText(s);
        }
    }
}