package com.feel.jeon.rx_sample.ui.day7

import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class DayThreeStateHolder {
    /**
     * debounce 또한 RxJava #2 - 변형 연산자의 buffer 연산과 같이 흐름 제어 연산자입니다.
     * 빠르게 연속 이벤트를 처리할 수 있는 흐름 제어 함수입니다.
     * 예를 들면 버튼을 빠르게 누르는 상황에서 마지막으로 누른 이벤트만을 처리하는 경우와 같이 사용할 수 있습니다.
     *
     * 반복적으로 빠르게 발행된 아이템들을 필터링할 때 유용합니다.
     */
    fun debounce() {
        Observable.create { emitter ->
            emitter.onNext("1")
            Thread.sleep(100)
            emitter.onNext("2")
            emitter.onNext("3")
            emitter.onNext("4")
            emitter.onNext("5")
            Thread.sleep(100)
            emitter.onNext("6")
        }.debounce(10, TimeUnit.MILLISECONDS)
            .subscribe { println(it) }.dispose()
    }

    /**
     * distinct 연산자는 이미 발행한 아이템을 중복해 발행하지 않도록 필터링합니다.
     *
     * 자바의 Set 자료구조와 유사하다고 생각하면 될 것 같습니다.
     */
    fun distinct() {
        Observable.just(1,2,2,1,3)
            .distinct()
            .subscribe { println(it) }.dispose()
    }

    /**
     * elementAt 연산자는 발행되는 아이템 시퀀스에서 특정 인덱스에 해당하는 아이템을 필터링합니다.
     */
    fun elementAt() {
        Observable.just(1,2,3,4)
            .elementAt(2)
            .subscribe { println(it) }.dispose()
    }

    /**
     * 조건식이 true일 때만 아이템을 방출하도록 합니다. 즉, 주어진 술어를 만족하는 아이템만을 넘깁니다.
     */
    fun filter() {
        Observable.just(2,30,22,5,60,1)
            .filter { x -> x > 10 }
            .subscribe { println(it) }.dispose()
    }

    /**
     * Sample 연산자는 일정 시간 간격으로 최근에 Observable이 발행한 아이템들을 방출하는 연산자입니다.
     *
     * Sample 연산자도 buffer, debounce 연산과 같이 흐름 제어 연산에 해당합니다.
     */
    fun sample() {
        Thread(
            Runnable {
                val disposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                    .sample(300, TimeUnit.MILLISECONDS)
                    .subscribe {
                        println(it)
                    }

                Thread.sleep(1000)
                disposable.dispose()
            }
        ).start()
    }

    /**
     * Observable이 발행하는 n개의 아이템을 무시하고 이후에 나오는 아이템을 방출하는 연산자입니다.
     */
    fun skip() {
        Observable.just(1,2,3,4)
            .skip(2)
            .subscribe { println(it) }.dispose()
    }

    /**
     * take 연산자는 skip 연산자와 반대로 Observable이 처음 발행하는 n개의 아이템만 방출하도록 하는 연산자입니다.
     *
     * n번째 뒤로 발행되는 아이템은 모두 무시합니다.
     */
    fun take() {
        Observable.just(1,2,3,4)
            .take(2)
            .subscribe { println("Data : $it") }.dispose()
    }

    /**
     * 모든 발행되는 아이템이 특정 조건을 만족할 때 true를 반환합니다.
     *
     * 만약 아이템 중 하나라도 조건에 부합하지 않는다면 false를 반환합니다.
     */
    fun all() {
        Observable.just(1,2)
            .all { num -> num > 0 }
            .subscribe(::println).dispose()

        Observable.just(0,1,2)
            .all { num -> num > 0 }
            .subscribe(::println).dispose()
    }

    /**
     * 여러 개의 Observable을 동시에 구독하고,
     * 그중 가장 먼저 아이템을 발행하는 Observable을 선택하고 싶다면 amb 연산자를 사용하면 됩니다.
     */
    fun amb() {
        val list = ArrayList<Observable<Int>>()
        list.add(Observable.just(20,40,60).delay(100, TimeUnit.MILLISECONDS))
        list.add(Observable.just(1,2,3))
        list.add(Observable.just(0,0,0).delay(200, TimeUnit.MILLISECONDS))
        Observable.amb(list).subscribe { println(it) }.dispose()
    }

}