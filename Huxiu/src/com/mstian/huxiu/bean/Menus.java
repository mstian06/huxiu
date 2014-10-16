/*
 * @Title:	Menus.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-15 下午5:37:36 
 */
package com.mstian.huxiu.bean;

import java.util.ArrayList;

public class Menus extends BaseType{

    private String url;
    private String name;
    private String icon_dark;
    private String icon_light;
    private int displayer;
    private int is_login;
    
    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getIcon_dark() {
        return icon_dark;
    }

    public String getIcon_light() {
        return icon_light;
    }

    public int getDisplayer() {
        return displayer;
    }

    public int getIs_login() {
        return is_login;
    }
    
    public int getCategoryId() {
        String[] paths = url.split("/");
        
        if (paths != null && paths.length >= 3) {
            return Integer.valueOf(paths[2]);
        } else {
            return 0;
        }
    }

    public static class MenusRequestData {
        private int splash_ver;
        private int about_ver;
        private String splash_url;
        ArrayList<Menus> menu;
        public int getSplash_ver() {
            return splash_ver;
        }
        public int getAbout_ver() {
            return about_ver;
        }
        public String getSplash_url() {
            return splash_url;
        }
        public ArrayList<Menus> getMenu() {
            return menu;
        }
    }
    
}
