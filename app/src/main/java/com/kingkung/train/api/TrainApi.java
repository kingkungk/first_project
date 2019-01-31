package com.kingkung.train.api;

import com.kingkung.train.bean.CheckOrderData;
import com.kingkung.train.bean.MessageListReslut;
import com.kingkung.train.bean.MessageReslut;
import com.kingkung.train.bean.PassengerInfo;
import com.kingkung.train.bean.QueryOrderWaitTimeData;
import com.kingkung.train.bean.QueueCountData;
import com.kingkung.train.bean.SubmitStatusData;
import com.kingkung.train.bean.TrainData;
import com.kingkung.train.bean.Result;
import com.kingkung.train.bean.UamtkResult;
import com.kingkung.train.bean.UserNameResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

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
    Observable<MessageReslut<TrainData>> queryTrain(@QueryMap Map<String, String> fields);

    @POST(Urls.CHECK_USER)
    @FormUrlEncoded
    Observable<MessageListReslut<Object>> checkUser(@FieldMap Map<String, String> fields);

    @POST(Urls.SUBMIT_ORDER)
    Observable<MessageListReslut<Object>> submitOrder(@Body RequestBody body);

    @GET(Urls.INIT_DC)
    Observable<String> initDc(@QueryMap Map<String, String> fields);

    @POST(Urls.GET_PASSENGER)
    @FormUrlEncoded
    Observable<MessageListReslut<PassengerInfo.PassengerNormal>> getPassenger(@FieldMap Map<String, String> fields);

    @POST(Urls.CHECK_ORDER_INFO)
    @FormUrlEncoded
    Observable<MessageListReslut<CheckOrderData>> checkOrderInfo(@FieldMap Map<String, String> fields);

    @POST(Urls.GET_QUEUE_COUNT)
    @FormUrlEncoded
    Observable<MessageListReslut<QueueCountData>> getQueueCount(@FieldMap Map<String, String> fields);

    @POST(Urls.CONFIRM_SINGLE_FOR_QUEUE)
    @FormUrlEncoded
    Observable<MessageListReslut<SubmitStatusData>> confirmSingleForQueue(@FieldMap Map<String, String> fields);

    @GET(Urls.QUERY_ORDER_WAIT_TIME)
    Observable<MessageListReslut<QueryOrderWaitTimeData>> queryOrderWaitTime(@QueryMap Map<String, String> fields);

    @POST(Urls.RESULT_ORDER_FOR_QUEUE)
    @FormUrlEncoded
    Observable<MessageListReslut<SubmitStatusData>> resultOrderForQueue(@FieldMap Map<String, String> fields);

    @GET(Urls.LOGOUT)
    Observable<String> logout();

    @GET(Urls.INDEX)
    Observable<String> index();

    @GET
    Observable<String> cityCode(@Url String cityCode);

    @POST(Urls.QUERY_PASSENGER)
    @FormUrlEncoded
    Observable<MessageListReslut<PassengerInfo.PassengerData>> queryPassenger(@Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize);
}
