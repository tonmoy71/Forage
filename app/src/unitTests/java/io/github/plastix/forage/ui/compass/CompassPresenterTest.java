package io.github.plastix.forage.ui.compass;

import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.concurrent.TimeUnit;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.RxSchedulersOverrideRule;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.util.LocationUtils;
import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(ForageRoboelectricUnitTestRunner.class)
@PrepareForTest(Status.class)
public class CompassPresenterTest {

    @Rule
    public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    private CompassPresenter compassPresenter;

    @Mock
    private CompassView view;

    @Mock
    private LocationInteractor locationInteractor;

    @Mock
    private AzimuthInteractor azimuthInteractor;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        compassPresenter = new CompassPresenter(azimuthInteractor, locationInteractor);
        compassPresenter.onViewAttached(view);
    }

    @Test
    public void updateCompass_updatesView() throws InterruptedException {
        Location target = LocationUtils.buildLocation(0, 0);

        compassPresenter.setTargetLocation(target);
        when(azimuthInteractor.getAzimuthObservable()).thenReturn(Observable.just(0f));

        Location location = LocationUtils.buildLocation(0, 0);
        when(locationInteractor.getLocationObservable(anyLong())).thenReturn(Observable.just(location));

        Status status = PowerMockito.mock(Status.class);
        when(status.getStatusCode()).thenReturn(LocationSettingsStatusCodes.SUCCESS);
        when(locationInteractor.getLocationSettingStatus()).thenReturn(Single.just(status));

        compassPresenter.startCompass();

        // Wait for the compass presenter to update the view
        // We need this because we're applying backpressure operators to the observable
        TimeUnit.MILLISECONDS.sleep(500);

        verify(view, times(1)).setDistance(anyFloat());
        verify(view, times(1)).setAccuracy(anyFloat());
        verify(view, times(1)).rotateCompass(anyFloat());
    }
}
