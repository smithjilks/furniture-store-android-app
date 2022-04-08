package com.smith.furniturestore

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smith.furniturestore.ui.auth.LoginFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginTests {


    @Test
    fun testEmailValidation() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.login_email_edit_text)).check(matches(hasErrorText("Enter a valid email address")))

        onView(withId(R.id.login_email_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.login_email_edit_text)).check(matches(hasErrorText("Enter a valid email address")))

        onView(withId(R.id.login_email_edit_text)).perform(typeText("johndoe@gmail.com")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.login_password_edit_text)).check(matches(not(hasErrorText(""))))

    }

    @Test
    fun testPasswordValidation() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.login_password_edit_text)).check(matches(hasErrorText("Password should be greater than 8 characters")))

        onView(withId(R.id.login_password_edit_text)).perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
//        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.login_password_edit_text)).check(matches(hasErrorText("Password should be greater than 8 characters")))


    }

    @Test
    fun testLogoDisplayed() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.logo_image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginButtonDisplayed() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testProgressBarIsHidden() {
        launchFragmentInContainer<LoginFragment>()
        onView(withId(R.id.login_progress_bar)).check(matches(not(isDisplayed())))
    }


}