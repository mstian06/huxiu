/*
 * @Title:	ArticleActivity.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-16 下午4:30:57 
 */
package com.mstian.huxiu.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.TextureView;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.ArticleDetial.ArticleDetialRequestData;
import com.mstian.huxiu.bean.ArticleDetial;
import com.mstian.huxiu.bean.Articles;
import com.mstian.huxiu.data.GsonRequest;
import com.mstian.huxiu.data.RequestManager;
import com.mstian.huxiu.util.CommonUtils;
import com.mstian.huxiu.vendor.HuxiuApi;

import java.text.SimpleDateFormat;

public class ArticleActivity extends FragmentActivity{
    
    private WebView mWebView;
    private Articles mArticles;
    
    private TextView mArticleTitle;
    private TextView mArticleAuthor;
    private TextView mArticleDate;
    private TextView mArticleCommentCount;
    
    
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    public String WEB_STYLE = "<style> img {max-width:340px;} </style>";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_atricle);
        mWebView = (WebView)findViewById(R.id.webview);
        mArticleTitle = (TextView)findViewById(R.id.article_title);
        mArticleAuthor = (TextView)findViewById(R.id.article_author);
        mArticleDate = (TextView)findViewById(R.id.article_date);
        mArticleCommentCount = (TextView)findViewById(R.id.article_commentcount);
        
//        WindowManager windowManager = getWindowManager();    
//        Display display = windowManager.getDefaultDisplay();    
//        mScreenWidth = display.getWidth();
//        mScreenHeight = display.getHeight();
        
        mArticles = (Articles)getIntent().getSerializableExtra("article");
        loadData();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    private void loadData() {
        RequestManager.addRequest(new GsonRequest<ArticleDetialRequestData>(HuxiuApi.getArticleDetialUrlString(mArticles.getAid()), ArticleDetialRequestData.class, null,
                new Response.Listener<ArticleDetialRequestData>() {
            @Override
            public void onResponse(final ArticleDetialRequestData response) {
                CommonUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        processArticleDetialData(response);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ArticleActivity.this, R.string.refresh_list_failed,
                        Toast.LENGTH_SHORT).show();
            }
        }), this);
    }
    
    private void processArticleDetialData(ArticleDetialRequestData response) {
        
        ArticleDetial articles = response.getContent();
        
        mArticleTitle.setText(articles.getTitle());
        mArticleAuthor.setText(articles.getAuthor());
        mArticleDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(articles.getDateline() *1000)));
        mArticleCommentCount.setText(String.valueOf(articles.getComment_num()));
        
//        mWebView.loadUrl(response.getContent().getUrl());
//        mWebView.loadData(response.getContent().getContent(), "text/html", "gb2312") ;
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        String contentString = WEB_STYLE + articles.getContent();
        mWebView.loadData(contentString, "text/html; charset=UTF-8", null);//这种写法可以正确解码
    }
}
