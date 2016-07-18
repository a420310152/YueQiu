package com.jhy.org.yueqiu.bmob;

import com.bmob.BmobConfiguration;
import com.jhy.org.yueqiu.config.App;
import com.jhy.org.yueqiu.config.Key;
import com.jhy.org.yueqiu.domain.Person;
import com.jhy.org.yueqiu.utils.HttpUtils;
import com.jhy.org.yueqiu.utils.Utils;
import com.squareup.okhttp.Callback;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public final class BmobUtils {
    private static final String BASE_CLOUD_URL = "http://cloud.bmob.cn/" + Key.bmob.secret_key + "/";
    public static final String FUNC_GET_TOKEN = "getToken";

    public static void initialize () {
        Bmob.initialize(App.getInstance(), Key.bmob.application_id);
    }

    public static void get (String func, Map<String, String> params, Callback callback) {
        HttpUtils.get(BASE_CLOUD_URL + func, params, callback);
    }
    public static void get (String func, Callback callback) {
        HttpUtils.get(BASE_CLOUD_URL + func, callback);
    }

    public static void post (String func, Map<String, String>params, Callback callback) {
        HttpUtils.post(BASE_CLOUD_URL + func, params, callback);
    }

    public static boolean isEmpty (BmobObject value, BmobObject... values) {
        if (value == null || Utils.isEmpty(value.getObjectId())) {
            return true;
        }
        for (BmobObject i : values) {
            if (i == null || Utils.isEmpty(i.getObjectId())) {
                return true;
            }
        }
        return false;
    }

    public static Person getCurrentUser () {
        return BmobUser.getCurrentUser(App.getInstance(), Person.class);
    }

    public static <T> void query (String objectId, GetListener<T> listener) {
        new BmobQuery<T>().getObject(App.getInstance(), objectId, listener);
    }
    public static <T> void query (FindListener<T> listener) {
        new BmobQuery<T>().findObjects(App.getInstance(), listener);
    }
    public static <T> void query (int limit, FindListener<T> listener) {
        BmobQuery<T> query = new BmobQuery<T>();
        query.setLimit(limit);
        query.findObjects(App.getInstance(), listener);
    }

    public static void insert (List<BmobObject> list, SaveListener listener) {
        new BmobObject().insertBatch(App.getInstance(), list, listener);
    }
    public static void insert (BmobObject obj, SaveListener listener) {
        if (obj != null) {
            obj.save(App.getInstance(), listener);
        }
    }

    public static void update (List<BmobObject> list, UpdateListener listener) {
        new BmobObject().updateBatch(App.getInstance(), list, listener);
    }
    public static void update (BmobObject obj, UpdateListener listener) {
        if (!isEmpty(obj)) {
            obj.update(App.getInstance(), listener);
        }
    }

    public static void delete (List<BmobObject> list, DeleteListener listener) {
        new BmobObject().deleteBatch(App.getInstance(), list, listener);
    }
    public static void delete (BmobObject obj, DeleteListener listener) {
        if (!isEmpty(obj)) {
            obj.delete(App.getInstance(), listener);
        }
    }
    public static void delete (String id, DeleteListener listener) {
        BmobObject obj = new BmobObject();
        obj.setObjectId(id);
        obj.delete(App.getInstance(), listener);
    }

    public static void upload (BmobFile file, UploadListener listener) {
        BmobFileUploader.upload(file, listener);
    }
}
