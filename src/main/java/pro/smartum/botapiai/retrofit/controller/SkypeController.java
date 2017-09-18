package pro.smartum.botapiai.retrofit.controller;

import pro.smartum.botapiai.dto.GrantType;
import pro.smartum.botapiai.retrofit.rq.SkypeReplyRq;
import pro.smartum.botapiai.retrofit.rs.SkypeTokenRs;
import retrofit2.Call;
import retrofit2.http.*;

import static pro.smartum.botapiai.retrofit.UrlManager.GET_SKYPE_ACCESS_TOKEN;

public interface SkypeController {

    @POST
    Call<Object> reply(@Url String skypeUrl, @Header("Authorization") String skypeAccessToken, @Body SkypeReplyRq skypeReplyRq);

    @FormUrlEncoded
    @POST(GET_SKYPE_ACCESS_TOKEN)
    Call<SkypeTokenRs> fetchAccessToken(@Field("client_id") String client_id, @Field("client_secret") String clientSecret,
                                        @Field("grant_type") GrantType grantType, @Field("scope") String scope);
}
