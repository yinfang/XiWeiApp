package com.zyf.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;

import com.zyf.common.R;
import com.zyf.domain.C;
import com.zyf.util.AppUtil;
import com.zyf.util.image.GetImagePath;
import com.zyf.util.image.ImageUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;


@SuppressLint({"InflateParams", "Registered", "NewApi"})
public class CamareAndCropActivity extends BaseActivity {
    private boolean isShowPictrue;// 是否展示裁剪后的照片
    public boolean isCrop = false;//是否裁剪 默认不裁剪
    private static String path = Environment.getExternalStorageDirectory() + "";
    private static File mCameraFile = new File(path, "IMAGE_FILE_NAME.jpg");//照相机的File对象
    private static File mCropFile = new File(path, "PHOTO_FILE_NAME.jpg");//裁剪后的File对象
    private static File mGalleryFile = new File(path, "IMAGE_GALLERY_NAME.jpg");//相册的File对象

    @Override
    public void picListSelected(View view, int index, boolean isCrop) {
        this.isCrop = isCrop;
        if (index == 0) {// 拍照
            takePicture(this, C.REQUEST_FROM_CAMERA);
        } else if (index == 1) {// 选择图片
            choosePicture(this, C.REQUEST_FROM_PICTURE);
        }
    }

    /**
     * 拍照、相册选择照片、照片裁剪后的回调
     */
    @SuppressWarnings("deprecation")
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.REQUEST_FROM_CAMERA && resultCode == RESULT_OK) {// 照相后剪裁
            Uri inputUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0以上
                inputUri = FileProvider.getUriForFile(this, AppUtil.getPackageName(getApplicationContext()) + ".fileprovider",
                        mCameraFile);//通过FileProvider创建一个content类型的Uri
            } else {
                inputUri = Uri.fromFile(mCameraFile);
            }
            if (isCrop) {
                startCrop(inputUri);
            } else {
                afterPictureCrop(inputUri);
            }
        } else if (requestCode == C.REQUEST_FROM_PICTURE && resultCode == RESULT_OK) {// 选取图片后剪裁
            if (data != null) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   // 判断版本大于等于7.0
                    File imgUri = new File(GetImagePath.getPath(this, data.getData()));
                    uri = FileProvider.getUriForFile(this, AppUtil.getPackageName(getApplicationContext()) + ".fileprovider",
                            imgUri);
                } else {
                    uri = data.getData();
                }
                if (isCrop) {
                    startCrop(uri);
                } else {
                    afterPictureCrop(uri);
                }
            }
        } else if (requestCode == C.REQUEST_HANDLE_PICTURE && resultCode == RESULT_OK) {// 处理剪裁结果
            Uri uri = UCrop.getOutput(data);
            afterPictureCrop(uri);
        }
    }

    /**
     * 显示裁剪后的照片
     */
    public void afterPictureCrop(Uri outUri) {
        String filePath;
        if (!isCrop) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   // 判断版本大于等于7.0
                filePath = getPathFromContentProvider(outUri);
            } else {
                filePath = FileUtils.getPath(this, outUri);
            }
        } else {
            filePath = FileUtils.getPath(this, outUri);
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = ImageUtil.calculateInSampleSize(options, C.small, C.small);//自定义一个宽和高
        Bitmap small = BitmapFactory.decodeFile(filePath, options);
        if (isShowPictrue)
            if (iv != null) {
                if (small != null) {
                    iv.setImageBitmap(small);
                }
            }
        String tmpfile = ImageUtil.saveTempBitmap(filePath, "tmp", ".jpg");//不论是否剪裁图片，上传时都需要压缩
      /*  if (null != lin) {
            Bitmap large = BitmapFactory.decodeFile(tmpfile);
            if (large == null) {
                return;
            }
            lin.setBackground(new BitmapDrawable(large));
        }*/
        doUploadImage(tmpfile, small);
    }

    /**
     * 拍照
     */
    public static void takePicture(Activity a, int requestType) {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
            Uri uriForFile = FileProvider.getUriForFile(a, AppUtil.getPackageName(a.getApplicationContext()) + ".fileprovider",
                    mCameraFile);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intentFromCapture.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intentFromCapture.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
        }
        a.startActivityForResult(intentFromCapture, requestType);
    }

    /**
     * 相册选择照片
     */
    public static void choosePicture(Activity a, int requestType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            Uri uriForFile = FileProvider.getUriForFile
                    (a, AppUtil.getPackageName(a.getApplicationContext()) + ".fileprovider", mGalleryFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            a.startActivityForResult(intent, requestType);
        } else {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mGalleryFile));
            a.startActivityForResult(intent, requestType);
        }
    }

    /**
     * 裁剪照片
     */
    public static void cropPicture(final Activity a, final int requestType,
                                   final Uri inputUri) {
        if (inputUri == null) {
            Log.e("error", "文件不存在");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri outPutUri = Uri.fromFile(mCropFile);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            Uri outPutUri = Uri.fromFile(mCropFile);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = GetImagePath.getPath(a, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 50);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", "JPEG");
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        a.startActivityForResult(intent, requestType);//这里就将裁剪后的图片的Uri返回了


    }

    public void setisShowPictrue(boolean isShowPictrue) {
        this.isShowPictrue = isShowPictrue;
    }

    private Uri getOutUri() {
        String date = C.df_yMd.format(new Date());
        String random = String.valueOf(new Random().nextLong());
        String name = date + "-" + random + ".jpg";
        Uri uri = Uri.fromFile(new File(getCacheDir(), name));
        return uri;
    }

    private void startCrop(Uri sourceUri) {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);
        options.setCompressionQuality(80);
        options.setStatusBarColor(Color.BLACK);
        options.setToolbarColor(getResources().getColor(R.color.title_blue));
        UCrop.of(sourceUri, getOutUri()).withAspectRatio(1, 1).withMaxResultSize(350, 350)
                .withOptions(options).start(this, C.REQUEST_HANDLE_PICTURE);

    }

    public String getPathFromContentProvider(Uri uri) {
        List<PackageInfo> packs = this.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            String fileProviderClassName = FileProvider.class.getName();
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (uri.getAuthority().equals(provider.authority)) {
                            if (provider.name.equalsIgnoreCase(fileProviderClassName)) {
                                Class<FileProvider> fileProviderClass = FileProvider.class;
                                try {
                                    Method getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context.class, String.class);
                                    getPathStrategy.setAccessible(true);
                                    Object invoke = getPathStrategy.invoke(null, this, uri.getAuthority());
                                    if (invoke != null) {
                                        String PathStrategyStringClass = FileProvider.class.getName() + "$PathStrategy";
                                        Class<?> PathStrategy = Class.forName(PathStrategyStringClass);
                                        Method getFileForUri = PathStrategy.getDeclaredMethod("getFileForUri", Uri.class);
                                        getFileForUri.setAccessible(true);
                                        Object invoke1 = getFileForUri.invoke(invoke, uri);
                                        if (invoke1 instanceof File) {
                                            return ((File) invoke1).getAbsolutePath();
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }

                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 上传图片（已压缩为1280*720，质量为95%）
     */
    protected void doUploadImage(String tmpfile, Bitmap small) {
    }

}