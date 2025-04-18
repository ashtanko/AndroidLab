package dev.shtanko.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import org.junit.Rule

abstract class AbstractBenchmark(
    protected val startupMode: StartupMode = StartupMode.WARM,
    protected val iterations: Int = 10,
) {
    @get:Rule
    val rule = MacrobenchmarkRule()

    abstract val metrics: List<Metric>

    open fun MacrobenchmarkScope.setupBlock() {}
    abstract fun MacrobenchmarkScope.measureBlock()

    fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "dev.shtanko.androidlab",
            metrics = metrics,
            compilationMode = compilationMode,
            startupMode = startupMode,
            iterations = iterations,
            setupBlock = { setupBlock() },
            measureBlock = { measureBlock() }
        )
    }
}
