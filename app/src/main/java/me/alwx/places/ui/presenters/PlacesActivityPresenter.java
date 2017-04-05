package me.alwx.places.ui.presenters;


import me.alwx.places.data.repositories.PlaceRepository;
import me.alwx.places.ui.activities.PlacesActivity;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivityPresenter implements Presenter {
    private PlacesActivity activity;
    private PlaceRepository placeRepository;

    public PlacesActivityPresenter(PlacesActivity activity,
                                   PlaceRepository placeRepository) {
        this.activity = activity;
        this.placeRepository = placeRepository;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
