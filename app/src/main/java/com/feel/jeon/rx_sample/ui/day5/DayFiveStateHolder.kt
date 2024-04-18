package com.feel.jeon.rx_sample.ui.day5

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

class DayFiveStateHolder {

    fun exceptionCode() {
        Observable.just("1", "2", "a", "3")
            .map { value ->
                value.toInt()
            }
            .subscribe {
                println(it)
            }.dispose()
    }

    fun exceptionHandling() {
        Observable.just("1", "2", "a", "3")
            .map { value ->
                value.toInt()
            }.subscribeBy(
                onNext = { value -> println(value) },
                onError = { println("Error!!") }
            ).dispose()
    }

    /**
     * onErrorReturn 연산자는 오류가 발생하면 아이템 발행을 종료하고, onError()를 호출하는 대신에 오류 처리를 위한 함수를 실행합니다.
     */
    fun onErrorReturn() {
        Observable.just("1", "2", "a", "3")
            .map { value ->
                value.toInt()
            }
            .onErrorReturn {
                -1
            }
            .subscribe {
                println(it)
            }.dispose()
    }

    /**
     * onErrorResumeNext 연산자는 오류 발생 시 기존 스트림을 종료시키고, 다른 Observable 소스로 스트림을 대체합니다.
     */
    fun onErrorResumeNext() {
        Observable.just("1", "2", "a", "3")
            .map { value ->
                value.toInt()
            }
            .onErrorResumeNext {
                Observable.just(100, 200, 300)
            }
            .subscribe {
                println(it)
            }.dispose()
    }

    /**
     * retry 연산자는 에러 발생 시 인자로 주어진 횟수 만큼 재시도하는 연산자입니다.
     * 즉, Observable이 error를 발행했을 때, Observable을 재구독하도록 하는 것입니다.
     *
     * 보통 네트워크 요청 후 응답을 받아올 경우 실패시에 몇 번 더 요청할 지 결정하는 상황에서 사용할 수 있습니다.
     */
    fun retry() {
        Observable.just("1", "2", "a", "3")
            .map { value ->
                value.toInt()
            }
            .retry(2)
            .onErrorReturn {
                -1
            }
            .subscribe {
                println(it)
            }.dispose()
    }

    /**
     * doOnEach 연산자는 Observable이 아이템을 발행하기 전에 이를 콜백으로 확인할 수 있도록 해줍니다. 콜백은 Notification 형태로 들어옵니다.
     *
     * Notification에는 다음과 같은 메서드(코틀린은 프로퍼티)가 존재하여 현재 어떤 상태인지 확인할 수 있습니다.
     *
     * value: onNext에 의해 획득된 데이터입니다.
     * isOnNext: onNext 신호인 경우 true를 반환합니다.
     * isOnComplete: onComplete 신호인 경우 true를 반환합니다.
     * isOnError: onError 신호인 경우 true를 반환합니다.
     * error: onError 신호인 경우라면 Throwable을 반환하고, 그렇지 않으면 null을 반환합니다.
     *
     */
    fun doOnEach() {
        Observable.just(1, 2, 3)
            .doOnEach { notification ->
                val num = notification.value
                val isOnNext = notification.isOnNext
                val isOnComplete = notification.isOnComplete
                val isOnError = notification.isOnError
                val throwable = notification.error
                println("num : $num")
                println("isOnNext : $isOnNext")
                println("isOnComplete : $isOnComplete")
                println("isOnError : $isOnError")
                throwable?.let {
                    it.printStackTrace()
                }
            }.subscribe { value ->
                println("Subscribed : $value")
            }.dispose()
    }

    /**
     * doOnNext 연산자는 doOnEach와 형태가 매우 비슷하지만 Notification 대신
     * 간단히 발행된 아이템을 확인할 수 있는 Consumer를 파라미터로 넘깁니다.
     */
    fun doOnNext() {
        Observable.just(1, 2, 3)
            .doOnNext { item ->
                if(item > 2) {
                    throw IllegalArgumentException()
                }
            }.subscribeBy(
                onNext = { println(it) },
                onError = { println("Error!!") }
            ).dispose()
    }

    /**
     * doOnSubscribe 연산자는 구독 시마다 콜백을 받을 수 있도록 합니다. 매개 변수로 Disposable을 받을 수 있습니다.
     */
    fun doOnSubscribe() {
        val observable = Observable.just(1, 2, 3)
            .doOnSubscribe { disposable ->
                println("구독")
            }

        observable.subscribe {
            println(it)
        }.dispose()

        observable.subscribe {
            println(it)
        }.dispose()
    }

    /**
     * doOnComplete 연산자는 Emitter의 onComplete() 호출로 Observable이 정상적으로 종료될 때 호출되는 콜백입니다.
     * 오류가 발생하거나 스트림을 폐기하는 경우에는 콜백이 호출되지 않습니다.
     */
    fun doOnComplete() {
        val observable = Observable.create<Int> { emitter ->
            emitter.onNext(1)
            Thread.sleep(1000)
            emitter.onNext(2)
            Thread.sleep(1000)
            emitter.onComplete()
        }.doOnComplete {
            println("Complete!!")
        }

        val disposable1 = observable.subscribe {
            println("First : $it")
        }

        val disposable2 = observable.doOnSubscribe {
            it.dispose()
        }.subscribe {
            println(it)
        }
    }

    /**
     * doOnError 연산자는 Observable 내부에서 onError() 호출로 Observable이 정상적으로 종료되지 않을 때 호출되는 콜백입니다.
     * 콜백 매개 변수로 Throwable이 들어옵니다
     */
    fun doOnError() {
        Observable.just(2, 1, 0)
            .map { num ->
                10 / num
            }
            .doOnError { throwable ->
                println("Error!!")
            } // Throwable이 매개변수로 들어옴
            .subscribeBy(
                onNext = { println(it) },
                onError = { throwable -> throwable.printStackTrace() }
            ).dispose()
    }

    /**
     * doOnTerminate 연산자는 onComplete 연산자와 유사하게 Observable이 종료될 때 호출되는 콜백입니다.
     * onComplete 연산자와의 차이점은 오류가 발생했을 때도 콜백이 호출된다는 점입니다.
     */
    fun doOnTerminate() {
        Observable.just(2, 1, 0)
            .map { value -> 10 / value }
            .doOnComplete { println("doOnComplete") }
            .doOnTerminate { println("doOnTerminate") }
            .subscribeBy(
                onNext = { println(it) },
                onError = { println("Error!!") },
            ).dispose()

        println()

        Observable.just(2, 1)
            .map { value -> 10 / value }
            .doOnComplete { println("doOnComplete") }
            .doOnTerminate { println("doOnTerminate") }
            .subscribeBy(
                onNext = { println(it) },
                onError = { println("Error!!") },
            ).dispose()
    }

    /**
     * doOnDispose는 구독 중인 스트림이 dispose() 메서드 호출로 인해 폐기되는 경우에 콜백이 호출됩니다.
     */
    fun doOnDispose() {
        Thread(
            Runnable {
                val observable = Observable.interval(500, TimeUnit.MILLISECONDS)
                    .doOnDispose {
                        println("doOnDispose")
                    }

                val disposable = observable.subscribe {
                    println(it)
                }

                Thread.sleep(1100)
                disposable.dispose()
            }
        ).start()
    }

    /**
     * Observable이 onError(), onComplete() 또는 스트림이 폐기될 때 doFinally 콜백이 호출됩니다.
     * Observable을 구독한 뒤 종료되는 어떠한 상황에서도 후속 조치를 해야 하는 상황에서 이 연산자를 이용할 수 있습니다.
     *
     * 자바의 try ~ catch ~ finally의 finally라고 생각하면 될 것 같습니다.
     */
    fun doFinally() {
       Thread(
           Runnable {
               val observable = Observable.interval(500, TimeUnit.MILLISECONDS)
                   .doOnComplete { println("doOnComplete") }
                   .doOnTerminate { println("doOnTerminate") }
                   .doFinally { println("doFinally") }

               val disposable = observable.subscribe {
                   println(it)
               }

               Thread.sleep(1100)
               disposable.dispose()
           }
       ).start()
    }
}