package dev.shtanko.androidlab.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService

inline fun executorDispatcher(
    executor: () -> ExecutorService,
): CoroutineDispatcher = executor().asCoroutineDispatcher()
