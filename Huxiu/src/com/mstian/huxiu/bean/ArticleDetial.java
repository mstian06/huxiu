/*
 * @Title:	ArticleDetial.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-16 下午4:48:34 
 */
package com.mstian.huxiu.bean;

public class ArticleDetial extends BaseType{
    
    private String title;
    private String author;
    private int author_id;
    private long dateline;
    private String content;
    private int aid;
    private String url;
    private String pic;
    private String summary;
    private String img;
    private int comment_num;
    private int like_num;
    private int dislike_num;
    private int is_fav;
    private int is_like;
    private int apply;
    private int grade;
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public long getDateline() {
        return dateline;
    }

    public String getContent() {
        return content;
    }

    public int getAid() {
        return aid;
    }

    public String getUrl() {
        return url;
    }

    public String getPic() {
        return pic;
    }

    public String getSummary() {
        return summary;
    }

    public String getImg() {
        return img;
    }

    public int getComment_num() {
        return comment_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public int getDislike_num() {
        return dislike_num;
    }

    public int getIs_fav() {
        return is_fav;
    }

    public int getIs_like() {
        return is_like;
    }

    public int getApply() {
        return apply;
    }

    public int getGrade() {
        return grade;
    }

    public static class ArticleDetialRequestData {
        private int aid;
        private int result;
        private ArticleDetial content;
        public int getAid() {
            return aid;
        }
        public int getResult() {
            return result;
        }
        public ArticleDetial getContent() {
            return content;
        }
    }
}
