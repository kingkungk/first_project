package com.kingkung.train.api;

public class Urls {
    public final static String BASE_URL = "https://kyfw.12306.cn/";

    public final static String UAMTK = "/passport/web/auth/uamtk";

    public final static String CAPTCHA = "/passport/captcha/captcha-image";

    public final static String CAPTCHA_CHECK = "/passport/captcha/captcha-check";

    public final static String LOGIN = "/passport/web/login";

    public final static String UAMAUTH_CLIENT = "/otn/uamauthclient";

    public final static String QUERY_TRAIN = "/otn/leftTicket/queryX";

    public final static String CHECK_USER = "/otn/login/checkUser";

    public final static String SUBMIT_ORDER = "/otn/leftTicket/submitOrderRequest";

    public final static String INIT_DC = "/otn/confirmPassenger/initDc";

    public final static String GET_PASSENGER = "/otn/confirmPassenger/getPassengerDTOs";

    public final static String GET_PASS_CODE_NEW = "/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&0.6253143368566529";

    public final static String CHECK_ORDER_INFO = "otn/confirmPassenger/checkOrderInfo";

    public final static String GET_QUEUE_COUNT = "otn/confirmPassenger/getQueueCount";

    public final static String CONFIRM_SINGLE_FOR_QUEUE = "otn/confirmPassenger/confirmSingleForQueue";

    public final static String QUERY_ORDER_WAIT_TIME = "otn/confirmPassenger/queryOrderWaitTime";

    public final static String RESULT_ORDER_FOR_QUEUE = "otn/confirmPassenger/resultOrderForDcQueue";

    public final static String LOGOUT = "otn/login/loginOut";

    public final static String STATIC_NAME = "otn/resources/js/framework/station_name.js";

    public final static String INDEX = "https://www.12306.cn/index/";

    public final static String QUERY_PASSENGER = "/otn/passengers/query";

    public final static String BASE_TRAIN_URL = "http://192.168.1.136:8890/";

    public final static String UPLOAD_CONFIG = "train/config";
}
