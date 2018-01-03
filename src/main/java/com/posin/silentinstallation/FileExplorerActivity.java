package com.posin.silentinstallation;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnItemClick;

/**
 * created by Greetty at 2018/1/3 15:31
 * <p>
 * 文件选择Activity
 */
public class FileExplorerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "FileExplorerActivity";

    private ListView listView;
    private SimpleAdapter adapter;
    private  String rootPath = Environment.getExternalStorageDirectory().getPath();
    private  String currentPath = rootPath;
    private List<Map<String, Object>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new SimpleAdapter(this, list, R.layout.list_item,
                new String[]{"name", "img"}, new int[]{R.id.name, R.id.img});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        refreshListItems(currentPath);
    }

    private void refreshListItems(String path) {
        setTitle(path);
        File[] files = new File(path).listFiles();
        list.clear();
        if (files != null) {
            for (File file : files) {
                Map<String, Object> map = new HashMap<>();
                if (file.isDirectory()) {
                    map.put("img", R.mipmap.directory);
                } else {
                    map.put("img", R.mipmap.file_doc);
                }
                map.put("name", file.getName());
                map.put("currentPath", file.getPath());
                list.add(map);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        currentPath = (String) list.get(position).get("currentPath");
        File file = new File(currentPath);
        if (file.isDirectory())
            refreshListItems(currentPath);
        else {
            Intent intent = new Intent();
            intent.putExtra("apk_path", file.getPath());
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (rootPath.equals(currentPath)) {
            super.onBackPressed();
        } else {
            File file = new File(currentPath);
            currentPath = file.getParentFile().getPath();
            refreshListItems(currentPath);
        }
    }

}
