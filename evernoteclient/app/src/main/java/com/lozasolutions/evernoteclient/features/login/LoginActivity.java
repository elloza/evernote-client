package com.lozasolutions.evernoteclient.features.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.lozasolutions.evernoteclient.R;
import com.lozasolutions.evernoteclient.features.base.BaseActivity;
import com.lozasolutions.evernoteclient.injection.component.ActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements LoginMvpView, EvernoteLoginFragment.ResultCallback {

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.logoFreedompop)
    ImageView logoFreedompop;
    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;
    @BindView(R.id.progress)
    ProgressBar progress;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    public static void launch(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginButton.setOnClickListener(v -> {
            EvernoteSession.getInstance().authenticate(LoginActivity.this);
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
        });

    }

    @Override
    public void onLoginFinished(boolean successful) {
        if (successful) {
            finish();
        } else {
            loginButton.setEnabled(true);
        }
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        loginPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        loginPresenter.detachView();
    }


    @Override
    public void showError(Throwable error) {
        //TODO
    }
}
