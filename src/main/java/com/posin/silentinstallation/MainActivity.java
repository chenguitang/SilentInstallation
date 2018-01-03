package com.posin.silentinstallation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.posin.silentinstallation.utils.InstallUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_reflex_install)
    Button btnReflexInstall;

    private static final String TAG = "MainActivity";
    private  String path = "mnt/sdcard/target.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_reflex_install, R.id.btn_root_install, R.id.btn_select_path})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reflex_install:
                try {
                    InstallUtils.reflexInstall(path);
                    Log.e(TAG, "++++++++++++安装成功++++++++++++");
                } catch (Throwable throwable) {
                    Log.e(TAG, "出错了： " + throwable.getMessage());
                    throwable.printStackTrace();
                }
                break;
            case R.id.btn_root_install:
                try {
                    if (isRoot()) {
                        InstallUtils.rootInstall(path);
                        Log.e(TAG, "++++++++++++安装成功++++++++++++");
                    } else {
                        Toast.makeText(this, "机器没有root，获取获取管理员权限",
                                Toast.LENGTH_SHORT).show();
                    }


                } catch (Throwable throwable) {
                    Log.e(TAG, "出错了： " + throwable.getMessage());
                    throwable.printStackTrace();
                }
                break;
            case R.id.btn_select_path: //选择文件
                Intent intent = new Intent(this, FileExplorerActivity.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            path = data.getStringExtra("apk_path");
//            apkPathText.setText(apkPath);
        }
    }

    /**
     * 判断机器是否已Root
     *
     * @return boolean
     */
    public boolean isRoot() {
        boolean bool = false;
        try {
            bool = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}
