package io.github.plastix.forage.ui.cachelist;

import com.google.android.gms.common.api.Status;

import io.github.plastix.forage.data.local.model.Cache;
import io.realm.OrderedRealmCollection;

/**
 * Interface implemented by {@link CacheListFragment} to define callbacks used by
 * {@link CacheListPresenter}.
 */
public interface CacheListView {

    void setGeocacheList(OrderedRealmCollection<Cache> caches);

    void onErrorInternet();

    void onErrorFetch();

    void onErrorLocation();

    void setRefreshing();

    void showLocationDialog(Status status);
}
