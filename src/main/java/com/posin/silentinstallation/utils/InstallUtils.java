package com.posin.silentinstallation.utils;

import android.content.pm.IPackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * Created by Greetty on 2018/1/2.
 * <p>
 * 应用安装工具类
 */
public class InstallUtils {

    private static final String TAG = "InstallUtils";

    /**
     * 通过反射实现静默安装
     *
     * @param path 安装包路径
     * @throws Throwable 异常
     */
    public static void reflexInstall(String path) throws Throwable {

        File apkFile = new File(path);

        Class<?> clazz = Class.forName("android.os.ServiceManager");
        Method method_getService = clazz.getMethod("getService",
                String.class);
        IBinder bind = (IBinder) method_getService.invoke(null, "package");

        IPackageManager iPm = IPackageManager.Stub.asInterface(bind);

        iPm.installPackage(Uri.fromFile(apkFile), null, 2,
                apkFile.getName());
    }

    /**
     * 获取管理员（root）权限
     *
     * @param path apk地址
     * @throws Throwable 异常信息
     */
    public static void rootInstall(String path) throws Throwable {

        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            //申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());

            Log.e(TAG, "11111111111111");
            // 执行pm install命令
            String command = "pm install -r " + path + "\n";
            //String command = "reboot";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }

            Log.e("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (msg.contains("Failure")) {
                throw new Exception("---------------- install package failure ----------------");
            }
        } finally {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (errorStream != null) {
                errorStream.close();
            }
        }
    }


}
