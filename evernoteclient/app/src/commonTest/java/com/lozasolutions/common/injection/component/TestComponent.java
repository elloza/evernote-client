package com.lozasolutions.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.lozasolutions.common.injection.module.ApplicationTestModule;
import com.lozasolutions.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
