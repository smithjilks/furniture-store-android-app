package com.smith.furniturestore

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smith.furniturestore.ui.auth.LoginFragment
import com.smith.furniturestore.ui.auth.RegisterFragment
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupTests {

    @Test
    fun testPasswordValidation() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_password_edit_text))
            .check(matches(hasErrorText("Password should be greater than 8 characters")))

        onView(withId(R.id.signup_password_edit_text))
            .perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
//        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_password_edit_text))
            .check(matches(hasErrorText("Password should be greater than 8 characters")))


    }

    @Test
    fun testConfirmPasswordValidation() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_confirm_password_edit_text))
            .check(matches(hasErrorText("Password should be greater than 8 characters")))

        onView(withId(R.id.signup_confirm_password_edit_text))
            .perform(typeText("abcd")).perform(ViewActions.closeSoftKeyboard())
//        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_confirm_password_edit_text))
            .check(matches(hasErrorText("Passwords do not match")))

    }

    @Test
    fun testConfirmPasswordMatchesButShort() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_confirm_password_edit_text))
            .perform(typeText("1234")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_password_edit_text))
            .perform(typeText("1234")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_confirm_password_edit_text))
            .check(matches(hasErrorText("Password should be greater than 8 characters")))
        onView(withId(R.id.signup_password_edit_text))
            .check(matches(hasErrorText("Password should be greater than 8 characters")))

    }

    @Test
    fun testPasswordsMatchAndGreaterThan8() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_confirm_password_edit_text))
            .perform(typeText("1234567890")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_password_edit_text))
            .perform(typeText("1234567890")).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.signup_button)).perform(click())
        onView(withId(R.id.signup_confirm_password_edit_text))
            .check(matches(not(hasErrorText(""))))

    }

    @Test
    fun testSignupButtonDisplayed() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testSignupButtonIsEnabled() {
        launchFragmentInContainer<RegisterFragment>()
        onView(withId(R.id.signup_button)).check(matches(not(isNotEnabled())))
    }
}