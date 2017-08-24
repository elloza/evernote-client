package com.lozasolutions.evernoteclient;

import com.lozasolutions.evernoteclient.features.detail.DetailMvpView;
import com.lozasolutions.evernoteclient.features.detail.DetailPresenter;
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
public class DetailPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    DetailMvpView mockDetailMvpView;
    @Mock
    DataManager mockDataManager;
    private DetailPresenter detailPresenter;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        detailPresenter.detachView();
    }

}
