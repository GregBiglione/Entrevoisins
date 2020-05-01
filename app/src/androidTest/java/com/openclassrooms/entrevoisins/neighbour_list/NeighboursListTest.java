
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;
    private int mPosition = 0;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService mApiService;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp()
    {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mApiService = DI.getNeighbourApiService();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(withId(R.id.list_neighbours))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(withId(R.id.list_neighbours))
                .perform(actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(withId(R.id.list_neighbours))
                .check(withItemCount(ITEMS_COUNT-1));
    }


    @Test
    public void goToDetailedProfile_onClick()
    {
       //cliquer sur l'élément en identifiant le container parent et ses enfants
       onView(withId(R.id.list_neighbours))
               .perform(actionOnItemAtPosition(mPosition, click()));
    }

   @Test
   public void onDetailedProfileLaunch_textView_containName()
   {
       //cliquer sur l'élément en identifiant le container parent et ses enfants
       onView(withId(R.id.list_neighbours))
               .perform(actionOnItemAtPosition(mPosition, click()));

       //vérifier que le neighbourName est affiché sur l'écran DetailedProfile
       onView(withId(R.id.firstName))
               .check(matches(withText(mApiService.getNeighbours().get(mPosition).getName())));
   }

   @Test
   public void favoritePage_showNeighbours_onlyIfIsFav()
   {
       //cliquer sur l'élément en identifiant le container parent et ses enfants
       onView(withId(R.id.list_neighbours))
               .perform(actionOnItemAtPosition(mPosition, click()));

       //cliquer sur l'étoile pour vérifier le changement de couleur
       onView(withId(R.id.star_button))
               .perform(click());

       //retour à la page d'accueil
       pressBack();

       //cliquer sur l'onglet favori
       onView(withContentDescription("Favorites"))
               .perform(click());

       //vérifier l'affichage des favoris
       onView(withId(R.id.favorite_list))
               .check(matches(isDisplayed()));

//mApiService.getFavorites()
   }
}