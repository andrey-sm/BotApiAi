package pro.smartum.botapiai.retrofit.controller;

import io.reactivex.Single;
import pro.smartum.botapiai.retrofit.rq.FbReplyRq;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface FacebookController {

    @POST
    Single<Object> reply(@Url String fbUrl, @Body FbReplyRq fbReplyRq);
}
