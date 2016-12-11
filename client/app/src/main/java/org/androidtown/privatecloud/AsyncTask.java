package org.androidtown.privatecloud;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by neverstop on 2016-12-10.
 */

public class AsyncTask extends AppCompatActivity implements View.OnClickListener{
    BackgroundTask task;
    Button requestBtn;
    TextView url;
    String address, result;
    TextView textView, tv;

    public static String defaultUrl = "http://m.naver.com";
    Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynk);

        textView = (TextView) findViewById(R.id.textView01);
        requestBtn = (Button) findViewById(R.id.requestBtn);
        url = (TextView) findViewById(R.id.url);
        tv = (TextView) findViewById(R.id.txtMsg);
        address = ""; result = "";

        requestBtn.setOnClickListener(this);
    }

    class BackgroundTask extends android.os.AsyncTask<Integer, Integer, String>{
        protected void onPreExecute(){
            address = url.getText().toString();
        }

        protected String doInBackground(Integer ... integers) {
            result = request(address);
            return result;
        }

        protected void onPostExecute(String a){
            Log.d("", "onPostExecute: "+a);
            tv.setText(a.trim());
            textView.setText("Finished.");
        }
        protected void onCancelled(){
            textView.setText("Cancelled.");
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        task = new BackgroundTask();
        task.execute();
    }

    private String request(String urlStr) {
        StringBuilder output = new StringBuilder();
        HttpURLConnection conn= null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.getHeaderFields();

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                } else if (resCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                }
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        } finally {
            if(conn!=null){
                conn.disconnect();
            }
        }

        return output.toString();
    }
}
