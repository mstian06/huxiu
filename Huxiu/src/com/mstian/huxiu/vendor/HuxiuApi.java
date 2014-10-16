package com.mstian.huxiu.vendor;

public class HuxiuApi {

    public static final String BASE_URL = "http://m.api.huxiu.com";
    
//    http://m.api.huxiu.com/start
    public static final String URL_START_STRING = BASE_URL + "/start";
    
//    http://m.api.huxiu.com/portal/1/1?client_ver=5&platform=Android&mid=865372021272213
    public static final String URL_PORTAL_LIST = BASE_URL + "%1$s/%2$d";
    
//    http://m.api.huxiu.com/article/44120?screen_size=1080&client_ver=5&platform=Android&mid=865372021272213
    public static final String URL_ARTICLE_CONTENT = BASE_URL + "/article/%d";
    
//    http://m.api.huxiu.com/comment/44120/1?client_ver=5&platform=Android&mid=865372021272213
    public static final String URL_ARTICLE_COMMENT = BASE_URL + "/comment/%1$d/%2$d";
    
    //parasm
    public static final String URL_PARAM_STRING = "?client_ver=5&platform=Android";
    
    public static String getStartUrl() {
        return URL_START_STRING;
    }
    
    public static String getArticleDetialUrlString (int articleId) {
        return String.format(URL_ARTICLE_CONTENT, articleId);
    }
}
