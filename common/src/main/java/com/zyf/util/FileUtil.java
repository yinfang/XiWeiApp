package com.zyf.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.zyf.model.MyData;
import com.zyf.model.MyRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by long on 17-6-9.
 */

public class FileUtil {

    /**
     * 检测Sdcard是否存在
     */
    protected static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 生成文件
     */
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将文件写入本地
     *
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    public static File saveFile(Context context, InputStream is, String destFileName) {
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            File dir = new File(getDownloadPath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } catch (IOException e) {
            UI.showToast(context,"文件写入失败！");
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取下载路径
     *
     * @return
     */
    public static String getDownloadPath() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "/Download/");
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    /**
     * 生成文件夹
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("FileError:", e + "");
        }
    }

    /**
     * 从文件读取指定长度的byte[]
     */
    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        if (len == -1) {
            len = (int) file.length();
        }
        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 读文件 ,返回String
     */
    public static String readFile(File file) {
        BufferedReader reader = null;
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuilder builder = new StringBuilder();
//            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从指定路径读文件 ,返回Mydata
     */
    public static MyData getDirFilesContent(String rootFilePath) {
        MyData data = new MyData();
        if (TextUtils.isEmpty(rootFilePath)) {
            return data;
        }
        File file = new File(rootFilePath);
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    MyRow row = new MyRow();
                    File reFile = files[i];
                    row.put(reFile.getName(), readFile(reFile));
                    data.add(row);
                }
                return data;
            } else {
                return data;
            }
        } else {
            return data;
        }
    }

    /**
     * 将字符串写入到文本文件中
     */
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(strContent.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.d("writeTxtToFile", e.getMessage());
        }
    }

    /**
     * 复制文件
     *
     * @param in
     * @param out
     */
    public static void copy(final InputStream in, final OutputStream out) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    /**
     * 解压zip文件
     *
     * @param zipFile
     * @param outputDir
     */
    public static void unzip(String zipFile, String outputDir) {
        byte[] buffer = new byte[1024];
        try {
            // create output directory is not exists
            File folder = new File(outputDir);
            if (!folder.exists()) {
                folder.mkdir();
            }
            // get the zip file content
            ZipInputStream zis = new ZipInputStream(
                    new FileInputStream(zipFile));
            // get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputDir + File.separator + fileName);
                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                // create all non exists folders
                // else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从Assets目录获取文本文件内容
     *
     * @param fileName
     * @return
     * @throws Exception
     */

    public String getFromAssets(Context context, String fileName) throws Exception {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getAssets().open
                    (fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            bufReader.close();
            inputReader.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 将assets下的文件写入内存
     * pdf  word  excel 等文件
     *
     * @return File
     */
    public static File saveAssets(Context context, String filename) {
        File f = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getAssets().open(filename);
            f = new File(Environment.getExternalStorageDirectory() + File.separator + "/Download/", filename);
            fos = new FileOutputStream(f);
            //获取文件的字节数
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            int bytes = is.read(buffer);
            //.getString(buffer, ENCODING);
            //将读取的数据写到本地
            fos.write(buffer, 0, bytes);
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            f.delete();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return f;
        }
    }

    /**
     * 播放音频文件
     */
    public static void playBeep(final Context context, final int resSound,
                                final int duration) {
        Thread t = new Thread() {
            public void run() {
                MediaPlayer player = null;
                int countBeep = 0;
                while (countBeep < 1) {
                    player = MediaPlayer.create(context, resSound);
                    player.start();
                    countBeep += 1;
                    try {
                        Thread.sleep(player.getDuration() + duration);
                        player.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    /**
     * 获取配置文件中的mate数据
     * 可配置mate的组件有 activity 、 application  、service 、receiver
     */
    public static Bundle getMeta(Context context) {
        try {
//            if (context instanceof Application) {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData;
            }
          /*  //获取Activity的mate
                ActivityInfo appInfo = context.getPackageManager()
                        .getActivityInfo(((Activity) context).getComponentName(), PackageManager.GET_META_DATA);
                if (appInfo != null) {
                    return appInfo.metaData;
                }
            //获取Service的mate
                ServiceInfo appInfo = context.getPackageManager()
                        .getServiceInfo( new ComponentName(context,Service.class), PackageManager.GET_META_DATA);
                if (appInfo != null) {
                    return appInfo.metaData;
                }
              //获取Receiver的mate
                ActivityInfo appInfo = context.getPackageManager()
                        .getReceiverInfo( new ComponentName(context, BroadcastReceiver.class), PackageManager.GET_META_DATA);
                if (appInfo != null) {
                    return appInfo.metaData;

            }*/
        } catch (PackageManager.NameNotFoundException ex) {
            Log.d("getMeta", ex.getMessage());
        }
        return null;
    }

    /**
     * 获取手机通讯录数据
     */
    public static Vector<String> getContactsMobile(Context context) {
        Vector<String> v = new Vector<String>();
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);
        while (pCur.moveToNext()) {
            String phone = pCur
                    .getString(pCur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (phone.length() == 11 && phone.startsWith("1"))
                System.out.println("phone" + phone);
            v.add(phone);
        }
        pCur.close();
        return v;
    }
}
