package com.oneMap.module.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.TypedValue;

import com.oneMap.module.common.R;
import com.oneMap.module.common.base.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class CommonUtil {

    private static String TAG= "CommonUtil.class";
    /**
     * 优先存储在SD卡，其次是内部存储
     */
    public static String getRWPath(Context context) {
        String rwPath = null;
        String storagePath = getStoragePath(context, true);
        if (null != storagePath) {
            rwPath = storagePath;
        } else {
            String sdPath = getSDPath();
            rwPath = sdPath;
        }
        return rwPath;
    }




    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDPath() {
        String sdPath = null;
        // 判断sd卡是否存在
        boolean sdCardExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExit) {
            // 获取根目录
            sdPath = Environment.getExternalStorageDirectory().toString();
        }
        return sdPath;

    }



    /**
     * 可以获取SD卡路径，不用在设置中这是默认存储位置
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某个文件夹下所有文件夹名称以及路径
     * */
    public static List<String> getAllFilePathsByRoot(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    /**
     * 去除多余空格 主要用于字符串中间有多个空格 只保留一个
     */
    public static String removeUnnecessarySpace(String string) {
        StringBuffer sb = new StringBuffer();//用其他方法实现
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                sb.append(string.charAt(i));
            } else {
            }
            try {
                if (string.charAt(i) == ' ' && string.charAt(i + 1) != ' ') {
                    sb.append(' ');
                }
            } catch (Exception e) {
                continue;
            }
        }
        return sb.toString();
    }

    public static int getDarkColorPrimary(Context context){
        TypedValue typedValue = new  TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }



    /**
     * 读取某一特定路径下json字符串
     */
    public static String getJson(String fileName, Activity activity) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = activity.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 判断屏幕方向  横竖屏
     */
    public static boolean isScreenChange() {
        Configuration mConfiguration = BaseApplication.getBaseApplication().getContext().getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false;
        }
        return false;
    }





    /**
     * int 与 rgb值转换
     * */
    public static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    /**
     * dp转px
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }



    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取某一路径下文件内容
     */
    public static String readFile(String filePath) {
        if (!filePath.equals("")) {
            try {
                File urlFile = new File(filePath);
                InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String str = "";
                String mimeTypeLine = null;
                while ((mimeTypeLine = br.readLine()) != null) {
                    str = str + mimeTypeLine;
                    Log.i("", "readFile: " + str);
                }
                return str;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }





   /**
    * 向某一路径下保存文件  txt
    * */
    public static void saveStrToTxt(String filePath, String fileName, String string) {
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        String JsonPath = filePath + fileName;
        final File Jsonfile = new File(JsonPath);
        if (!Jsonfile.getParentFile().exists()) {//判断父文件是否存在，如果不存在则创建
            Jsonfile.getParentFile().mkdirs();
        }
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(Jsonfile));
            out.print(string);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "saveStrToTxt: "+e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    //保存字符串
    public static void SaveString(String path, String str) {
        try {
            if (new File(path).exists()) {
                new File(path).delete();
            }
            FileWriter fw = new FileWriter(path);
            fw.flush();
            fw.write(str);
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "SaveString: "+e.getMessage());
        }
    }

    //读取某一路径下的文件
    public static String readTxtFromSDCard(String filPath, String filename) {
        StringBuilder sb = new StringBuilder();
        //判断是否有读取权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //打开文件输入流
            try {
                FileInputStream input = new FileInputStream(filPath + filename);
                byte[] temp = new byte[1024];

                int len = 0;
                //读取文件内容:
                while ((len = input.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                //关闭输入流
                input.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }






    /**
     * 图片压缩
     * */
    public static boolean dealImage(String sourceFilePath, String targetFilePath) {
        try {
            int dh = 1024;
            int dw = 1024;
            BitmapFactory.Options factory = new BitmapFactory.Options();
            factory.inJustDecodeBounds = true; //当为true时  允许查询图片不为 图片像素分配内存
            InputStream is = new FileInputStream(sourceFilePath);
            Bitmap bmp = BitmapFactory.decodeStream(is, null, factory);
            int hRatio = (int) Math.ceil(factory.outHeight / (float) dh); //图片是高度的几倍
            int wRatio = (int) Math.ceil(factory.outWidth / (float) dw); //图片是宽度的几倍
            System.out.println("hRatio:" + hRatio + "  wRatio:" + wRatio);
            //缩小到  1/ratio的尺寸和 1/ratio^2的像素
            if (hRatio > 1 || wRatio > 1) {
                if (hRatio > wRatio) {
                    factory.inSampleSize = hRatio;
                } else
                    factory.inSampleSize = wRatio;
            }
            factory.inJustDecodeBounds = false;
            is.close();
            is = new FileInputStream(sourceFilePath);
            bmp = BitmapFactory.decodeStream(is, null, factory);
            OutputStream outFile = new FileOutputStream(targetFilePath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, outFile);
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }




    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }



    /**
     * 打开文件
     */
    public static void openFile(File file, Context context) {
        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);  //设置intent的Action属性
        String type = getMIMEType(file);  //获取文件file的MIME类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            Uri contentUri = FileProvider.getUriForFile(context,  "com.oneMap.module.common.fileprovider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);   //设置intent的data和Type属性。
        context.startActivity(intent);     //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。

    }

    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");  //获取后缀名前的分隔符"."在fName中的位置。
        if (dotIndex < 0) {
            return type;
        }

        String end = fName.substring(dotIndex).toLowerCase();  /* 获取文件的后缀名*/
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final static String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
}
