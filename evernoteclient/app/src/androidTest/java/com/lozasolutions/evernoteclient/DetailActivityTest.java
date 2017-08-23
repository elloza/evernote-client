package com.lozasolutions.evernoteclient;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.evernote.edam.type.Note;
import com.lozasolutions.evernoteclient.common.TestComponentRule;
import com.lozasolutions.evernoteclient.common.TestDataFactory;
import com.lozasolutions.evernoteclient.data.model.response.Pokemon;
import com.lozasolutions.evernoteclient.data.model.response.Statistic;
import com.lozasolutions.evernoteclient.features.detail.DetailActivity;
import com.lozasolutions.evernoteclient.util.ErrorTestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import io.reactivex.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());

    public final ActivityTestRule<DetailActivity> detailActivityTestRule =
            new ActivityTestRule<>(DetailActivity.class, false, false);

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public TestRule chain = RuleChain.outerRule(component).around(detailActivityTestRule);

    @Test
    public void checkNoteDisplays() {
        Note note = TestDataFactory.makeNote("id");
        stubDataManagerGetPokemon(Single.just(note));
        detailActivityTestRule.launchActivity(
                DetailActivity.getStartIntent(InstrumentationRegistry.getContext(), note.name));

        for (Statistic stat : note.stats) {
            onView(withText(stat.stat.name)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkErrorViewDisplays() {
        stubDataManagerGetPokemon(Single.error(new RuntimeException()));
        Pokemon pokemon = TestDataFactory.makeNote("id");
        detailActivityTestRule.launchActivity(
                DetailActivity.getStartIntent(InstrumentationRegistry.getContext(), pokemon.name));
        ErrorTestUtil.checkErrorViewsDisplay();
    }

    public void stubDataManagerGetPokemon(Single<Pokemon> single) {
        when(component.getMockApiManager().getPokemon(anyString())).thenReturn(single);
    }
}
