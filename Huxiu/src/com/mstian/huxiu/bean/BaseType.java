
package com.mstian.huxiu.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Issac on 7/18/13.
 */
public class BaseType implements Serializable{
    public String toJson() {
        return new Gson().toJson(this);
    }
}
