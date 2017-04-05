package me.alwx.places.data.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Place implements PlaceModel, Parcelable {
    private static final Factory<Place> FACTORY = new Factory<>(AutoValue_Place::new);

    public static final RowMapper<Place> SELECT_ALL_MAPPER = FACTORY.selectAllMapper();

    public static String selectAll() {
        return FACTORY.SelectAll().statement;
    }

    public static void insert(SQLiteOpenHelper dbOpenHelper, Place place) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Place.InsertRow insertRow = new Place.InsertRow(db);
        insertRow.bind(place.title(), place.description(), place.phone());
        insertRow.program.executeInsert();
    }

    public static void deleteAll(SQLiteOpenHelper dbOpenHelper) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        db.execSQL(DELETEALL);
    }

    public static TypeAdapter<Place> typeAdapter(Gson gson) {
        return new AutoValue_Place.GsonTypeAdapter(gson);
    }
}
