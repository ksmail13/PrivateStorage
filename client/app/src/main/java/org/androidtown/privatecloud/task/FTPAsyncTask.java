package org.androidtown.privatecloud.task;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.androidtown.privatecloud.GlobalVar;
import org.androidtown.privatecloud.model.FileRequestInfo;
import org.androidtown.privatecloud.model.FileRequestType;
import org.androidtown.privatecloud.model.FileResponseInfo;
import org.androidtown.privatecloud.model.HttpResponse;
import org.androidtown.privatecloud.util.RequestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by neverstop on 2016-12-12.
 */

public abstract class FTPAsyncTask extends AsyncTask<String , Void, Boolean> {

    protected static final String TAG = FTPAsyncTask.class.getSimpleName();
    private FTPClient mFTPClient = null;
    public String host, username, password, portStr;

    private static final int TIMEOUT = 10000;
    private Context context;
    FTPAsyncTask(Context context) {
        this.context = context;
    }

    protected void completeSync(String workID) {
        Map<String, String> header = createRequestHeader();

        HttpResponse response = RequestUtils.httpRequest("PUT", GlobalVar.SERVER+"/file/complete?id="+workID, header, null, TIMEOUT);
        if(response.responceCode == 204) Log.d(TAG, "completeSync");
    }

    protected FileResponseInfo requestFileDownload(String path) {
        Map<String, String> header = createRequestHeader();

        HttpResponse response = null;
        try {
            response = RequestUtils.httpRequest("GET", GlobalVar.SERVER+"/file?path="+ URLEncoder.encode(path,"UTF-8"), header, null, TIMEOUT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FileResponseInfo responseInfo = new Gson().fromJson(response.resultMessage, FileResponseInfo.class);
        return responseInfo;
    }

    protected FileResponseInfo requestFileUpload(String path) {
        Map<String, String> header = createRequestHeader();

        FileRequestInfo requestInfo = new FileRequestInfo();
        requestInfo.setDirectory(FilenameUtils.getFullPathNoEndSeparator(path));
        requestInfo.setFilename(FilenameUtils.getName(path));
        requestInfo.setType(FileRequestType.CREATE);

        HttpResponse response = RequestUtils.httpRequest("PUT", GlobalVar.SERVER+"/file", header, requestInfo, TIMEOUT);
        FileResponseInfo responseInfo = new Gson().fromJson(response.resultMessage, FileResponseInfo.class);
        return responseInfo;
    }

    protected FileResponseInfo requestFileUpdate(String path) {
        Map<String, String> header = createRequestHeader();

        FileRequestInfo requestInfo = new FileRequestInfo();
        requestInfo.setDirectory(FilenameUtils.getFullPathNoEndSeparator(path));
        requestInfo.setFilename(FilenameUtils.getName(path));
        requestInfo.setType(FileRequestType.UPDATE);

        HttpResponse response = RequestUtils.httpRequest("PUT", GlobalVar.SERVER+"/file", header, requestInfo, TIMEOUT);
        FileResponseInfo responseInfo = new Gson().fromJson(response.resultMessage, FileResponseInfo.class);
        return responseInfo;
    }

    protected FileResponseInfo requestFileDelete(String path) {
        Map<String, String> header = createRequestHeader();

        FileRequestInfo requestInfo = new FileRequestInfo();
        requestInfo.setDirectory(FilenameUtils.getFullPathNoEndSeparator(path));
        requestInfo.setFilename(FilenameUtils.getName(path));
        requestInfo.setType(FileRequestType.DELETE);

        HttpResponse response = RequestUtils.httpRequest("PUT", GlobalVar.SERVER+"/file", header, requestInfo, TIMEOUT);
        FileResponseInfo responseInfo = new Gson().fromJson(response.resultMessage, FileResponseInfo.class);
        return responseInfo;
    }

    @NonNull
    private Map<String, String> createRequestHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("authorization", context.getSharedPreferences("Private", MODE_PRIVATE).getString("access_token", ""));
        header.put("Content-Type", "application/json");
        return header;
    }

    public void execute() {
        this.execute(host, username, password, portStr);
    }

    public void onError(int errorCode, String errorMessage, Throwable e) {
        Log.e(TAG, "onError: "+errorMessage, e);
    }


    public abstract void onResult(Boolean r);

    @Override
    protected void onPostExecute(Boolean r) {
        onResult(r);
    }


    public FTPAsyncTask setHost(String h){
        this.host = h;
        return this;
    }

    public FTPAsyncTask setPort(String p){
        portStr = p;
        return this;
    }

    public FTPAsyncTask setUsername(String u){
        username = u;
        return this;
    }

    public FTPAsyncTask setPassword(String p){
        password = p;
        return this;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        int port = Integer.parseInt(strings[3].toString());

        try {
            mFTPClient = new FTPClient();
            mFTPClient.setConnectTimeout(10 * 1000);
            // connecting to the host
            mFTPClient.connect(this.host, 10000);
            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(this.username, this.password);
                /*
                * Set File Transfer Mode
                * To avoid corruption issue you must specified a correct
                * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                * transferring text, image, and compressed files.
                */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                return status;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(mFTPClient != null) {
                try {
                    mFTPClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return false;
    }
}
