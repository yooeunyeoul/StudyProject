package com.dongeul.studyproject

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    lateinit var valueFromFlow: Flow<Int>

    @Test
    fun addition_isCorrect() {
        valueFromFlow = flow<Int> {
            for (i in 1..30) {
                delay(100)
                emit(i)
            }
        }
        runTest {
            val result = valueFromFlow.conflate().onEach { delay(500) }.toList()
            assertThat(listOf(1, 10, 20, 30)).isEqualTo(result)

        }
    }
}