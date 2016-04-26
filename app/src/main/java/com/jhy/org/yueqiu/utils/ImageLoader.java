package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jhy.org.yueqiu.activity.MyTeamActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/21 0021.
 */
public class ImageLoader implements View.OnClickListener {
    private static final int CODE_IMAGE_REQUEST = 0x16;
    private static final int CODE_CAMERA_REQUEST = 0x17;
    private static final int CODE_RESIZE_REQUEST = 0x18;

    public static final String IMAGE_AVATAR = "avatar.jpg";
    public static final String IMAGE_LOGO = "logo.jpg";

    private Activity activity;
    private ImageView iv_selectImage;
    private View v_takePhone;

    private File file;
    private String fileName = IMAGE_AVATAR;
    private Uri uri;


    public ImageLoader(Activity activity, ImageView iv_selectImage, View v_takePhone) {
        this.activity = activity;

        this.iv_selectImage = iv_selectImage;
        if (iv_selectImage != null) {
            iv_selectImage.setOnClickListener(this);
        }

        this.v_takePhone = v_takePhone;
        if (v_takePhone != null) {
            v_takePhone.setOnClickListener(this);
        }

        if (activity.getClass() == MyTeamActivity.class) {
            fileName = IMAGE_LOGO;
        }
        file = new File(Environment.getExternalStorageDirectory(), fileName);
        uri = Uri.fromFile(file);
    }
    public ImageLoader(Activity activity, ImageView iv_selectImage) {
        this(activity, iv_selectImage, null);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_selectImage) {
//            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            activity.startActivityForResult(galleryIntent, CODE_IMAGE_REQUEST);
        } else if (v == v_takePhone) {
            if (isSdcardExisting()) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                activity.startActivityForResult(cameraIntent, CODE_CAMERA_REQUEST);
            } else {
                Toast.makeText(v.getContext(), "请插入sd卡", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setResult (int requestCode, Intent data) {

        switch (requestCode) {

            case CODE_IMAGE_REQUEST:
                resizeImage(data.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (isSdcardExisting()) {
                    resizeImage(uri);
                } else {
                    Toast.makeText(activity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;

            case CODE_RESIZE_REQUEST:
                if (data != null) {
                    showResizeImage(data);
                }
                break;
        }
    }

    private boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CODE_RESIZE_REQUEST);
    }

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = toRoundBitmap(photo);
            iv_selectImage.setImageBitmap(photo);
            saveBitmapAsFile(photo);
        }
    }

    private void saveBitmapAsFile (Bitmap bitmap) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap toRoundBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int r=0;
        //取最短边做边长
        if(width<height){
            r=width;
        } else {
            r=height;
        }
        //构建一个bitmap
        Bitmap backgroundBm=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas=new Canvas(backgroundBm);
        Paint p=new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect=new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r/2, r/2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }
}
