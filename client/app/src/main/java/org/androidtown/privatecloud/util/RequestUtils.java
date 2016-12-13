package org.androidtown.privatecloud.util;

import android.util.Log;

import com.google.gson.Gson;

import org.androidtown.privatecloud.model.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by neverstop on 2016-12-13.
 */

public class RequestUtils {


    public static HttpResponse httpRequest(String method, String urlStr, Map<String, String> header, Object requestMessage, int timeout) {
        //urlStr =urlStr;
        StringBuilder output = new StringBuilder();
        HttpResponse response = new HttpResponse();
        HttpURLConnection conn= null;
        try {
            StringBuilder data = new StringBuilder();
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                // httpRequest header
                for(Map.Entry<String, String> item : header.entrySet()) {
                    conn.setRequestProperty(item.getKey(), item.getValue());
                }
                if(requestMessage instanceof Map) {
                    Map<String, String> messageBody = (Map<String, String>) requestMessage;
                    if (messageBody.size() > 0) {
                        // httpRequest body
                        for (Map.Entry<String, String> item : messageBody.entrySet()) {
                            data.append(item.getKey()).append("=").append(item.getValue()).append("&");
                        }
                        data.deleteCharAt(data.length() - 1);
                    }
                } else {
                    data.append(new Gson().toJson(requestMessage));
                }

                conn.setDoInput(true);
                conn.setConnectTimeout(timeout);
                conn.setRequestMethod(method);

                if(!method.toLowerCase().equals("get") && requestMessage != null) {
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(data.toString().getBytes("utf-8"));
                }
                int resCode = conn.getResponseCode();
                response.responceCode = resCode;
                receiveMessage(output, resCode >= 400? conn.getErrorStream() : conn.getInputStream());
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

    private static void receiveMessage(StringBuilder output, InputStream input){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input));

            String line = null;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                output.append(line).append("\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
