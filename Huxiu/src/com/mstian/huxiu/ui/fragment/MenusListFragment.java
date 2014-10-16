/*
 * @Title:	MenusListFragment.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-15 下午5:55:50 
 */
package com.mstian.huxiu.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.Menus.MenusRequestData;
import com.mstian.huxiu.ui.MainActivity;
import com.mstian.huxiu.ui.adapter.MenusListAdapter;

public class MenusListFragment extends Fragment{

    private ListView mListView;
    private MenusListAdapter mAdapter;
    private MainActivity mActivity;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        
        View contentView = inflater.inflate(R.layout.fragment_drawer, null);
        mActivity = (MainActivity)getActivity();
        mListView = (ListView) contentView.findViewById(R.id.listView);
        mAdapter = new MenusListAdapter(mListView, MenusListFragment.this);
        mListView.setAdapter(mAdapter);
        
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.setItemChecked(position, true);
                mActivity.setCurrentMenu(mAdapter.getItem(position));
            }
        });
        
        return contentView;
        
    }
    
    public MenusRequestData getMenusRequestData() {
        return ((MainActivity)getActivity()).getMenusRequestData();
    }
    
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
