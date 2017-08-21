package com.lozasolutions.evernoteclient.injection.component;

import com.lozasolutions.evernoteclient.injection.PerFragment;
import com.lozasolutions.evernoteclient.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
