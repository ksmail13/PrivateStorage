package org.androidtown.privatecloud.task;

import android.content.Context;
import android.util.Log;

import org.androidtown.privatecloud.GlobalVar;
import org.androidtown.privatecloud.model.FileResponseInfo;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by neverstop on 2016-12-13.
 */

public abstract class FileUploadTask extends FTPAsyncTask {

    String serverFilePath;
    File uploadFile;
    public FTPClient mFTPClient = null;

    public FileUploadTask(Context context) {
        super(context);
    }

    public void setServerFilePath(String path){ this.serverFilePath = path; }

    public void setUploadFile(File file){ this.uploadFile = file; }

    public abstract void onResult(Boolean r);

    public void execute() {
        this.execute(serverFilePath);
    }

    public void onError(String error, String errorMessage) {
        Log.e(TAG, "onError: " +error + errorMessage);
    }

    @Override
    protected void onPostExecute(Boolean r) {
        onResult(r);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result= false;
        FileResponseInfo response = requestFileUpload(strings[0]);
        if(response.getError() != null) {
            Log.e(TAG, response.getError());
            Log.e(TAG, response.getMessage());
            onError(response.getError(),response.getMessage());
            return false;
        }
        result = receiveFileByFtp(strings);
        completeSync(response.getWorkId());
        return result;
    }


    private boolean receiveFileByFtp(String... strings) {
        File LocalFile = uploadFile.getParentFile();
        boolean done = false;
        if (!LocalFile.exists())
            LocalFile.mkdir();
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            mFTPClient = new FTPClient();
            mFTPClient.setConnectTimeout(10 * 1000);
            // connecting to the host
            mFTPClient.connect(GlobalVar.SERVER2, 10000);
            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(GlobalVar.USERID, GlobalVar.PASSWORD);
                /*
                * Set File Transfer Mode
                * To avoid corruption issue you must specified a correct
                * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                * transferring text, image, and compressed files.
                */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                if(!uploadFile.exists()) {
                    return false;
                }
                inputStream = new FileInputStream(uploadFile);
                System.out.println("Start uploading " + strings[0]);

                done = mFTPClient.storeFile(new String(strings[0].getBytes("EUC-KR")), inputStream);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(mFTPClient != null) {
                try {
                    mFTPClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return done;
    }
}
