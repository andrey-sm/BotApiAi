package pro.smartum.botapiai.retrofit.controller;

import pro.smartum.botapiai.retrofit.rs.TgPhotosRs;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface TelegramController {

    @POST
    Call<Object> reply(@Url String tgUrl);

    @GET
    Call<TgPhotosRs> getUserPhotos(@Url String tgUrl);

    @GET
    Call<TgPhotosRs> getUserPhotoPath(@Url String tgUrl);
}
