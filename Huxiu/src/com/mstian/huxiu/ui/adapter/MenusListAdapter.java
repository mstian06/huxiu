/*
 * @Title:	MenusListAdapter.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-15 下午6:04:04 
 */
package com.mstian.huxiu.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.Menus;
import com.mstian.huxiu.bean.Menus.MenusRequestData;
import com.mstian.huxiu.ui.fragment.MenusListFragment;

public class MenusListAdapter extends BaseAdapter{

    private ListView mListView;
    private Fragment mFragment;
    
    
    public MenusListAdapter(ListView listView, Fragment fragment) {
        // TODO Auto-generated constructor stub
        mListView = listView;
        mFragment = fragment;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        MenusRequestData data = ((MenusListFragment)mFragment).getMenusRequestData();
        if (data != null && data.getMenu() != null)
        {
            int count = 0;
            for (Menus itemMenus : data.getMenu()) {
                if (itemMenus.getIs_login() == 0) {
                    count++;
                }
            }
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public Menus getItem(int position) {
        MenusRequestData data = ((MenusListFragment)mFragment).getMenusRequestData();
        if (data != null && data.getMenu() != null)
        {
            return data.getMenu().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(com.mstian.huxiu.AppData.getContext()).inflate(
                    R.layout.listitem_drawer, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(getItem(position).getName());
        textView.setSelected(mListView.isItemChecked(position));
        return convertView;
    }

}
