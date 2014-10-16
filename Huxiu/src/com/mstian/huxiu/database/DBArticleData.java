package com.mstian.huxiu.database;

import android.provider.BaseColumns;

public class DBArticleData implements BaseColumns {

    public static final String TABLE_NAME = "articles";
    
    public static final String DEFAULT_SORT_ORDER = "aid desc";
    
    public static final String _ID = "_id";
    public static final String AID = "aid";
    public static final String CATEGORY_ID = "category_id";//category_id
    public static final String CONTENT_JSON = "content_json";
    
    public static int INDEX__ID = 0;
    public static int INDEX_AID = 1;
    public static int INDEX_CATEGORY_ID = 2;
    public static int INDEX_CONTENT_JSON = 3;
    
    public static String createTable()
    {
        return "create table if not exists " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, aid integer not null unique, category_id integer not null, content_json text not null)";
    }
    
    public static String dropTable()
    {
        return "drop table if exists " + TABLE_NAME;
    }
}
