package com.feel.jeon.rx_sample.ui.day4

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

class DayFourStateHolder {

    /**
     * 두 개의 Observable 중 한 소스에서 아이템이 발행될 때, 두 Observable에서 가장 최근에 발행한 아이템을 취합하여 하나로 발행하는 연산자입니다.
     *
     * 실무에서 많이 사용되는 연산자 중 하나로, 여러 개의 http 요청에 의한 응답을 하나로 묶어서 처리할 때 사용됩니다.
     */
    fun combinedLatest() {
        Thread(
            Runnable {
                // 1초마다 i값을 방출하는 Observable
                val observable1 = Observable.create<Int> { emitter ->
                    object : Thread() {
                        override fun run() {
                            for (i in 1..5) {
                                emitter.onNext(i)
                                try {
                                    sleep(1000)
                                } catch (ie: InterruptedException) {
                                    ie.printStackTrace()
                                }
                            }
                        }
                    }.start()
                }

// 0.5초마다 문자를 방출하는 Observable
                val observable2 = Observable.create<Char> { emitter ->
                    object : Thread() {
                        override fun run() {
                            for (i in 'a'..'d') {
                                emitter.onNext(i)
                                try {
                                    sleep(500)
                                } catch (ie: InterruptedException) {
                                    ie.printStackTrace()
                                }
                            }
                        }
                    }.start()
                }

                val disposeAble = Observable.combineLatest(
                    observable1,
                    observable2,
                    BiFunction { num, chr -> "$num$chr" }
                ).subscribe {
                    println(it)
                }

                Thread.sleep(5000)
                disposeAble.dispose()
            }
        ).start()
    }

    /**
     * zip 연산자는 여러 Observable을 하나로 결합하여 지정된 함수를 통해 하나의 아이템으로 발행합니다.
     *
     * combineLatest와 비슷해 보이지만 combineLatest 연산자는 가장 최근에 발행한 아이템을 기준으로 결합하는 데 반해
     * zip은 여러 Observable의 발행 순서를 엄격히 지켜 아이템을 결합합니다.
     */
    fun zip() {
        Thread(
            Runnable {
                // 1초마다 i값을 방출하는 Observable
                val observable1 = Observable.create<Int> { emitter ->
                    object : Thread() {
                        override fun run() {
                            for (i in 1..5) {
                                emitter.onNext(i)
                                try {
                                    sleep(1000)
                                } catch (ie: InterruptedException) {
                                    ie.printStackTrace()
                                }
                            }
                        }
                    }.start()
                }

// 0.5초마다 문자를 방출하는 Observable
                val observable2 = Observable.create<Char> { emitter ->
                    object : Thread() {
                        override fun run() {
                            for (i in 'a'..'d') {
                                emitter.onNext(i)
                                try {
                                    sleep(500)
                                } catch (ie: InterruptedException) {
                                    ie.printStackTrace()
                                }
                            }
                        }
                    }.start()
                }

                val disposeAble = Observable.zip(
                    observable1,
                    observable2
                ) { num, chr ->
                    "$num$chr"
                }.subscribe {
                    println(it)
                }

                Thread.sleep(5000)
                disposeAble.dispose()
            }
        ).start()
    }

    /**
     * merge 연산자를 이용하면 여러 Observable을 하나의 Observable처럼 결합하여 사용할 수 있습니다.
     *
     * 여러 Observable이 발행하는 아이템을 발행 시점에 하나의 스트림에 교차해 끼워 넣어 하나의 Observable을 만듭니다.
     * 이때, 데이터가 발행되는 순서로 데이터를 방출합니다.
     */
    fun merge() {
        Thread(
            Runnable {
                val observable1 = Observable.intervalRange(
                    1, // 시작값
                    5, // 발행 횟수
                    0, // 초기 지연
                    100, // 발행 간격
                    TimeUnit.MILLISECONDS // 간격 단위
                ).map { value ->
                    value * 20
                }

                val observable2 = Observable.create { emitter ->
                    object : Thread() {
                        override fun run() {
                            for (i in 0..2) {
                                emitter.onNext(1)
                                try {
                                    sleep(300)
                                } catch (ie: InterruptedException) {
                                    ie.printStackTrace()
                                }
                            }
                        }
                    }.start()
                }

                val disposeAble = Observable.merge(
                    observable1,
                    observable2
                ).subscribe {
                    println(it)
                }

                Thread.sleep(700)
                disposeAble.dispose()
            }
        )
    }
}