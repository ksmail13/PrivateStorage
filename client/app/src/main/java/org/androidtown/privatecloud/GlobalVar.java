package org.androidtown.privatecloud;

import android.app.Application;


/**
 * Created by Sungyoon Kim on 2016-06-07.
 */


public class GlobalVar extends Application {

    private static String[] users = {"choi@uos.ac.kr","hong@uos.ac.kr","hwang@uos.ac.kr"};
    private static String[] passwords = {"1234","1234","1234"};

    private static boolean login = false;

    private static String user_name="";
    private static String user_id="";
    private static String user_dept="";


    public GlobalVar(String user_name, String user_id, String user_dept) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_dept = user_dept;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static String getProf_name(){
        return user_name;
    }

    public static String getProf_id(){
        return user_id;
    }

    public static String getProf_dept(){
        return user_dept;
    }

    public static String getUsers(int index) { return users[index]; }

    public static String getPasswords(int index) { return passwords[index]; }

    public static boolean getLogin() { return login; }
}

