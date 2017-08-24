package com.lozasolutions.evernoteclient;

import com.lozasolutions.evernoteclient.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by shivam on 29/5/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();


    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new DataManager();
    }


}
