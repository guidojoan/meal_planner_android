package com.astutify.mealplanner.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.not

fun visibleViews(vararg viewIds: Int) {
    viewIds.forEach {
        onView(withId(it))
            .check(matches(isDisplayed()))
    }
}

fun notVisibleViews(vararg viewIds: Int) {
    viewIds.forEach {
        onView(withId(it))
            .check(matches(not(isDisplayed())))
    }
}
