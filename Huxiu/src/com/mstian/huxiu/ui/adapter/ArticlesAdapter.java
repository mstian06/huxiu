/*
 * @Title:	ArticlesAdapter.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-15 上午11:46:18 
 */
package com.mstian.huxiu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mstian.huxiu.AppData;
import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.Articles;
import com.mstian.huxiu.data.RequestManager;
import com.mstian.huxiu.util.TimeUtils;

import java.text.SimpleDateFormat;


public class ArticlesAdapter extends CursorAdapter{
    
    private LayoutInflater mLayoutInflater;

    private ListView mListView;
    
    private BitmapDrawable mDefaultAvatarBitmap = (BitmapDrawable) AppData.getContext()
            .getResources().getDrawable(R.drawable.default_avatar);

    private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));


    public ArticlesAdapter(Context context, ListView listView) {
        super(context, null, false);
        mLayoutInflater = ((Activity) context).getLayoutInflater();
        mListView = listView;
    }
    
    @Override
    public Articles getItem(int position) {
        mCursor.moveToPosition(position);
        return Articles.fromCursor(mCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.inflate(R.layout.listitem_articles, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = getHolder(view);
        if (holder.imageRequest != null) {
            holder.imageRequest.cancelRequest();
        }

        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
                + mListView.getHeaderViewsCount()));

        Articles shot = Articles.fromCursor(cursor);
        holder.imageRequest = RequestManager.loadImage(shot.getImg(), RequestManager
                .getImageListener(holder.image, mDefaultImageDrawable, mDefaultImageDrawable));
        holder.title.setText(shot.getTitle());
        holder.userName.setText(shot.getAuthor());
        holder.text_comment_count.setText(String.valueOf(shot.getComment_num()));
//        holder.time.setText(TimeUtils.getListTime(shot.getDateline()));
//        holder.time.setText(String.valueOf(shot.getDateline()));
        holder.time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(shot.getDateline() * 1000)));
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class Holder {
        public ImageView image;

        public ImageView avatar;

        public TextView title;

        public TextView userName;

        public TextView text_view_count;

        public TextView text_comment_count;

        public TextView text_like_count;

        public TextView time;

        public ImageLoader.ImageContainer imageRequest;

        public Holder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            userName = (TextView) view.findViewById(R.id.userName);
            text_comment_count = (TextView) view.findViewById(R.id.text_comment_count);
            time = (TextView) view.findViewById(R.id.time);
            
        }
    }
    
}
