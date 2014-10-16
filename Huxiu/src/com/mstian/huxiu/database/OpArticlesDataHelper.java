
package com.mstian.huxiu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;

import com.mstian.huxiu.AppData;
import com.mstian.huxiu.bean.Articles;
import com.mstian.huxiu.bean.Menus;
import com.mstian.huxiu.database.ArticlesContentProvider.ArticlesDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OpArticlesDataHelper extends OpBaseDataHelper {
    
    private Menus mMenus;

    public OpArticlesDataHelper(Context context, Menus menu) {
        super(context);
        mMenus = menu;
    }

    @Override
    protected Uri getContentUri() {
        return ArticlesContentProvider.CONTENT_URI;
    }

    private ContentValues getContentValues(Articles article) {
        ContentValues values = new ContentValues();
        values.put(DBArticleData.AID, article.getAid());
        values.put(DBArticleData.CATEGORY_ID, article.getCatid());
        values.put(DBArticleData.CONTENT_JSON, article.toJson());
        return values;
    }

    public Articles query(int cid, long aid) {
        Articles shot = null;
        Cursor cursor = query(null, DBArticleData.CATEGORY_ID + "=?" + " AND " + DBArticleData.AID + "= ?",
                new String[] {
                        String.valueOf(cid), String.valueOf(aid)
                }, null);
        if (cursor.moveToFirst()) {
            shot = Articles.fromCursor(cursor);
        }
        cursor.close();
        return shot;
    }

    public void bulkInsert(List<Articles> shots) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (Articles shot : shots) {
            ContentValues values = getContentValues(shot);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        synchronized (ArticlesContentProvider.DBLock) {
            ArticlesDataBaseHelper mDBHelper = ArticlesContentProvider.getDBHelper();
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            int row = db.delete(DBArticleData.TABLE_NAME, DBArticleData.CATEGORY_ID + "=?", new String[] {
                String.valueOf(mMenus.getCategoryId())
            });
            return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, DBArticleData.CATEGORY_ID + "=?",
                new String[] { 
                    String.valueOf(mMenus.getCategoryId())
                },
                DBArticleData._ID + " ASC");
    }
}
