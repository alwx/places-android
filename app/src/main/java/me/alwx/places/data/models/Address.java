package me.alwx.places.data.models;

import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

import java.util.LinkedList;
import java.util.List;

import me.alwx.places.data.db.Db;

/**
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
@AutoValue
public abstract class Address implements AddressModel, Parcelable {
    public static final RowMapper<Address> MAPPER = new RowMapper<Address>() {
        @NonNull
        @Override
        public Address map(@NonNull Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String location = Db.getString(cursor, LOCATION);
            String street = Db.getString(cursor, STREET);
            String city = Db.getString(cursor, CITY);
            String post_code = Db.getString(cursor, POST_CODE);
            String country = Db.getString(cursor, COUNTRY);
            return new AutoValue_Address(
                    id, location, street, city, post_code, country
            );
        }
    };

    public static TypeAdapter<Address> typeAdapter(Gson gson) {
        return new AutoValue_Address.GsonTypeAdapter(gson);
    }

    /**
     * Returns a string representation of Address.
     * The difference between this method and toString method is that this one
     * returns data in a human-readable form
     *
     * @return address
     */
    public String asString() {
        List<String> list = new LinkedList<>();

        addToList(list, location());
        addToList(list, street());
        addToList(list, city());
        addToList(list, post_code());
        addToList(list, country());

        return TextUtils.join(", ", list);
    }

    /**
     * Helper method that adds field to list if field is not empty.
     *
     * @param list destination list
     * @param field text data
     */
    private void addToList(@NonNull List<String> list, @Nullable String field) {
        if (field != null && !field.isEmpty()) {
            list.add(field);
        }
    }
}
