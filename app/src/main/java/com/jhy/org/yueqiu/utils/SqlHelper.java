package com.jhy.org.yueqiu.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
public class SqlHelper extends SQLiteOpenHelper {
    public static final class table {
        public static final String user_info = "user_info";
    }

    private static final String DB_NAME = "yueqiu.db";
    private static final int DB_VERSION = 1;

    public SqlHelper(Context context) {
        super(context, getFilePath(), null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user_info (userId VARCHAR(64) PRIMARY KEY, name VARCHAR(256), avatarUrl VARCHAR(256))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    private static String getFilePath () {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            path += "/" + DB_NAME;
            return path;
        }
        return DB_NAME;
    }
}
