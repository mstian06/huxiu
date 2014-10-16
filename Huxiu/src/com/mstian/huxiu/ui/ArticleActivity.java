/*
 * @Title:	ArticleActivity.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-16 下午4:30:57 
 */
package com.mstian.huxiu.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
//    private Articles mArticles;
    private String mArticleUrl;
    
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
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        
        mArticleTitle = (TextView)findViewById(R.id.article_title);
        mArticleAuthor = (TextView)findViewById(R.id.article_author);
        mArticleDate = (TextView)findViewById(R.id.article_date);
        mArticleCommentCount = (TextView)findViewById(R.id.article_commentcount);
        
        mWebView = (WebView)findViewById(R.id.webview);
        mWebView.setWebViewClient( new WebViewClient() {
            
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    
                    int article_id = -1;
                    String[] partStrings = url.split("/");
                    if (partStrings != null && partStrings.length > 0) {
                        for (int i = 1; i < partStrings.length; i++) {
                            if (partStrings[i-1].equals("article")) {
                                article_id = Integer.valueOf(partStrings[i]);
                            }
                        }
                    }
                    
                    if (article_id == -1) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ArticleActivity.this, ArticleActivity.class);
                        intent.putExtra("url", HuxiuApi.getArticleDetialUrlString(article_id));
                        startActivity(intent);
                    }

                    return true;
                };
            }
        );
        
//        WindowManager windowManager = getWindowManager();    
//        Display display = windowManager.getDefaultDisplay();    
//        mScreenWidth = display.getWidth();
//        mScreenHeight = display.getHeight();
        
        mArticleUrl = (String)getIntent().getStringExtra("url");
        loadData();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    private void loadData() {
        RequestManager.addRequest(new GsonRequest<ArticleDetialRequestData>(mArticleUrl, ArticleDetialRequestData.class, null,
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
