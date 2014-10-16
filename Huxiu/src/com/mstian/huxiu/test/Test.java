/*
 * @Title:	Test.java 
 * @author:	mstian <maoshengtian@gmail.com>
 * @data:	2014-10-15 下午2:43:56 
 */
package com.mstian.huxiu.test;

import android.content.Context;

import com.google.gson.Gson;
import com.mstian.huxiu.AppData;
import com.mstian.huxiu.bean.Articles;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test {

    public static void parseJson()
    {
        Articles.PortalRequestData tmpData = null;
        try {
            String json = getFromAssets("test.txt", AppData.getContext());
            tmpData = new Gson().fromJson(json, Articles.PortalRequestData.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        if (tmpData != null)
        {
            System.out.println(tmpData.toString());
        }
    }
    
    public static String getFromAssets(String fileName, Context context)
    { 
        try 
        { 
             InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName)); 
             BufferedReader bufReader = new BufferedReader(inputReader);
             String line="";
             String Result="";
             
             while((line = bufReader.readLine()) != null)
                Result += line;
             return Result;
        }
        catch (Exception e) 
        { 
            e.printStackTrace(); 
            return "";
        }
    } 
}
