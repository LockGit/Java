package com.lock.demo.tools;

import com.lock.demo.entity.JsonResponse;

/**
 * @author Lock
 * @date 2018/4/30 23:19
 */
public class Json {


    public static JsonResponse success(String msg) {
        JsonResponse response = new JsonResponse();
        response.setCode(1);
        response.setMsg(msg);
        return response;
    }


    public static JsonResponse error(String msg) {
        JsonResponse response = new JsonResponse();
        response.setCode(0);
        response.setMsg(msg);
        return response;
    }

}
