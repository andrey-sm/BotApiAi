package pro.smartum.botapiai.retrofit.controller;

import pro.smartum.botapiai.retrofit.rq.FbReplyRq;
import pro.smartum.botapiai.retrofit.rs.FbUserInfoRs;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface FacebookController {

    @POST
    Call<Object> reply(@Url String fbUrl, @Body FbReplyRq fbReplyRq);

    @GET
    Call<FbUserInfoRs> getUserProfile(@Url String fbUrl);
}
