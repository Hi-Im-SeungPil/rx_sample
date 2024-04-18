package com.feel.jeon.rx_sample.ui.day2

import io.reactivex.rxjava3.core.Observable
import java.util.Random
import java.util.concurrent.TimeUnit

class DayTwoStateHolder {
    /**
     * 발행되는 값에 대해 원하는 수식을 적용하거나 다른 타입으로 변환시킬 수 있습니다. 즉, 원하는 형태로 데이터를 가공하는 것입니다.
     */
    fun map() {
        val intObservable = Observable.just(1, 2, 3)
        val multiplyIntObservable = intObservable.map {
            it * 10
        }
        multiplyIntObservable.subscribe {
            println(it)
        }.dispose()
    }

    /**
     * flatMap 연산자는 제공한 함수를 Observable에서 제공하는 각 아이템에 적용한 후 그대로 return하지 않고,
     * Observable들로 return한 다음, 이것들을 병합해서 다시 Observable로 return 합니다.
     * 다만, flatMap은 아이템 순서를 전혀 신경쓰지 않습니다.
     */
    fun flatMap() {
        val strObservable = Observable.just("a", "b", "c")
        strObservable.flatMap { str ->
            Observable.just(str + 1, str + 2)
        }.subscribe {
            println(it)
        }.dispose()
    }

    /**
     * switchMap 연산자도 flatMap 연산자와 같이 연산자에 제공한 함수를 Observable에서 제공하는 각 아이템에 적용한 후 그대로 return하지 않고,
     * Observable들로 return한 다음, 이것들을 병합해서 다시 Observable로 return 합니다. 다만, 가장 최근에 발행된 아이템을 방출하여 반환합니다.
     * 즉, 새로운 데이터가 들어올 경우 이전 동작을 멈추고 현재 들어온 작업을 수행하는 것입니다.
     *
     * 검색기능을 구현하기 좋음
     */
    fun switchMap() {
        val observable = Observable.just("a", "b", "c")

        observable.switchMap { str ->
            val delay = Random().nextInt(2)
            Observable.just("${str}1", "${str}2")
                .delay(delay.toLong(), TimeUnit.SECONDS)
        }
            .toList()
            .subscribe { result ->
                println("Result : $result")
            }.dispose()
    }

    /**
     * concatMap 연산자도 flatMap 연산자와 같이 연산자에 제공한 함수를 Observable에서 제공하는 각 아이템에 적용한 후 그대로 return하지 않고,
     * Observable들로 return한 다음, 이것들을 병합해서 다시 Observable로 return 합니다.
     * 다만, 처음에 발행된 아이템의 순서를 유지하고 있습니다.
     */
    fun concatMap() {
        val observable = Observable.just("a", "b", "c")

        observable.concatMap { str ->
            val delay = Random().nextInt(2)
            Observable.just("${str}1", "${str}2")
                .delay(delay.toLong(), TimeUnit.SECONDS)
        }
            .toList()
            .subscribe { result ->
                println("Result : $result")
            }.dispose()
    }

    /**
     * buffer() 함수는 일정 시간 동안 데이터를 모아두었다가 한꺼번에 방출합니다.
     * 즉, Observable이 발행하는 아이템을 묶어서 List로 발행합니다.
     * 따라서 넘쳐나는 데이터 흐름을 제어하면서 모든 데이터를 받아야 하는 경우 활용할 수 있습니다.
     *
     * 에러를 발행하는 경우 이미 발행된 아이템들이 버퍼에 포함되더라도 버퍼를 발행하지 않고 에러를 즉시 전달합니다.
     */
    fun buffer() {
        Observable.range(0, 10)
            .buffer(3)
            .subscribe { integers ->
                println("버퍼 아이템 발행")
                integers.forEach {
                    println("#$it")
                }
            }.dispose()
    }

    /**
     * scan 연산자는 순차적으로 발행되는 아이템들의 연산을 다음 아이템 발행의 첫 번째 인자로 전달합니다
     */
    fun scan() {
        Observable.range(1, 5)
            .scan { x, y ->
                print(String.format("%d + %d = ", x, y))
                x + y
            }.subscribe {
                println(it)
            }.dispose()
    }

    /**
     * 아이템들을 특정 그룹화된 GroupObservable로 재정의할 수 있습니다. 즉, 단일 Observable을 여러 개로 이루어진 Observable 그룹으로 만드는 기능을 수행합니다.
     *
     * 마블 다이어그램에서 원 모양의 아이템들을 하나의 GroupObservable로, 삼각형 모양의 아이템들을 하나의 GroupObservable로 묶는 모습을 확인할 수 있습니다.
     */
    fun groupBy() {
        Observable.just(
            "Magenta Circle",
            "Cyan Circle",
            "Yellow Triangle",
            "Yellow Circle",
            "Magenta Triangle",
            "Cyan Triangle"
        ).groupBy { item ->
            if(item.contains("Circle")) {
                "Circle"
            } else if(item.contains("Triangle")) {
                "Triangle"
            } else {
                "None"
            }
        }.subscribe { group ->
            group.subscribe { shape ->
                println("${group.key} : $shape")
            }.dispose()
        }.dispose()
    }



}