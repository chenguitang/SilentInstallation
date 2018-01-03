package com.posin.silentinstallation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.posin.silentinstallation.utils.InstallUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start_install)
    Button btnStartInstall;

    private static final String TAG = "MainActivity";
    private final String path = "mnt/sdcard/target.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start_install})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_install:
                try {
                    InstallUtils.reflexInstall(path);
                    Log.e(TAG, "++++++++++++安装成功++++++++++++");
                } catch (Throwable throwable) {
                    Log.e(TAG, "出错了： " + throwable.getMessage());
                    throwable.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
