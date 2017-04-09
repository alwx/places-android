package me.alwx.places.data.models.gson;

import java.util.List;

/**
 * This class parses the Geocode response.
 * It is used by GSON.
 *
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class GeocodeResponse {
    private List<Result> results;

    public float getLat() {
        if (results.size() > 0) {
            return results.get(0).geometry.location.lat;
        }
        return 0;
    }

    public float getLng() {
        if (results.size() > 0) {
            return results.get(0).geometry.location.lng;
        }
        return 0;
    }

    private static class Result {
        private Geometry geometry;

        private static class Geometry {
            private Location location;

            private static class Location {
                private float lat;
                private float lng;
            }
        }
    }
}
