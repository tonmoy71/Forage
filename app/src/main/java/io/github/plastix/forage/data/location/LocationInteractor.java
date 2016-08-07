package io.github.plastix.forage.data.location;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Single;

/**
 * Reactive wrapper around Google Play Location Services.
 */
public class LocationInteractor {

    private static final int LOCATION_FASTEST_INTERVAL_MILLIS = 250;

    private Provider<LocationAsyncEmitter> locationEmitterProvider;
    private Provider<LocationStatusAsyncEmitter> locationAvailableProvider;

    @Inject
    public LocationInteractor(@NonNull Provider<LocationAsyncEmitter> locationEmitterProvider,
                              @NonNull Provider<LocationStatusAsyncEmitter> locationAvailableProvider) {
        this.locationEmitterProvider = locationEmitterProvider;
        this.locationAvailableProvider = locationAvailableProvider;
    }

    /**
     * Gets the updated location using Google Play Location Services.
     * The caller must have location permissions before calling this method!
     *
     * @return An rx.Single that emits the updated location.
     */
    @NonNull
    public Single<Location> getUpdatedLocation() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);

        LocationAsyncEmitter locationEmitter = locationEmitterProvider.get();
        locationEmitter.setLocationRequest(request);

        return Observable.fromAsync(locationEmitter, AsyncEmitter.BackpressureMode.DROP)
                .take(1)
                .toSingle();
    }

    /**
     * Returns an Observable that emits Location events at the specified interval. This uses the Google
     * Play Services Location API.
     * The caller must have location permissions before calling this method!
     *
     * @param intervalInMillis Time between location events in milliseconds.
     * @return Location observable.
     */
    @NonNull
    public Observable<Location> getLocationObservable(long intervalInMillis) {

        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(LOCATION_FASTEST_INTERVAL_MILLIS)
                .setInterval(intervalInMillis);

        LocationAsyncEmitter locationEmitter = locationEmitterProvider.get();
        locationEmitter.setLocationRequest(request);

        return Observable.fromAsync(locationEmitter, AsyncEmitter.BackpressureMode.DROP);
    }

    /**
     * Returns the current Location setting status using the Google Play Services location SettingsAPI.
     *
     * @return rx.Single that emits the current Location Settings {@link Status}.
     */
    @NonNull
    public Single<Status> getLocationSettingStatus() {
        return Observable.fromAsync(locationAvailableProvider.get(),
                AsyncEmitter.BackpressureMode.NONE).take(1).toSingle();
    }

}
