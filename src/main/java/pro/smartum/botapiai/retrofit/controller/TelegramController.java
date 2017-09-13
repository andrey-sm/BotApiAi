package pro.smartum.botapiai.retrofit.controller;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TelegramController {

    @POST
    Call<Object> reply(@Url String tgUrl);
}
