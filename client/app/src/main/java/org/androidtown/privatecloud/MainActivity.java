package org.androidtown.privatecloud;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.androidtown.privatecloud.model.FileInfo;
import org.androidtown.privatecloud.model.FileType;
import org.androidtown.privatecloud.task.FTPAsyncTask;
import org.androidtown.privatecloud.task.FileDownloadTask;
import org.androidtown.privatecloud.task.FileUploadTask;
import org.androidtown.privatecloud.task.RequestAsyncTask;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int check=0;
    private static final String TAG = "Main";
    private IconTextGridAdapter adapter;
    private GridView gridview;
    private String access_token, login_id, login_password;
    List<FileInfo> fileList = new ArrayList<FileInfo>();
    private int syncCheck = 0;
    public static FTPAsyncTask ftpNet;
    private FileDownloadTask Download;
    private FileUploadTask Upload;
    private String[] Type;
    private int presentFolderIndex = 0;
    private String folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        folder  = getIntent().getStringExtra("folder");
        gridview = (GridView) findViewById(R.id.gridview);
        registerForContextMenu(gridview);

        SharedPreferences preferences = getSharedPreferences("Private", MODE_PRIVATE);
        access_token = preferences.getString("access_token", "");
        login_id = preferences.getString("login_id", "");
        login_password = preferences.getString("login_password", "");
        GlobalVar.USERID = login_id;
        GlobalVar.PASSWORD = login_password;
//        ftpNet = new FTPAsyncTask() {
//            @Override
//            public void onResult(Boolean result) {
//                if(result == Boolean.TRUE){
//                    Toast.makeText(getApplicationContext(), "FTP 로그인 완료", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        ftpNet.setHost(GlobalVar.SERVER2).setUsername(login_id).setPassword(login_password).setPort("9999");
//        ftpNet.execute();

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadFileList();
    }

    // ip:port/file/{폴더이름}
    public void setAdapter() {
        adapter = new IconTextGridAdapter(this);
        Type = new String[fileList.size()];

        Resources res = getResources();
        if (fileList != null) {
            for (int i = 0; i < fileList.size(); i++) {
                FileType type = fileList.get(i).getType();
                String thumbnail = fileList.get(i).getThumbnail();
                if (thumbnail != null) {

                    adapter.addItem(new IconTextItem(null, fileList.get(i).getName(), GlobalVar.SERVER+"/thumbnail/"+thumbnail));

                } else {
                    if (type == FileType.DIRECTORY) {
                        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.foldericon), fileList.get(i).getName()));
                    } else {
                        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.notepadicon), fileList.get(i).getName()));
                    }
                }
                Type[i] = type.toString();
            }
        } else {
            Toast.makeText(getApplicationContext(), "파일이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }

        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                IconTextItem curItem = (IconTextItem) adapter.getItem(position);
                final String curData = curItem.getData();

                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "private/"+getFolder());

                if(!directory.exists()) {
                    if (!directory.mkdirs()) {
                        Log.e(TAG, "Directory not created");
                    }
                }
                File file = new File(directory, curData);
                String realType = FilenameUtils.getExtension(file.getName());

                Toast.makeText(getApplicationContext(), "Selected : " + curData, Toast.LENGTH_LONG).show();

                if ("FILE".equals(Type[position])) {
                    if (!file.exists()) {
                        Download = new FileDownloadTask(MainActivity.this) {
                            @Override
                            public void onResult(Boolean r) {
                                Toast.makeText(getApplicationContext(), curData + " 다운로드 완료", Toast.LENGTH_LONG).show();
                            }

                        };
                        Download.setServerFilePath(curData);
                        Download.setDownloadFile(file);
                        Download.execute();
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        if (realType.equalsIgnoreCase("mp3")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "audio/*");
                        } else if (realType.equalsIgnoreCase("mp4")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "vidio/*");
                        } else if (realType.equalsIgnoreCase("jpg")
                                || realType.equalsIgnoreCase("jpeg")
                                || realType.equalsIgnoreCase("gif")
                                || realType.equalsIgnoreCase("png")
                                || realType.equalsIgnoreCase("bmp")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "image/*");
                        } else if (realType.equalsIgnoreCase("txt")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "text/*");
                        } else if (realType.equalsIgnoreCase("doc")
                                || realType.equalsIgnoreCase("docx")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/msword");
                        } else if (realType.equalsIgnoreCase("xls")
                                || realType.equalsIgnoreCase("xlsx")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/vnd.ms-excel");
                        } else if (realType.equalsIgnoreCase("ppt")
                                || realType.equalsIgnoreCase("pptx")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/vnd.ms-powerpoint");
                        } else if (realType.equalsIgnoreCase("pdf")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/pdf");
                        } else if (realType.equalsIgnoreCase("hwp")) {
                            intent.setDataAndType(Uri.parse("file://" + file.getPath()), "application/haansofthwp");
                        }
                        startActivity(intent);
                    }
                } else if ("DIRECTORY".equals(Type[position])) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("folder", getFolder() + "/" + curData);
                    startActivity(i);
                }

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
            ReadFileList();
        } else if( id == R.id.externalFileList) {
            Intent intent = new Intent(getApplicationContext(), ExternalFile.class);
            intent.putExtra("uploadPath", getFolder());
            startActivity(intent);
        } else if (id == R.id.logout) {
            SharedPreferences prefs = getSharedPreferences("Private", MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("login", false);
            edit.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ReadFileList() {
        RequestAsyncTask<List<FileInfo>> request = new RequestAsyncTask<List<FileInfo>>(new TypeToken<List<FileInfo>>() {
        }.getType()) {
            @Override
            public void onSuccess(List<FileInfo> result) {
                if (result == null) return;
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

        request.setMethod("GET").setUrl(GlobalVar.SERVER + "/file/"+getFolder()+"/list").setBody(body).setHeader(header);

        request.execute();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public String getFolder (){
        return folder == null?"":folder;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = (int) info.id;
        IconTextItem curItem = (IconTextItem) adapter.getItem(index);
        final String curData = curItem.getData();

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), "private/"+getFolder());

        if(!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
        }
        File file = new File(directory, curData);

        switch (item.getItemId()) {
            case R.id.id_delete:
                return true;
            case R.id.id_upload:
                if (file.exists()) {
                    Upload = new FileUploadTask(this) {
                        @Override
                        public void onResult(Boolean r) {
                            Toast.makeText(getApplicationContext(), curData + "업로드 완료", Toast.LENGTH_LONG).show();
                        }

                    };
                    Upload.setServerFilePath(curData);
                    Upload.setUploadFile(file);
                    Upload.execute();
                } else {
                    Toast.makeText(getApplicationContext(), curData + "다운로드 되지 않은 파일", Toast.LENGTH_LONG).show();
                }
            default:
                return super.onContextItemSelected(item);
        }
    }
}