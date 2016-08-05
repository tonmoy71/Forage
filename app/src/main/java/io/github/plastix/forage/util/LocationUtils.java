package io.github.plastix.forage.util;

import android.hardware.GeomagneticField;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationUtils {

    private static double MIN_LATITUDE = -90;
    private static double MAX_LATITUDE = 90;
    private static double MIN_LONGITUDE = -180;
    private static double MAX_LONGITUDE = 180;

    private LocationUtils() {
        throw new UnsupportedOperationException("No Instantiation!");
    }

    /**
     * Helper method for creating a new Location object with the specified latitude and longitude.
     * Android's Location class doesn't have a nice constructor or builder.
     *
     * @param lat Latitude of location.
     * @param lon Longitude of location.
     * @return New location object.
     */
    @NonNull
    public static Location buildLocation(double lat, double lon) {
        Location location = new Location(""); // Blank location provider name
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }

    /**
     * Gets the magnetic declination at the specified location.
     *
     * @param location Current location.
     * @return The declination of the horizontal component of the magnetic
     * field from true north, in degrees (i.e. positive means the
     * magnetic field is rotated east that much from true north).
     */
    public static double getMagneticDeclination(@NonNull Location location) {
        GeomagneticField geoField = new GeomagneticField(
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(),
                System.currentTimeMillis()
        );

        return geoField.getDeclination();
    }

    /**
     * String overloaded version of {@link #isValidLatitude(double)}.
     *
     * @param latitude String Latitude
     * @return True if valid, else false.
     */
    public static boolean isValidLatitude(@NonNull String latitude) {
        try {
            return isValidLatitude(Double.valueOf(latitude));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the specified double is a valid latitude.
     *
     * @param latitude Latitude as double.
     * @return True if valid, else false.
     */
    public static boolean isValidLatitude(double latitude) {
        return latitude <= MAX_LATITUDE && MIN_LATITUDE <= latitude;
    }

    /**
     * String overloaded version of {@link #isValidLongitude(double)}.
     *
     * @param longitude String Latitude
     * @return True if valid, else false.
     */
    public static boolean isValidLongitude(@NonNull String longitude) {
        try {
            return isValidLongitude(Double.valueOf(longitude));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the specified double is a valid longitude.
     *
     * @param longitude Longitude as double.
     * @return True if valid, else false.
     */
    public static boolean isValidLongitude(double longitude) {
        return longitude <= MAX_LONGITUDE && MIN_LONGITUDE <= longitude;
    }

    public static boolean statusLocationEnabled(Status status){
        return status.getStatusCode() == LocationSettingsStatusCodes.SUCCESS;
    }

    public static boolean statusLocationResolutionRequired(Status status){
        return status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED;
    }
}
