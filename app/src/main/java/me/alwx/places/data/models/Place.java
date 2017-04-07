package me.alwx.places.data.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Place implements PlaceModel, Parcelable {
    private static final Factory<Place> FACTORY = new Factory<>(new PlaceModel.Creator<Place>() {
        @Override
        public Place create(long _id,
                            @NonNull String title,
                            @NonNull String description,
                            @NonNull String phone) {
            return new AutoValue_Place(_id, title, description, phone);
        }
    });

    public static final RowMapper<Place> SELECT_ALL_MAPPER = FACTORY.selectAllMapper();

    public static String selectAll() {
        return FACTORY.SelectAll().statement;
    }

    public static TypeAdapter<Place> typeAdapter(Gson gson) {
        return new AutoValue_Place.GsonTypeAdapter(gson);
    }
}
