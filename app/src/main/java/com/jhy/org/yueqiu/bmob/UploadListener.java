package com.jhy.org.yueqiu.bmob;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface UploadListener {
    void onSuccess(BmobFile file);
    void onProgress(BmobFile file, int value);
    void onFailure(BmobFile file, int code, String msg);
}
