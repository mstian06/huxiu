package com.mstian.huxiu.bean;

import android.database.Cursor;

import com.google.gson.Gson;
import com.mstian.huxiu.database.DBArticleData;

import java.util.ArrayList;
import java.util.HashMap;

public class Articles extends BaseType{
    
    private static final HashMap<Integer, Articles> CACHE = new HashMap<Integer, Articles>();
    
    private int comment_num;
    private String author;
    private int author_id;
    private String title;
    private int catid;
    private long dateline;
    private long updateline;
    private long sorttime;
    private int aid;
    private String summary;
    private String img;
    private int apply;
    private int grade;
    
    private static void addToCache(Articles portal) {
        CACHE.put(portal.getAid(), portal);
    }
    
    private static Articles getFromCache(int aid) {
        return CACHE.get(aid);
    }
    
    public static Articles fromJson(String json) {
        return new Gson().fromJson(json, Articles.class);
    }
    
    public static Articles fromCursor(Cursor cursor) {
        int aid = cursor.getInt(cursor.getColumnIndex(DBArticleData.AID));
        Articles portal = getFromCache(aid);
        if (portal != null) {
            return portal;
        }
        portal = new Gson().fromJson(
                cursor.getString(cursor.getColumnIndex(DBArticleData.CONTENT_JSON)),
                Articles.class);
        addToCache(portal);
        return portal;
    }


    public int getComment_num() {
        return comment_num;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public String getTitle() {
        return title;
    }

    public int getCatid() {
        return catid;
    }

    public long getDateline() {
        return dateline;
    }

    public long getUpdateline() {
        return updateline;
    }

    public long getSorttime() {
        return sorttime;
    }

    public int getAid() {
        return aid;
    }

    public String getSummary() {
        return summary;
    }

    public String getImg() {
        return img;
    }

    public int getApply() {
        return apply;
    }

    public int getGrade() {
        return grade;
    }
    
    public static class Image
    {
        private String title;
        private int aid;
        private String pic;
        private int displayorder;
        private int type;
        private String url;
        public String getTitle() {
            return title;
        }
        public int getAid() {
            return aid;
        }
        public String getPic() {
            return pic;
        }
        public int getDisplayorder() {
            return displayorder;
        }
        public int getType() {
            return type;
        }
        public String getUrl() {
            return url;
        }
    }

    public static class PortalRequestData {
        
        private int catid;
        private int page;
        private int result;
        private ArrayList<Articles> content;
        private ArrayList<Image> images;
        public int getCatid() {
            return catid;
        }
        public int getPage() {
            return page;
        }
        public int getResult() {
            return result;
        }
        public ArrayList<Articles> getContent() {
            return content;
        }
        public ArrayList<Image> getImages() {
            return images;
        }
    }
}
