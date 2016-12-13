package org.androidtown.privatecloud;

import android.app.Application;


/**
 * Created by Sungyoon Kim on 2016-06-07.
 */


public class GlobalVar extends Application {

    private static boolean login = false;

    private static String tokenType="";
    private static String accessToken="";
//    public final static String SERVER = "http://192.168.219.144:9999";
//    public final static String SERVER2 = "192.168.219.144";
    public final static String SERVER = "http://192.168.219.163:9999";
    public final static String SERVER2 = "192.168.219.163";

    public static String USERID, PASSWORD;


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static void setTokenType(String str) { tokenType = str; }

    public static void setAccessToken(String str) { accessToken = str; }

    public static String getTokenType() { return tokenType; }

    public static String getAccessToken() { return accessToken; }

    public static boolean getLogin() { return login; }
}

