package pub.tanzby.lab9.factory;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.tanzby.lab9.BuildConfig;
import pub.tanzby.lab9.model.GitHub;
import pub.tanzby.lab9.model.Repos;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tan on 2017/12/12.
 */

public final class ServiceFactory {

    public static final String API_URL = "https://api.github.com";
    /**
     * retrofit对象提供相应的访问Github用户名以及其repos的的API
     */
    public interface GitHubService{
        @GET("/users/{user}")
        Observable<GitHub> getUser(@Path("user") String user);

        @GET("users/{user}/repos")
        Observable< List<Repos> > getUserRepos(@Path("user") String user);
    }

    /**
     * 使用特定的参数设置创建一个Retrofit客户端
     * @return 创建的Retrofit客户端
     */
    public static Retrofit createRetrofit()
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return new Retrofit.Builder().baseUrl(API_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
