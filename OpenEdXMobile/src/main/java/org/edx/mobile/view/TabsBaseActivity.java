package org.edx.mobile.view;

import android.os.Bundle;

import org.edx.mobile.R;
import org.edx.mobile.base.BaseSingleFragmentActivity;
import org.edx.mobile.http.notifications.SnackbarErrorNotification;
import org.edx.mobile.interfaces.RefreshListener;
import org.edx.mobile.interfaces.SnackbarStatusListener;
import org.edx.mobile.util.NetworkUtil;

import de.greenrobot.event.EventBus;

public abstract class TabsBaseActivity extends BaseSingleFragmentActivity
        implements SnackbarStatusListener, RefreshListener {

    private SnackbarErrorNotification snackbarErrorNotification;
    private boolean isFullScreenErrorVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snackbarErrorNotification = new SnackbarErrorNotification(findViewById(R.id.coordinator_layout));
    }

    @Override
    public void hideSnackBar() {
        snackbarErrorNotification.hideError();
    }

    @Override
    public void resetSnackbarVisibility(boolean fullScreenErrorVisibility) {
        this.isFullScreenErrorVisible = fullScreenErrorVisibility;
        final boolean isNetworkConnected = NetworkUtil.isConnected(this);
        if (fullScreenErrorVisibility || isNetworkConnected) {
            snackbarErrorNotification.hideError();
        } else if (!isNetworkConnected) {
            snackbarErrorNotification.showOfflineError(this);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (NetworkUtil.isConnected(this)) {
            snackbarErrorNotification.hideError();
        } else if (!isFullScreenErrorVisible) {
            snackbarErrorNotification.showOfflineError(this);
        }
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(getEvent());
    }

    public abstract Object getEvent();
}
