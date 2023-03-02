package com.bonsai.common;

public class Response {
    public Integer statusCode;
    public String message;
    public Object data;

    public static Response createResponseNotLogin(){
        Response response = new Response();
        response.message = Contants.Message.NOT_LOGIN;
        response.statusCode = Contants.StatusCode.NOT_LOGIN;
        return response;
    }

    public static Response createResponseNotAllow(){
        Response response = new Response();
        response.message = Contants.Message.NOT_ALLOW;
        response.statusCode = Contants.StatusCode.NOT_ALLOW;
        return response;
    }

    public static Response createResponseSuccess(Object data){
        Response response = new Response();
        response.message = Contants.Message.SUCCESS;
        response.statusCode = Contants.StatusCode.OK;
        response.data = data;
        return response;
    }

    public static Response createResponseServerError(){
        Response response = new Response();
        response.message = Contants.Message.SERVER_ERROR;
        response.statusCode = Contants.StatusCode.SERVER_ERROR;
        return response;
    }

    public static Response createResponseError(){
        Response response = new Response();
        response.message = Contants.Message.FAILURE;
        response.statusCode = Contants.StatusCode.Error;
        return response;
    }

    public static Response createResponseError(String message){
        Response response = new Response();
        response.message = message;
        response.statusCode = Contants.StatusCode.Error;
        return response;
    }

    public static Response createResponseNotLoginAdmin(){
        Response response = new Response();
        response.message = Contants.Message.NOT_LOGIN;
        response.statusCode = Contants.StatusCode.NOT_LOGIN_ADMIN;
        return response;
    }
}
