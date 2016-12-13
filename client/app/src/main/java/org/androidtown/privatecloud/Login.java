
package org.androidtown.privatecloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;

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
    }

    public void onLoginClicked(View v) {
        login();
    }

    public void login() {

        if (validate() == 0) {
            //this.setResult(Activity.RESULT_OK);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (validate() == 1) {
            Toast.makeText(getApplicationContext(), "아이디 확인하세요", Toast.LENGTH_LONG).show();}
        else if (validate() == 2) {
            Toast.makeText(getApplicationContext(), "패스워드 확인하세요", Toast.LENGTH_LONG).show();}
    }

    public int validate() {


        int check= 1;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("올바른 이메일 주소를 입력하세요");
        } else {
            int selected = 0;
            for (int i = 0; i < 3; i++)
                if (email.equals(GlobalVar.getUsers(i))) {
                    check= 0;
                    selected = i;
                }

            if (check==0) {
                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    passwordText.setError("4-10 사이 비밀번호를 입력하세요");
                    check = 2;
                } else {
                    if (password.equals(GlobalVar.getPasswords(selected))) {
                        check = 0;
                    } else {
                        passwordText.setError("비밀번호가 맞지 않습니다.");
                        check = 2;
                    }
                }
            }
            else
                emailText.setError("가입된 이메일 주소가 아닙니다.");
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
}