package com.mstian.huxiu.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.mstian.huxiu.AppData;
import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.Articles;
import com.mstian.huxiu.database.OpArticlesDataHelper;
import com.mstian.huxiu.ui.ArticleActivity;
import com.mstian.huxiu.ui.MainActivity;
import com.mstian.huxiu.ui.adapter.ArticlesAdapter;
import com.mstian.huxiu.vendor.HuxiuApi;

import java.util.ArrayList;

public class PortalListFragment extends BasePageListFragment<Articles.PortalRequestData> implements
LoaderManager.LoaderCallbacks<Cursor> {
    
    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";

    private OpArticlesDataHelper mDataHelper;

    public static PortalListFragment newInstance() {
        PortalListFragment fragment = new PortalListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        mDataHelper = new OpArticlesDataHelper(AppData.getContext(), ((MainActivity)getActivity()).getMenus());

        getLoaderManager().initLoader(0, null, this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Articles article = (Articles) getAdapter().getItem(position - mListView.getHeaderViewsCount());
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });

        return contentView;
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDataHelper.getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getAdapter().changeCursor(data);
        if (data != null && data.getCount() == 0) {
            loadFirstPage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getAdapter().changeCursor(null);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_shot;
    }

    @Override
    protected CursorAdapter getAdapter() {
        return (CursorAdapter) super.getAdapter();
    }

    @Override
    protected BaseAdapter newAdapter() {
        return new ArticlesAdapter(getActivity(), mListView);
    }

    @Override
    protected void processData(Articles.PortalRequestData response) {
        mPage = response.getPage();
        if (mPage == 1) {
            mDataHelper.deleteAll();
        }
        ArrayList<Articles> articles = response.getContent();
        mDataHelper.bulkInsert(articles);
    }

    @Override
    protected String getUrl(int page) {
        return String.format(HuxiuApi.URL_PORTAL_LIST, ((MainActivity)getActivity()).getMenus().getUrl(), page);
    }

    @Override
    protected Class getResponseDataClass() {
        return Articles.PortalRequestData.class;
    }
}