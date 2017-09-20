package pro.smartum.botapiai.retrofit.controller;

import pro.smartum.botapiai.retrofit.rs.SlackUserInfoRs;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

import static pro.smartum.botapiai.retrofit.UrlManager.GET_SLACK_USER_INFO;

public interface SlackController {

    @POST
    Call<Object> reply(@Url String slackUrl);

    @FormUrlEncoded
    @POST(GET_SLACK_USER_INFO)
    Call<SlackUserInfoRs> getUserProfile(@Field("token") String token, @Field("user") String user);
}
