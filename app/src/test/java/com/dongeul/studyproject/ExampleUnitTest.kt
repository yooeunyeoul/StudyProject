package com.dongeul.studyproject

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)

//Given-When-Then
class FLowTest {

    lateinit var valueFromFlow: Flow<Int>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun conflate_test() {
        valueFromFlow = flow<Int> {
            for (i in 1..30) {
                delay(100)
                emit(i)
            }
        }
        runTest {
            valueFromFlow = valueFromFlow
                .onEach { println("emit:$it") }
                .conflate().buffer(onBufferOverflow = BufferOverflow.DROP_LATEST)
//            assertThat(listOf(1, 10, 20, 30)).isEqualTo(result)
            val collectJob = launch {
                valueFromFlow.collect {
                    delay(4000)
                    println("consume$it")
                }
            }
            advanceUntilIdle()


            collectJob.cancel()

        }

    }

    @Test
    fun flowTest_cold() {
        runTest {
            val myFlow = flow<Int> {
                repeat(10) {
                    println(it)
                    emit(it)
                    delay(1000)
                }
            }
            advanceUntilIdle()
            myFlow.collect {
                println("#1st -> ${it}")
            }


            delay(2000)
            myFlow.collect {
                println("#2nd -> ${it}")
            }
        }
    }

    @Test
    fun stateflowTest_hot() {
        runTest {
//            val myFlow = flow<Int> {
//                repeat(10){
//                    println(it)
//                    emit(it)
//                    delay(1000)
//                }
//            }
//
//            val myStateFlow = myFlow.stateIn(
//                scope = testDispatcher,
//                ,
//                initialValue = 1
//            )
        }
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}