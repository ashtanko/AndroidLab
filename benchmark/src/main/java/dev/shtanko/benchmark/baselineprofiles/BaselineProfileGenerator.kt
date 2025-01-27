package dev.shtanko.benchmark.baselineprofiles

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RequiresApi(Build.VERSION_CODES.P)
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateBaselineProfiles() = rule.collect(
        packageName = "dev.shtanko.androidlab",
        includeInStartupProfile = true,
    ) {
        startActivityAndWait()
    }
}
