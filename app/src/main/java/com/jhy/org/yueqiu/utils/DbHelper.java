package com.jhy.org.yueqiu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;

import com.jhy.org.yueqiu.config.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/5/7 0007.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final class table {
        public static final String user_info = "user_info";
        public static final String challenge_info = "challenge_info";
    }
    private static final String CREATE_USER_INFO_UNIQUE_INDEX = "CREATE UNIQUE INDEX unique_index_user_info ON user_info (userId)";

    private static final String DB_NAME = "yueqiu.db";
    private static final int DB_VERSION = 1;
    private static Logx logx = new Logx(DbHelper.class);

    public DbHelper(Context context) {
        super(context, getFilePath(), null, DB_VERSION);
        logx.e("onCreate: filePath = " + getFilePath());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String prefix = "CREATE TABLE IF NOT EXISTS ";

        db.execSQL(prefix + table.user_info + " (" +
                "userId VARCHAR(64) PRIMARY KEY, " +
                "name VARCHAR(256), " +
                "portraitUri VARCHAR(256)" +
                ")");
        db.execSQL(prefix + table.challenge_info + "(" +
                "challenge_id VARCHAR(64) PRIMARY KEY, " +
                "challenge_type VARCHAR(64), " +
                "challenge_title VARCHAR(256), " +
                "img_url VARCHAR(256), " +
                "initiator_name VARCHAR(256), " +
                "place_name VARCHAR(256), " +
                "from_date VARCHAR(64)" +
                ")");

        db.beginTransaction();
        try {
            db.execSQL(CREATE_USER_INFO_UNIQUE_INDEX);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
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





    private static class UserInfoCache {
        private static final String COL_USER_ID = "userId";
        private static final String COL_NAME = "name";
        private static final String COL_PORTRAIT_URI = "portraitUri";

        private static UserInfoCache singleton = null;

        private DbHelper dbHelper;
        private SQLiteDatabase db;
        private Set<String> changedSet;
        private HashMap<String, UserInfo> userMap;

        private UserInfoCache (Context context) {
            dbHelper = new DbHelper(context);
            db = dbHelper.getReadableDatabase();

            changedSet = new HashSet<>();
            userMap = new HashMap<>();

            List<UserInfo> userList= queryAll();
            for (UserInfo i : userList) {
                userMap.put(i.getUserId(), i);
            }
        }

        public static UserInfoCache getInstance () {
            if (singleton == null) {
                singleton = new UserInfoCache(App.getInstance());
            }
            return singleton;
        }

        public List<UserInfo> queryAll () {
            List<UserInfo> list = new ArrayList<>();
            Cursor cursor = db.query(table.user_info, null, null, null, null, null, null);

            UserInfo userInfo = null;
            while (cursor.moveToNext()) {
                userInfo = buildInfoFromCursor(cursor);
                list.add(userInfo);
            }
            cursor.close();
            return list;
        }

        public UserInfo getUserInfo (String userId) {
            return userMap.get(userId);
        }

        public UserInfoCache setUserInfo (UserInfo userInfo) {
            if (userInfo != null) {
                String userId = userInfo.getUserId();
                userMap.put(userId, userInfo);
                changedSet.add(userId);
            }
            return this;
        }

        public UserInfoCache remove (String userId) {
            if (contains(userId)) {
                userMap.remove(userId);
                changedSet.add(userId);
            }
            return this;
        }

        public boolean contains (String userId) {
            return userMap.containsKey(userId);
        }

        public UserInfoCache clear () {
            return this;
        }

        public UserInfoCache apply () {
            if (changedSet.size() > 0) {
                for (String i : changedSet) {
                    String where = COL_USER_ID + " = " + i;

                    if (userMap.containsKey(i)) {
                        UserInfo userInfo = userMap.get(i);

                        ContentValues values = new ContentValues();
                        values.put(COL_USER_ID, userInfo.getUserId());
                        values.put(COL_NAME, userInfo.getPortraitUri().toString());

                        db.replace(table.user_info, null, values);
                    } else {
                        int result = db.delete(table.user_info, where, null);
                        if (result == 0) {
                            logx.e("数据库中的记录删除 失败");
                        }
                    }
                }
                changedSet.clear();
            }
            return this;
        }

        private UserInfo buildInfoFromCursor (Cursor cursor) {
            UserInfo userInfo = null;
            if (cursor != null) {
                String userId = getStringFromCursor(cursor, COL_USER_ID);
                String name = getStringFromCursor(cursor, COL_NAME);
                String portraitUri = getStringFromCursor(cursor, COL_PORTRAIT_URI);
                userInfo = new UserInfo(userId, name, Uri.parse(portraitUri));
            }
            return userInfo;
        }

        private static String getStringFromCursor (Cursor cursor, String columnName) {
            String result = cursor.getString(cursor.getColumnIndex(columnName));
            return result == null ? "" : result;
        }
    }
}
