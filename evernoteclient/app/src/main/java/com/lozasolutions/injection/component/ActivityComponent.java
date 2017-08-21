package com.lozasolutions.injection.component;

import dagger.Subcomponent;
import com.lozasolutions.features.detail.DetailActivity;
import com.lozasolutions.features.main.MainActivity;
import com.lozasolutions.injection.PerActivity;
import com.lozasolutions.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);
}
