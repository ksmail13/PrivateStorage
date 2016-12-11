package org.androidtown.privatecloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.androidtown.privatecloud.model.FileInfo;
import org.androidtown.privatecloud.model.FileType;
import org.androidtown.privatecloud.util.RequestAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private IconTextGridAdapter adapter;
    private GridView gridview ;
    private String access_token;
    List<FileInfo> fileList = new ArrayList<FileInfo>();
    private int syncCheck = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);

        SharedPreferences preferences = getSharedPreferences("Private", MODE_PRIVATE);
        access_token = preferences.getString("access_token", "");

        ConnectServer();
    }
    // ip:port/file/{폴더이름}
    public void setAdapter(){
        adapter = new IconTextGridAdapter(this);

        Resources res = getResources();
        if(fileList != null) {
            for (int i = 0; i < fileList.size(); i++) {
                FileType type = fileList.get(i).getType();
                String thumbnail = fileList.get(i).getThumbnail();
                if (thumbnail != null) {
                    byte[] decodedString = Base64.decode(thumbnail, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    BitmapDrawable bd = new BitmapDrawable(res, decodedByte);
                    adapter.addItem(new IconTextItem(bd, fileList.get(i).getName()));
                } else {
                    if (type == FileType.DIRECTORY) {
                        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.foldericon), fileList.get(i).getName()));
                    } else{
                        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.notepadicon), fileList.get(i).getName()));
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "파일이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }

        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) adapter.getItem(position);
                String curData = curItem.getData();
                Toast.makeText(getApplicationContext(), "Selected : " + curData, Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.syncronization) {
            ConnectServer();
        } else if( id == R.id.logout){
            SharedPreferences prefs = getSharedPreferences("Private", MODE_PRIVATE);
            SharedPreferences.Editor edit =  prefs.edit();
            edit.putBoolean("login", false);
            edit.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ConnectServer(){
        RequestAsyncTask<List<FileInfo>> request = new RequestAsyncTask<List<FileInfo>>(new TypeToken<List<FileInfo>>(){}.getType()){
            @Override
            public void onSuccess(List<FileInfo> result) {
                if(result == null) return;
                Log.d("File sync result", result.toString());
                fileList = result;
                setAdapter();
            }

            public void onError(int errorCode, String errorMessage, Throwable e) {

                Log.e(TAG, "onError: " + errorMessage, e);
                Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_LONG).show();

            }
        };

        Map<String, String> header = new HashMap<String, String>();
        header.put("authorization", access_token);
        header.put("content-type", "application/json");

        Map<String, String> body = new HashMap<String, String>();


        request.setMethod("GET").setUrl(GlobalVar.SERVER + "/file").setBody(body).setHeader(header);
        request.execute();
    }
}
