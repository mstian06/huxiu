package com.mstian.huxiu.ui;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mstian.huxiu.R;
import com.mstian.huxiu.bean.Menus;
import com.mstian.huxiu.bean.Menus.MenusRequestData;
import com.mstian.huxiu.data.GsonRequest;
import com.mstian.huxiu.data.RequestManager;
import com.mstian.huxiu.ui.fragment.BasePageListFragment;
import com.mstian.huxiu.ui.fragment.MenusListFragment;
import com.mstian.huxiu.ui.fragment.PortalListFragment;
import com.mstian.huxiu.util.CommonUtils;
import com.mstian.huxiu.vendor.HuxiuApi;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {
    
    private Menu mMenu;
    
    private PullToRefreshAttacher mPullToRefreshAttacher;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private BasePageListFragment mContentFragment;
    
    private MenusRequestData menuData;
    private Menus currentMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                mMenu.findItem(R.id.action_refresh).setVisible(true);
            }

            public void onDrawerOpened(View drawerView) {
                mMenu.findItem(R.id.action_refresh).setVisible(false);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        PullToRefreshAttacher.Options options = new PullToRefreshAttacher.Options();
        options.headerInAnimation = R.anim.pulldown_fade_in;
        options.headerOutAnimation = R.anim.pulldown_fade_out;
        options.refreshScrollDistance = 0.3f;
        options.headerLayout = R.layout.pulldown_header;
        mPullToRefreshAttacher = new PullToRefreshAttacher(this, options);

        mPullToRefreshAttacher.setRefreshing(false);
        
        loadStartData();
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    protected void loadStartData() {
        RequestManager.addRequest(new GsonRequest<Menus.MenusRequestData>(HuxiuApi.getStartUrl(), Menus.MenusRequestData.class, null,
                new Response.Listener<Menus.MenusRequestData>() {
            @Override
            public void onResponse(final Menus.MenusRequestData response) {
                CommonUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
//                        if (isRefreshFromTop) {
//                            mPullToRefreshAttacher.setRefreshComplete();
//                        } else {
//                            mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
//                        }
                        processMenuData(response);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, R.string.refresh_list_failed,
                        Toast.LENGTH_SHORT).show();
//                if (isRefreshFromTop) {
//                    mPullToRefreshAttacher.setRefreshComplete();
//                } else {
//                    mLoadingFooter.setState(LoadingFooter.State.Idle, 3000);
//                }
            }
        }), this);
    }
    
    private void processMenuData(MenusRequestData response) {
        
        menuData = response;
        
        ArrayList<Menus> menus = response.getMenu();
        
        //fragment menu
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.left_drawer, new MenusListFragment())
                .commit();
        
        //fragment content
        setCurrentMenu(menus.get(0));
    }
    
    public void setCurrentMenu(Menus menu) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (menu == currentMenus)
        {
            return;
        }
        currentMenus = menu;

        FragmentManager fragmentManager = getSupportFragmentManager();
        mContentFragment = new PortalListFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_content, mContentFragment)
                .commit();
    }
    
    public MenusRequestData getMenusRequestData() {
        return menuData;
    }
    
    public Menus getMenus() {
        return currentMenus;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_refresh:
                if (mContentFragment != null) {
                    mContentFragment.loadFirstPageAndScrollToTop();
                }
                return true;
                
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }
}
