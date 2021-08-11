package com.zyf.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.zyf.common.R;
import com.zyf.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtil {

    public static Bitmap getBitmap(byte[] b) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = 1; // width，hight设为原来的十分一
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return BitmapFactory.decodeStream(bais, null, opt);
    }

    public static String saveTempBitmap(Bitmap bmp, String prefix, String suffix) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File file = File.createTempFile(prefix, suffix,
                        Environment.getExternalStorageDirectory());
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(CompressFormat.JPEG, 50, fos);
                fos.flush();
                fos.close();
                String filePath = file.getAbsolutePath();
                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String saveTempBitmap(String path, String prefix, String suffix) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File file = File.createTempFile(prefix, suffix,
                        Environment.getExternalStorageDirectory());
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
                opts.inSampleSize = calculateInSampleSize(opts, 1280, 720);
                opts.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(path, opts);
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 95, fos);
                fos.flush();
                fos.close();
                String filePath = file.getAbsolutePath();
                Log.d("ImageZoom", "ImageSize: " + getReadableFileSize(file.length()));
                return filePath;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取文件实际大小
     */
    public static String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;//获取图片的高
        final int width = options.outWidth;//获取图片的宽
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;//求出缩放值
    }

    /**
     * 保存图片到磁盘上，类型统一为PNG
     *
     * @param context
     * @param bmp     要保存的图像
     * @param path    要保存的路径
     * @param fname   文件名。如果为空，则系统会生成一个
     */
    public static void saveImage(Context context, Bitmap bmp, String path,
                                 String fname) {
        File rootDir = Environment.getExternalStorageDirectory();
        InputStream in = bitmapToStream(bmp);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int len;
        String msg = context.getString(R.string.save_picture_failed);
        if (fname == null || fname.equals("")) {
            fname = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                    .format(new Date());
        }
        fname += ".png";
        File dir = new File(rootDir, path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            out = new FileOutputStream(new File(dir, fname));
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, len);
            }
            msg = context.getString(R.string.save_picture_as);
            msg = String.format(msg, dir.getAbsolutePath() + "/" + fname);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap转InputStream
     */
    public static InputStream bitmapToStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * Drawable转InputStream
     */
    public static InputStream drawableToStream(Drawable d) {
        Bitmap bitmap = drawableToBitmap(d);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * Bitmap转Byte[]
     *
     * @param bm Bitmap对象
     * @return byte[]
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Byte[]转Bitmap
     *
     * @param b byte[]
     * @return Bitmap对象
     */
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 将图片旋转
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        //旋转图片
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * Base64编码的字符串转出图片
     *
     * @param string
     * @return
     */
    public static Bitmap base64ToBitmap(String string) {
        if (Utils.checkNullString(string)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片转Base64编码的字符
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        // 要返回的字符串
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();
                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.NO_WRAP);
//                LogHelper.i("reslut--------"+reslut);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reslut;
    }

    /**
     * 根据path获取bitmap （png格式）
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapFormPath(String path) {
        if (Utils.checkNullString(path)) {
            return null;
        }
        File file = new File(path);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
        opts.inSampleSize = 2;
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 将网络图片编码为base64
     *
     * @param url
     * @return
     */
    public static String encodeUrlToBase64(String url) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        System.out.println("图片的路径为:" + url.toString());
        //打开链接
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inStream.close();
            byte[] data = outStream.toByteArray();
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            return base64;//返回Base64编码过的字节数组字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
