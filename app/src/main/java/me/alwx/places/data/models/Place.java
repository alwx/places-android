package me.alwx.places.data.models;

import android.database.Cursor;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

import me.alwx.places.data.db.Db;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@AutoValue
public abstract class Place implements PlaceModel, Parcelable {
    public abstract Address address();

    public static final RowMapper<Place> MAPPER = new RowMapper<Place>() {
        @NonNull
        @Override
        public Place map(@NonNull Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String title = Db.getString(cursor, TITLE);
            String description = Db.getString(cursor, DESCRIPTION);
            String phone = Db.getString(cursor, PHONE);
            Address address = Address.MAPPER.map(cursor);
            return new AutoValue_Place(id, title, description, phone, address);
        }
    };

    /**
     * Returns an SQL expression to select all from places table.
     *
     * @return SQL expression as a string
     */
    public static String selectAll() {
        return String.format(
                "SELECT * FROM %1$s JOIN %3$s ON (%1$s.%2$s = %3$s.%4$s);",
                TABLE_NAME,
                ID,
                Address.TABLE_NAME,
                Address.ID
        );
    }

    public static TypeAdapter<Place> typeAdapter(Gson gson) {
        return new AutoValue_Place.GsonTypeAdapter(gson);
    }
}
