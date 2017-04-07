package me.alwx.places.ui.places;


import me.alwx.places.data.repositories.PlacesRepository;
import me.alwx.places.ui.Presenter;

/**
 * @author alwx
 * @version 1.0
 */
public class PlacesActivityPresenter implements Presenter {
    private PlacesActivity activity;
    private PlacesRepository placesRepository;

    public PlacesActivityPresenter(PlacesActivity activity,
                                   PlacesRepository placesRepository) {
        this.activity = activity;
        this.placesRepository = placesRepository;
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
