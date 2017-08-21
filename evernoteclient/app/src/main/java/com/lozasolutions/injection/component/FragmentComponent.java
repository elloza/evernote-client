package com.lozasolutions.injection.component;

import dagger.Subcomponent;
import com.lozasolutions.injection.PerFragment;
import com.lozasolutions.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
