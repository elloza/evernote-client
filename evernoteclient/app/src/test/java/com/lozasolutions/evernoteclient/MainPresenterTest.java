package com.lozasolutions.evernoteclient;

import com.lozasolutions.evernoteclient.features.main.MainMvpView;
import com.lozasolutions.evernoteclient.features.main.MainPresenter;
import com.lozasolutions.evernoteclient.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by ravindra on 24/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    MainMvpView mockMainMvpView;
    @Mock
    DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }

}
