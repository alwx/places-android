package me.alwx.places.data.repositories;

import android.support.annotation.NonNull;

import java.util.List;

import me.alwx.places.data.models.Place;
import rx.Observable;

/**
 * @author alwx
 * @version 1.0
 */

public interface PlacesDataSource {
    Observable<List<Place>> getPlaces();

    void savePlace(@NonNull Place place);

    void refreshPlaces();
}
