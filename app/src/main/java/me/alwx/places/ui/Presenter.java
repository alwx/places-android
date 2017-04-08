package me.alwx.places.ui;

import android.os.Bundle;

public interface Presenter {
    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();
}