package org.androidtown.privatecloud.task;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.androidtown.privatecloud.model.HttpResponse;
import org.androidtown.privatecloud.util.HttpResponseException;
import org.androidtown.privatecloud.util.RequestUtils;

import java.lang.reflect.Type;
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
        HttpResponse result = RequestUtils.httpRequest((String)params[0], (String)params[1], (Map<String, String>)params[2], (Map<String, String>)params[3], TIMEOUT);

        return result;
    }




}

