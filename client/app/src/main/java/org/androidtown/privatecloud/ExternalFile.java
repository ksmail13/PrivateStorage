package org.androidtown.privatecloud;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.privatecloud.task.FileUploadTask;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neverstop on 2016-12-13.
 */

public class ExternalFile extends Activity implements AdapterView.OnItemClickListener{
    String mRoot = "";
    String mPath = "";
    TextView mTextMsg;
    ListView mListFile;
    ArrayList<String> mArFile;
    private FileUploadTask Upload;
    private static final String TAG = "ExternalFileList";

    private String uploadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.externalfilelist);
        uploadPath = getIntent().getStringExtra("uploadPath");

        // SD 카드가 장착되어 있지 않다면 앱 종료
        if( isSdCard() == false )
            finish();
        mTextMsg = (TextView)findViewById(R.id.textMessage);
        // SD 카드 루트 폴더의 경로를 구한다
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mTextMsg.setText(mRoot);
        String[] fileList = getFileList(mRoot);
        for(int i=0; i < fileList.length; i++)
            Log.d("tag", fileList[i]);
        // ListView 초기화
        initListView();
        fileList2Array(fileList);
    }

    // ListView 초기화
    public void initListView() {
        mArFile = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mArFile);

        mListFile = (ListView)findViewById(R.id.listFile);
        mListFile.setAdapter(adapter);
        mListFile.setOnItemClickListener(this);
    }

    // ListView 항목 선택 이벤트 함수
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        String strItem = mArFile.get(position);
        // 선택된 폴더의 전체 경로를 구한다
        String selectFilePath = getAbsolutePath(strItem);
        File selectFile = new File(selectFilePath);
        if(selectFile.isDirectory()) {
            // 선택된 폴더에 존재하는 파일 목록을 구한다
            String[] fileList = getFileList(selectFilePath);
            // 파일 목록을 ListView 에 표시
            fileList2Array(fileList);
        } else {
            final String curData = mArFile.get(position);

            File file = new File(selectFilePath);
            Upload = new FileUploadTask(this) {
                @Override
                public void onResult(Boolean r) {
                    if(r) {
                        Toast.makeText(getApplicationContext(), curData + "업로드 완료", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), curData + "업로드 실패", Toast.LENGTH_LONG).show();
                    }
                }

            };
            Upload.setServerFilePath(FilenameUtils.normalize(uploadPath+"/"+curData));
            Upload.setUploadFile(file);
            Upload.execute();
        }
    }

    // 폴더명을 받아서 전체 경로를 반환하는 함수
    public String getAbsolutePath(String strFolder) {
        String strPath;
        // 이전 폴더일때
        if( strFolder == ".." ) {
            // 전체 경로에서 최하위 폴더를 제거
            int pos = mPath.lastIndexOf("/");
            strPath = mPath.substring(0, pos);
        }
        else
            strPath = mPath + "/" + strFolder;
        return strPath;
    }

    // SD 카드 장착 여부를 반환
    public boolean isSdCard() {
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED) == false) {
            Toast.makeText(this, "SD Card does not exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 특정 폴더의 파일 목록을 구해서 반환
    public String[] getFileList(String strPath) {
        // 폴더 경로를 지정해서 File 객체 생성
        File fileRoot = new File(strPath);
        // 해당 경로가 폴더가 아니라면 함수 탈출
        if( fileRoot.isDirectory() == false )
            return null;
        mPath = strPath;
        mTextMsg.setText(mPath);
        // 파일 목록을 구한다
        String[] fileList = fileRoot.list();
        return fileList;
    }

    // 파일 목록을 ListView 에 표시
    public void fileList2Array(String[] fileList) {
        if( fileList == null )
            return;
        mArFile.clear();
        // 현재 선택된 폴더가 루트 폴더가 아니라면
        if( mRoot.length() < mPath.length() )
            // 이전 폴더로 이동하기 위해서 ListView 에 ".." 항목을 추가
            mArFile.add("..");

        for(int i=0; i < fileList.length; i++) {
            Log.d("tag", fileList[i]);
            mArFile.add(fileList[i]);
        }
        ArrayAdapter adapter = (ArrayAdapter)mListFile.getAdapter();
        adapter.notifyDataSetChanged();
    }
}