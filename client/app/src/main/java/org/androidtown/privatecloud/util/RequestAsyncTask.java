package org.androidtown.privatecloud.util;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by neverstop on 2016-12-11.
 */

public abstract class RequestAsyncTask<R> extends AsyncTask<Object, Void, HttpResponse> {
    private static final String TAG = RequestAsyncTask.class.getSimpleName();
    public static final int TIMEOUT = 10000;

    private final Class<R> resultClass;
    private final Type resultType;
    private String method;
    private String url;
    private Map<String, String> header;
    private Map<String, String> body;

    public RequestAsyncTask(Class<R> type) {
        resultClass = type;
        this.resultType = null;
    }

    public RequestAsyncTask(Type type) {
        this.resultType = type;
        this.resultClass = null;
    }

    public RequestAsyncTask setMethod(String method) {
        this.method = method;

        return this;
    }

    public RequestAsyncTask setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestAsyncTask setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public RequestAsyncTask setBody(Map<String, String> body) {
        this.body = body;
        return this;
    }

    public void execute() {
        this.execute(method, url, header, body);
    }

    public void onError(int errorCode, String errorMessage, Throwable e) {
        Log.e(TAG, "onError: "+errorMessage, e);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(HttpResponse r) {
        if(r.responceCode >= 400) {
            onError(r.responceCode, r.resultMessage, new HttpResponseException(r.responceCode, r.resultMessage));
        } else {
            if(resultClass != null)
                onSuccess(new Gson().fromJson(r.resultMessage, resultClass));
            else
                onSuccess((R)new Gson().fromJson(r.resultMessage, resultType));
        }
    }

    public abstract void onSuccess(R result);

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(HttpResponse r) {
        super.onCancelled(r);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected HttpResponse doInBackground(Object... params) {
        HttpResponse result = request((String)params[0], (String)params[1], (Map<String, String>)params[2], (Map<String, String>)params[3]);

        return result;
    }

    private HttpResponse request(String method, String urlStr, Map<String, String> header, Map<String, String> requestMessage) {
        StringBuilder output = new StringBuilder();
        HttpResponse response = new HttpResponse();
        HttpURLConnection conn= null;
        try {
            StringBuilder data = new StringBuilder();
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                // request header
                for(Map.Entry<String, String> item : header.entrySet()) {
                    conn.setRequestProperty(item.getKey(), item.getValue());
                }

                if(requestMessage.size() > 0) {
                    // request body
                    for (Map.Entry<String, String> item : requestMessage.entrySet()) {
                        data.append(item.getKey()).append("=").append(item.getValue()).append("&");
                    }
                    data.deleteCharAt(data.length() - 1);
                }

                conn.setDoInput(true);
                conn.setConnectTimeout(TIMEOUT);
                conn.setRequestMethod(method);

                if(!method.toLowerCase().equals("get") && requestMessage != null) {
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.toString().getBytes("utf-8"));
                }
                int resCode = conn.getResponseCode();
                response.responceCode = resCode;
                receiveMessage(output, conn);
                response.resultMessage = output.toString();
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        } finally {
            if(conn!=null){
                conn.disconnect();
            }
        }

        return response;
    }

    private void receiveMessage(StringBuilder output, HttpURLConnection conn){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = null;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output.append(line).append("\n");
            }
        } catch(Exception e) {
            onError(-1, e.getMessage(), e);
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    onError(01, e.getMessage(), e);
                }
            }
        }

    }
}

class HttpResponse {
    public int responceCode;
    public String resultMessage;
}