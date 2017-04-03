package me.alwx.places.data.components;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import me.alwx.places.data.modules.NetworkModule;
import okhttp3.OkHttpClient;

/**
 * @author alwx
 * @version 1.0
 */
@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {
    Gson gson();

    OkHttpClient okHttpClient();
}
