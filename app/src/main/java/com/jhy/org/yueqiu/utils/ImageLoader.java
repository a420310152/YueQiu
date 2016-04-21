package com.jhy.org.yueqiu.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
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
}
