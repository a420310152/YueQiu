package com.jhy.org.yueqiu.bmob;

import com.jhy.org.yueqiu.config.App;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BmobFileUploader extends UploadFileListener {

    private BmobFile file;
    private UploadListener listener;

    private BmobFileUploader (BmobFile file, UploadListener listener) {
        this.file = file;
        this.listener = listener;
    }

    public static void upload (BmobFile file, UploadListener listener) {
        file.uploadblock(App.getInstance(), new BmobFileUploader(file, listener));
    }

    @Override
    public void onSuccess() {
        if (listener != null) {
            listener.onSuccess(file);
        }
    }

    @Override
    public void onProgress(Integer value) {
        if (listener != null) {
            listener.onProgress(file, value);
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        if (listener != null) {
            listener.onFailure(file, code, msg);
        }
    }
}
