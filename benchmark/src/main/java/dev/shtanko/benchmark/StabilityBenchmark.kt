package dev.shtanko.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@OptIn(ExperimentalMetricApi::class)
class StabilityBenchmark : AbstractBenchmark(StartupMode.WARM) {

    @Test
    fun stabilityCompilationFull() = benchmark(CompilationMode.Full())

    override val metrics: List<Metric> = listOf(
        FrameTimingMetric(),
        TraceSectionMetric("user_item", TraceSectionMetric.Mode.Sum)
    )

    override fun MacrobenchmarkScope.measureBlock() {
        scrollDown()
    }

    override fun MacrobenchmarkScope.setupBlock() {
        pressHome()
        startActivityAndWait()
    }
}

fun MacrobenchmarkScope.scrollDown() {
    device.wait(Until.findObject(By.res("user_list")), 5000)
    val list = device.findObject(By.res("user_list"))
    device.waitForIdle()
    list.setGestureMargin(device.displayWidth / 5)
    repeat(2) {
        list.fling(Direction.DOWN)
        list.fling(Direction.UP)
    }
}
