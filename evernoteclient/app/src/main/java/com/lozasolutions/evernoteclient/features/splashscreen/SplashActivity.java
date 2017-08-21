package com.lozasolutions.evernoteclient.features.splashscreen;

import android.os.Bundle;

import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.features.base.BaseActivity;
import com.lozasolutions.evernoteclient.features.main.MainActivity;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(splashPresenter.isAuthenticated()){
            startActivity(MainActivity.getStartIntent(this));
        }else{

        }
        finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_splashscreen;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        splashPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        splashPresenter.detachView();
    }


}
