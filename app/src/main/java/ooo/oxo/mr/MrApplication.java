/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ooo.oxo.mr;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Locale;

import ooo.oxo.mr.model.Color;
import ooo.oxo.mr.model.Image;
import ooo.oxo.mr.net.ColorTypeAdapter;
import ooo.oxo.mr.net.LoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class MrApplication extends Application {

    private final HashMap<Class, Object> apis = new HashMap<>();

    private OkHttpClient httpClient;

    private Retrofit retrofit;

    public static MrApplication from(Context context) {
        return (MrApplication) context.getApplicationContext();
    }

    private String buildAcceptLanguage() {
        Locale locale = Locale.getDefault();
        return String.format("%s-%s,%s;q=0.8,en-US;q=0.6,en;q=0.4",
                locale.getLanguage(), locale.getCountry(), locale.getLanguage());
    }

    private String buildUserAgent() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(metrics);
        return String.format("Mr.Mantou %s Android (%d/%s; %d; %dx%d)",
                BuildConfig.VERSION_NAME,
                Build.VERSION.SDK_INT, Build.VERSION.RELEASE,
                metrics.densityDpi, metrics.widthPixels, metrics.heightPixels);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        MrSharedState.createInstance();

        httpClient = new OkHttpClient();

        httpClient.networkInterceptors().add(chain -> chain.proceed(chain.request().newBuilder()
                .header("Accept-Language", buildAcceptLanguage())
                .header("User-Agent", buildUserAgent())
                .build()));

        if (BuildConfig.DEBUG) {
            httpClient.networkInterceptors().add(new LoggingInterceptor());
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat(Image.DATE_FORMAT_PATTERN)
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .create();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://119.29.45.113:1024/api/")
                .build();
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public <T> T createApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = retrofit.create(service);
            apis.put(service, instance);
        }

        //noinspection unchecked
        return (T) apis.get(service);
    }

}
