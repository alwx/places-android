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
 * @author alwx
 * @version 1.0
 */
@AutoValue
public abstract class Geodata implements GeodataModel, Parcelable {
    public static final RowMapper<Geodata> MAPPER = new RowMapper<Geodata>() {
        @NonNull
        @Override
        public Geodata map(@NonNull Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            float latitude = Db.getFloat(cursor, LATITUDE);
            float longitude = Db.getFloat(cursor, LONGITUDE);
            return new AutoValue_Geodata(id, latitude, longitude);
        }
    };

    public static double calculateDistance(@Nullable Geodata geodata,
                                           @Nullable Location location) {
        if (geodata != null && location != null
                && geodata.latitude() != null && geodata.longitude() != null) {
            int earthR = 6378137;
            double dLat = Math.toRadians(location.getLatitude() - geodata.latitude());
            double dLng = Math.toRadians(location.getLongitude() - geodata.longitude());
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(geodata.latitude()))
                    * Math.cos(Math.toRadians(location.getLatitude()))
                    * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            return earthR * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        } else {
            return -1d;
        }
    }

    public static String selectAll() {
        return String.format("SELECT * FROM %1$s;", TABLE_NAME);
    }

    public static Builder builder() {
        return new AutoValue_Geodata.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(long id);
        public abstract Builder setLatitude(Float latitude);
        public abstract Builder setLongitude(Float longitude);
        public abstract Geodata build();
    }
}
