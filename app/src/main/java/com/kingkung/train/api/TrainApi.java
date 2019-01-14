package com.kingkung.train.api;

import com.kingkung.train.bean.CheckOrderData;
import com.kingkung.train.bean.ConfirmSingleForQueueData;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.QueryOrderWaitTimeData;
import com.kingkung.train.bean.QueueCountData;
import com.kingkung.train.bean.ResultOrderForQueueData;
import com.kingkung.train.bean.StatusResult;
import com.kingkung.train.bean.TrainData;
import com.kingkung.train.bean.response.Result;
import com.kingkung.train.bean.response.UamtkResult;
import com.kingkung.train.bean.response.UserNameResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TrainApi {
    @POST(Urls.UAMTK)
    @FormUrlEncoded
    Observable<UamtkResult> uamtk(@Field("appid") String appid);

    @GET(Urls.CAPTCHA)
    Observable<ResponseBody> captcha(@QueryMap Map<String, String> fields);

    @POST(Urls.CAPTCHA_CHECK)
    @FormUrlEncoded
    Observable<Result> captchaCheck(@FieldMap Map<String, String> fields);

    @POST(Urls.LOGIN)
    @FormUrlEncoded
    Observable<Result> login(@FieldMap Map<String, String> fields);

    @POST(Urls.UAMAUTH_CLIENT)
    @FormUrlEncoded
    Observable<UserNameResult> uamauthClient(@FieldMap Map<String, String> fields);

    @GET(Urls.QUERY_TRAIN)
    Observable<StatusResult<TrainData>> queryTrain(@QueryMap Map<String, String> fields);

    @POST(Urls.CHECK_USER)
    @FormUrlEncoded
    Observable<StatusResult<Object>> checkUser(@FieldMap Map<String, String> fields);

    @POST(Urls.SUBMIT_ORDER)
    Observable<StatusResult<Object>> submitOrder(@Body RequestBody body);

    @GET(Urls.INIT_DC)
    Observable<String> initDc(@QueryMap Map<String, String> fields);

    @POST(Urls.GET_PASSENGER)
    @FormUrlEncoded
    Observable<StatusResult<PassengerInfo.PassengerData>> getPassenger(@FieldMap Map<String, String> fields);

    @POST(Urls.CHECK_ORDER_INFO)
    @FormUrlEncoded
    Observable<StatusResult<CheckOrderData>> checkOrderInfo(@FieldMap Map<String, String> fields);

    @POST(Urls.GET_QUEUE_COUNT)
    @FormUrlEncoded
    Observable<StatusResult<QueueCountData>> getQueueCount(@FieldMap Map<String, String> fields);

    @POST(Urls.CONFIRM_SINGLE_FOR_QUEUE)
    @FormUrlEncoded
    Observable<StatusResult<ConfirmSingleForQueueData>> confirmSingleForQueue(@FieldMap Map<String, String> fields);

    @GET(Urls.QUERY_ORDER_WAIT_TIME)
    Observable<StatusResult<QueryOrderWaitTimeData>> queryOrderWaitTime(@QueryMap Map<String, String> fields);

    @POST(Urls.RESULT_ORDER_FOR_QUEUE)
    @FormUrlEncoded
    Observable<StatusResult<ResultOrderForQueueData>> resultOrderForQueue(@FieldMap Map<String, String> fields);
}
