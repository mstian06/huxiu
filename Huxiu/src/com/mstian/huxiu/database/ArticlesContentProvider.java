package com.mstian.huxiu.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.mstian.huxiu.AppData;

import java.util.HashMap;


public class ArticlesContentProvider extends ContentProvider{
    
    public static final String TAG = "AritclesContentProvider";
    
    static final Object DBLock = new Object();
    
    public static final String AUTHORITY = "com.mstian.huxiu.ArticlesContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/articles");
    
    public static final String CONTENT_TYPE_STRING = "vnd.android.cursor.dir/vnd.huxiu.articles";
    public static final String CONTENT_ITEM_TYPE_STRING = "vnd.android.cursor.item/vnd.huxiu.articles";
    
    private static final UriMatcher sUriMatcher;
    public static final int VALUE_CONTENT_TYPE_STRING = 1;
    public static final int VALUE_CONTENT_ITEM_TYPE_STRING = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        
        sUriMatcher.addURI(AUTHORITY, "articles", VALUE_CONTENT_TYPE_STRING);
        sUriMatcher.addURI(AUTHORITY, "articles/#", VALUE_CONTENT_ITEM_TYPE_STRING);
    }
    

    private static HashMap<String, String> sHashMap;
    static {
        sHashMap = new HashMap<String, String>();
        
        sHashMap.put(DBArticleData._ID, DBArticleData._ID);
        sHashMap.put(DBArticleData.AID, DBArticleData.AID);
        sHashMap.put(DBArticleData.CATEGORY_ID, DBArticleData.CATEGORY_ID);
        sHashMap.put(DBArticleData.CONTENT_JSON, DBArticleData.CONTENT_JSON);
    }
    
    
    private static ArticlesDataBaseHelper mDataBaseHelper;
    
    public static ArticlesDataBaseHelper getDBHelper() {
        if (mDataBaseHelper == null) {
            mDataBaseHelper = new ArticlesDataBaseHelper(AppData.getContext());
        }
        return mDataBaseHelper;
    }
    
    @Override
    public boolean onCreate() {
        return true;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        
        synchronized (DBLock) {
            SQLiteQueryBuilder qbBuilder = new SQLiteQueryBuilder();
            
            switch (sUriMatcher.match(uri)) {
                case VALUE_CONTENT_TYPE_STRING:
                    qbBuilder.setTables(DBArticleData.TABLE_NAME);
                    qbBuilder.setProjectionMap(sHashMap);
                    break;
                default:
                    break;
            }
            
            String orderByString;
            if (TextUtils.isEmpty(sortOrder)) {
                orderByString = DBArticleData.DEFAULT_SORT_ORDER;
            } else {
                orderByString = sortOrder;
            }
            
            SQLiteDatabase db = getDBHelper().getReadableDatabase();
            Cursor c = qbBuilder.query(db, projection, selection, selectionArgs, null, null, orderByString);//yes
//            Cursor c = qbBuilder.query(db, projection, "category_id=1", null, null, null, orderByString);//yes
//            Cursor c = db.query(DBArticleData.TABLE_NAME, null, selection, selectionArgs, null, null, orderByString);
            
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        switch (sUriMatcher.match(uri)) {
            case VALUE_CONTENT_TYPE_STRING:
                return CONTENT_TYPE_STRING;
            case VALUE_CONTENT_ITEM_TYPE_STRING:
                return CONTENT_ITEM_TYPE_STRING;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        
        synchronized (DBLock) {
            if (sUriMatcher.match(uri) != VALUE_CONTENT_TYPE_STRING) {
                return null;
            }
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            long rowid = db.insert(DBArticleData.TABLE_NAME, null, values);
            
            if (rowid > 0) {
                Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowid);
                AppData.getContext().getContentResolver().notifyChange(insertUri, null);
                return insertUri;
            }
            
            return null;
        }
    }
    
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            db.beginTransaction();
            try {
                for (ContentValues contentValues : values) {
                    db.insertWithOnConflict(DBArticleData.TABLE_NAME, BaseColumns._ID, contentValues,
                            SQLiteDatabase.CONFLICT_IGNORE);
                }
                db.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri, null);
                return values.length;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count = 0;
            
            switch (sUriMatcher.match(uri)) {
                case VALUE_CONTENT_TYPE_STRING:
                    count = db.delete(DBArticleData.TABLE_NAME, selection, selectionArgs);
                    break;

                case VALUE_CONTENT_ITEM_TYPE_STRING:
                    String rowid = uri.getPathSegments().get(1);
                    count = db.delete(DBArticleData.TABLE_NAME, DBArticleData._ID + "=" + rowid 
                            + (!TextUtils.isEmpty(selection)? " and (" + selection + ")" : ""), selectionArgs);
                    break;
                default:
                    break;
            }
            
            AppData.getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count = 0;
            
            switch (sUriMatcher.match(uri)) {
                case VALUE_CONTENT_TYPE_STRING:
                    count = db.update(DBArticleData.TABLE_NAME, values, selection, selectionArgs);
                    break;

                case VALUE_CONTENT_ITEM_TYPE_STRING:
                    String rowid = uri.getPathSegments().get(1);
                    count = db.update(DBArticleData.TABLE_NAME, values, DBArticleData._ID + "=" + rowid 
                            + (!TextUtils.isEmpty(selection)? " and (" + selection + ")" : ""), selectionArgs);
                    break;
                default:
                    break;
            }
            
            AppData.getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }
    
    public static class ArticlesDataBaseHelper extends SQLiteOpenHelper{
        
        private static final String DB_NAME = "huxiu.db";
        private static final int DB_VERSION = 1;

        public ArticlesDataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(DBArticleData.createTable());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(DBArticleData.dropTable());
            onCreate(db);
        }
    }
}
