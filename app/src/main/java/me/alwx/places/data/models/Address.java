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
            return new AutoValue_Address(id, location, street, city, post_code, country);
        }
    };

    public static TypeAdapter<Address> typeAdapter(Gson gson) {
        return new AutoValue_Address.GsonTypeAdapter(gson);
    }

    public String asString() {
        List<String> list = new LinkedList<>();

        addToList(list, location());
        addToList(list, street());
        addToList(list, city());
        addToList(list, post_code());
        addToList(list, country());

        return TextUtils.join(", ", list);
    }

    private void addToList(List<String> list, @Nullable String field) {
        if (field != null && !field.isEmpty()) {
            list.add(field);
        }
    }
}
