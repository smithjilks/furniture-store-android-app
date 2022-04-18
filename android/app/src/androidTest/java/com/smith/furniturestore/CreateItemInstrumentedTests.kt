package com.smith.furniturestore

import android.content.Intent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smith.furniturestore.ui.main.CreateItemFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreateItemInstrumentedTests {


    @Test
    fun testTitleEmpty() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_item_title_edit_text)).check(matches(hasErrorText("Item title/name is required")))

    }

    @Test
    fun testTitleEntered() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_item_title_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_item_title_edit_text)).check(matches(not(hasErrorText(""))))
    }


    @Test
    fun testShortDescriptionEmpty() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_short_desc_edit_text)).check(matches(hasErrorText("Item Short Description is required")))

    }

    @Test
    fun testShortDescriptionEntered() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_short_desc_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_short_desc_edit_text)).check(matches(not(hasErrorText(""))))
    }

    @Test
    fun testLongDescriptionEmpty() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_long_desc_edit_text)).check(matches(hasErrorText("Item Long Description is required")))

    }

    @Test
    fun testLongDescriptionEntered() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_long_desc_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_long_desc_edit_text)).check(matches(not(hasErrorText(""))))
    }

    @Test
    fun testUnitPriceEmpty() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_price_edit_text)).check(matches(hasErrorText("Item price is required")))
    }

    @Test
    fun testUnitPriceEntered() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_price_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_price_edit_text)).check(matches(not(hasErrorText(""))))
    }

    @Test
    fun testItemImageNull() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).perform(click())
        onView(withId(R.id.create_item_error_image_view_text_view)).check(matches(hasErrorText("Item image is required")))
    }

    @Test
    fun testCreateItemButtonDisplayed() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testSelectImageDisplayed() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_image_picker_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testProgressBarIsHidden() {
        launchFragmentInContainer<CreateItemFragment>()
        onView(withId(R.id.create_item_progress_bar)).check(matches(not(isDisplayed())))
    }


}