package me.alwx.places.data.network;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@GsonTypeAdapterFactory
public abstract class AutoValueTypeAdapterFactory implements TypeAdapterFactory {
  public static TypeAdapterFactory create() {
    return new AutoValueGson_AutoValueTypeAdapterFactory();
  }
}