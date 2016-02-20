package io.github.plastix.forage.ui.compass;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.BaseFragmentActivity;

/**
 * Activity that represents the compass screen of the app. This is a container activity
 * for {@link CompassFragment}.
 */
public class CompassActivity extends BaseFragmentActivity<CompassFragment> {

    private static final String COMPASS_FRAG = "io.github.plastix.forage.ui.compass.compassfragment";

    @IdRes
    private static final int COMPASS_FRAME_ID = R.id.compass_content_frame;

    @Bind(R.id.compass_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        setSupportActionBar(toolbar);

        getDelegate().getSupportActionBar().setDisplayShowHomeEnabled(true);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected String getFragmentTag() {
        return COMPASS_FRAG;
    }

    @Override
    protected CompassFragment getFragmentInstance() {
        return new CompassFragment();
    }

    @Override
    protected int getContainerViewId() {
        return COMPASS_FRAME_ID;
    }
}
