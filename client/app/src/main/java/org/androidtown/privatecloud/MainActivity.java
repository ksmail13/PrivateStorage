package org.androidtown.privatecloud;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IconTextGridAdapter adapter;
    private GridView gridview ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);
        setAdapter();
    }

    public void setAdapter(){
        adapter = new IconTextGridAdapter(this);

        Resources res = getResources();
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.bkimage),"이미지1"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));
        adapter.addItem(new IconTextItem(res.getDrawable(R.drawable.cloud),"이미지2"));


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
