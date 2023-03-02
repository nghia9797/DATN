package com.bonsai.common;

public class Contants {

    public static interface UserStatus{
        public static Integer ACTIVE = 0;
        public static Integer BLOCK = 1;
    }

    public static interface AdminStatus{
        public static Integer ACTIVE = 0;
        public static Integer BLOCK = 1;
    }

    public static interface TreeStatus{
        public static Integer AVAILABLE = 0;
        public static Integer RUN_OUT = 1;
    }

    public static interface ShiperStatus{
        public static Integer ACTIVE = 0;
        public static Integer BLOCK = 1;
        public static Integer WORKING = 2;
    }

    public static interface Message{
        public static String NOT_LOGIN = "Not Login!";
        public static String NOT_ALLOW = "Not Permission!";
        public static String SUCCESS = "Action Success!";
        public static String FAILURE = "Action Failure!";
        public static String SERVER_ERROR = "Server Error!";
    }

    public static interface StatusCode{
        public static Integer OK = 200;
        public static Integer Error = 201;
        public static Integer NOT_ALLOW = 403;
        public static Integer SERVER_ERROR = 500;
        public static Integer NOT_LOGIN = 402;
        public static Integer NOT_LOGIN_ADMIN = 401;
    }

    public static interface BillStatus{
        public static Integer CREATE = 0;
        public static Integer CONFIRM = 1;
        public static Integer SHIP = 2;
        public static Integer COMPLETE = 3;
        public static Integer CANCEL = 4;
    }
}
