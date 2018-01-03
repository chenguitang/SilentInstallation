package com.posin.silentinstallation.utils;

import android.content.pm.IPackageManager;
import android.net.Uri;
import android.os.IBinder;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Greetty on 2018/1/2.
 * <p>
 * 应用安装工具类
 */
public class InstallUtils {

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
}
