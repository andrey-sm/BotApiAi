package pro.smartum.botapiai.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;
import pro.smartum.botapiai.retrofit.controller.FacebookController;
import pro.smartum.botapiai.retrofit.controller.SkypeController;
import pro.smartum.botapiai.retrofit.controller.SlackController;
import pro.smartum.botapiai.retrofit.controller.TelegramController;
import pro.smartum.botapiai.retrofit.error.RxErrorHandlingCallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Service
public class RetrofitClient {

    private static final String ROOT = "http://localhost:8080/";

    private final FacebookController facebookController;
    private final SkypeController skypeController;
    private final SlackController slackController;
    private final TelegramController telegramController;

    private RetrofitClient() {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ROOT)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(createGson()));

        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        initRequestInterceptor(okHttpClientBuilder);
        okHttpClientBuilder.followRedirects(false);
        initHttpLog(okHttpClientBuilder);

        final Retrofit retrofit = retrofitBuilder.client(okHttpClientBuilder.build()).build();

        facebookController = retrofit.create(FacebookController.class);
        skypeController = retrofit.create(SkypeController.class);
        slackController = retrofit.create(SlackController.class);
        telegramController = retrofit.create(TelegramController.class);
    }

    private void initRequestInterceptor(OkHttpClient.Builder builder) {
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder().header(ACCEPT, APPLICATION_JSON_VALUE);
            requestBuilder.header(CONTENT_TYPE, APPLICATION_JSON_VALUE);

            requestBuilder.method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }

    private void initHttpLog(OkHttpClient.Builder builder) {
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }

    public FacebookController getFacebookController() {
        return facebookController;
    }

    public SkypeController getSkypeController() {
        return skypeController;
    }

    public SlackController getSlackController() {
        return slackController;
    }

    public TelegramController getTelegramController() {
        return telegramController;
    }
}