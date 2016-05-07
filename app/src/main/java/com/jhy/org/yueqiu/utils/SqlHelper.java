package com.jhy.org.yueqiu.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
public class SqlHelper extends SQLiteOpenHelper {
    public static final class table {
        public static final String user_info = "user_info";
        public static final String challenge_info = "challenge_info";
    }

    private static final String DB_NAME = "yueqiu.db";
    private static final int DB_VERSION = 1;
    private static Logx logx = new Logx(SqlHelper.class);

    public SqlHelper(Context context) {
        super(context, getFilePath(), null, DB_VERSION);
        logx.e("onCreate: filePath = " + getFilePath());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String prefix = "CREATE TABLE IF NOT EXISTS ";

        db.execSQL(prefix + table.user_info + " (" +
                "userId VARCHAR(64) PRIMARY KEY, " +
                "name VARCHAR(256), " +
                "avatarUrl VARCHAR(256)" +
                ")");
        db.execSQL(prefix + table.challenge_info + "(" +
                "challenge_id VARCHAR(64), " +
                "type VARCHAR(64), " +
                "title VARCHAR(256), " +
                "img_url VARCHAR(256), " +
                "initiator_name VARCHAR(256), " +
                "placeName VARCHAR(256), " +
                "from_date VARCHAR(64)" +
                ")");
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

    public static class ChallengeTable {

    }
}
