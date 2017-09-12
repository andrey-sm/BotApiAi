package pro.smartum.botapiai.retrofit.error;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);
        boolean isSingle = rawType == Single.class;
        boolean isCompletable = rawType == Completable.class;
        if (rawType != Observable.class && !isSingle && !isCompletable) {
            return null;
        }

        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit), isSingle, isCompletable);
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
        private final Retrofit retrofit;
        private final CallAdapter<R, Object> wrapped;
        private boolean isSingle;
        private boolean isCompletable;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<R, Object> wrapped, boolean isSingle, boolean isCompletable) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
            this.isSingle = isSingle;
            this.isCompletable = isCompletable;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Object adapt(Call<R> call) {
            if (isSingle) {
                return ((Single<R>) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, Single<? extends R>>() {
                    @Override
                    public Single<? extends R> apply(Throwable throwable) throws Exception {
                        return Single.error(asRetrofitException(throwable));
                    }
                });
            } else if (isCompletable) {
                return ((Completable) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, Completable>() {
                    @Override
                    public Completable apply(Throwable throwable) throws Exception {
                        return Completable.error(asRetrofitException(throwable));
                    }
                });
            } else {
                return ((Observable) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, Observable>() {
                    @Override
                    public Observable apply(Throwable throwable) throws Exception {
                        return Observable.error(asRetrofitException(throwable));
                    }
                });
            }
        }

        private RetrofitException asRetrofitException(Throwable throwable) {

            // We had non-200 http error
            if (throwable instanceof retrofit2.HttpException) {
                retrofit2.HttpException httpException = (retrofit2.HttpException) throwable;
                Response response = httpException.response();
                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
            }

            // A network error happened
            if (throwable instanceof IOException) {
                return RetrofitException.networkError((IOException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable);
        }
    }
}

