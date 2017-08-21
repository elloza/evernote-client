package com.lozasolutions.evernoteclient.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.lozasolutions.evernoteclient.common.injection.module.ApplicationTestModule;
import com.lozasolutions.evernoteclient.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
