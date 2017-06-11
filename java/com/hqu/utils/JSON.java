package com.hqu.utils;

import com.google.gson.Gson;

/**
 * 自定义JSON类--基于谷歌Gson替换FastJson,解决toJSONString后属性变成小写
 * @author jack
 *
 */
public class JSON {

    private static  Gson gson = GsonBuilderUtil.create();  
    
    public static <T> T parseObject(String text, Class<T> clazz) {
    	
        return gson.fromJson(text, clazz);
    }
    
    public static String toJSONString(Object object) {
        return gson.toJson(object);
    }
}
