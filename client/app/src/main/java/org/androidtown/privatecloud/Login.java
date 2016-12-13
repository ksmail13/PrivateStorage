
package org.androidtown.privatecloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.androidtown.privatecloud.model.LoginResponse;
import org.androidtown.privatecloud.task.RequestAsyncTask;

import java.util.HashMap;
import java.util.Map;



public class Login extends AppCompatActivity {

        private static final String TAG = "Login";
        private static final int REQUEST_SIGNUP = 0;
        public static final int REQUEST_CODE_ANOTHER = 1001;

        Button loginButton;
        TextView emailText;
        TextView passwordText;
        String user_id;

        GlobalVar globalVar;
        /**
         * ATTENTION: This was auto-generated to implement the App Indexing API.
         * See https://g.co/AppIndexing/AndroidStudio for more information.
         */
        private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = (Button) findViewById(R.id.Blogin);
        emailText = (TextView) findViewById(R.id.email);
        passwordText = (TextView) findViewById(R.id.password);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        SharedPreferences preferences = getSharedPreferences("Private", MODE_PRIVATE);
        if(preferences.getBoolean("login", false)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onLoginClicked(View v) {
        login();
    }

    public void login() {
        int validateResult =validate();
        //this.setResult(Activity.RESULT_OK);
        RequestAsyncTask<LoginResponse> request = new RequestAsyncTask<LoginResponse>(LoginResponse.class) {
            @Override
            public void onSuccess(LoginResponse result) {
                Log.d("Login result", result.toString());
                SharedPreferences prefs = getSharedPreferences("Private", MODE_PRIVATE);
                SharedPreferences.Editor edit =  prefs.edit();
                edit.putString("access_token", result.getToken_type()+" "+result.getAccess_token());
                edit.putString("login_id",emailText.getText().toString());
                edit.putString("login_password",passwordText.getText().toString());
                edit.putBoolean("login", true);
                edit.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            public void onError(int errorCode, String errorMessage, Throwable e) {
                if(errorCode == 400 ) {
                    Log.e(TAG, "onError: " + errorMessage, e);
                    Toast.makeText(getApplicationContext(), "아이디/패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        };


        if (validateResult == 0) {
            Map<String, String> header = new HashMap<String, String>();
            header.put("authorization", Base64.encodeToString("privateId:privateSecret".getBytes(), Base64.NO_WRAP));
            //header.put("authorization", token_type+" "+access_token);
            header.put("content-type", "application/x-www-form-urlencoded");

            Map<String, String> body = new HashMap<String, String>();
            body.put("grant_type", "password");
            body.put("client_id", "privateId");
            body.put("client_secret", "privateSecret");
            body.put("username", emailText.getText().toString());
            body.put("password", passwordText.getText().toString());
            body.put("scope", "read write delete");


            request.setMethod("POST").setUrl(GlobalVar.SERVER + "/oauth/token").setBody(body).setHeader(header);
            request.execute();

        }
        else if (validateResult == 1) {
            Toast.makeText(getApplicationContext(), "아이디를 확인하세요.", Toast.LENGTH_LONG).show();}
        else if (validateResult == 2) {
            Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();}
    }

    public int validate() {


        int check= 1;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() ) {
            emailText.setError("아이디를 입력해주세요.");
        } else {
            check = 0;
            if (check==0) {
                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    passwordText.setError("4-10 사이 비밀번호를 입력하세요");
                    check = 2;
                } else {
                    check = 0;
                }
            }
        }
        return check;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_ANOTHER) {
            Toast toast = Toast.makeText(getApplicationContext(), "onActivityResult 메소드가 호출됨. 요청코드 :" + requestCode + ", 결과코드 : " + resultCode,Toast.LENGTH_LONG);
            toast.show();
        }
    }
}