package ru.momentum.sklad;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.momentum.sklad.api.NomenApi;
import ru.momentum.sklad.api.ServiceGenerator;

/**
 * Created by chaichukau on 02.02.18.
 */

public class App extends Application {
    private static NomenApi nomenApi;

    @Override
    public void onCreate() {
        super.onCreate();

        nomenApi = ServiceGenerator.createService(NomenApi.class, "obmen", "Obmen1234");
    }

    public static NomenApi getApi() {
        return nomenApi;
    }
}
