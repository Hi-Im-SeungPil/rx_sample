package com.feel.jeon.rx_sample.ui.day1

import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import org.reactivestreams.Publisher
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DayOneStateHolder {

    fun observableCreate() {
        val observable = Observable.create<String> { emitter ->
            emitter.onNext("Hello")
            emitter.onNext("World")
            emitter.onError(Throwable("is Error"))
            emitter.onComplete()
        }

        val observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Logger.d("onSubscribe")
            }

            override fun onError(e: Throwable) {
                Logger.d("on Error -> ${e.message}")
            }

            override fun onComplete() {
                Logger.d("on Complete")
            }

            override fun onNext(t: String) {
                Logger.d("on Next -> $t")
            }

        }

        // 두가지 방식으로 구독 가능.

        // 1.
        observable.subscribeBy(
            onNext = { data -> Logger.e("data -> $data") },
            onError = { error -> Logger.e("error -> ${error.message}") },
            onComplete = { Logger.e("on Complete!!") }
        ).dispose()

        // 2.
        observable.subscribe(observer)

        // RX Java
//        observable.subscribe(
//            // onNext
//            Consumer<String>() {str ->
//
//            },
//            // onError
//            Consumer<Throwable>() {error ->
//
//            },
//            //onComplete
//            Action {
//
//            }
//        )
    }

    fun just() {
        val observable = Observable.just("Hello", "World")
        observable.subscribe { str ->
            Logger.e(str)
        }.dispose()
    }

    // Java에서는 Observable.fromArray(array), kotlin에서는 Array.toObservable()
    fun fromXxx() {
        val list = listOf<Int>(4, 2, 3, 4).toObservable()
        list.subscribe { data ->
            Logger.e(data.toString())
        }.dispose()

        /**
         * Callable 인터페이스는 비동기적인 실행 결과를 반환합니다.
         * Runnable 인터페이스와 비슷하지만 Runnable은 실행 결과를 반환하지 않는다는 점에서 차이가 있습니다.
         */
        val callable = Callable<String> {
            "Hello World callable"
        }
        val callableObservable = Observable.fromCallable(callable)
        callableObservable.subscribe {
            Logger.e(it)
        }.dispose()

        /**
         * Future 인터페이스는 비동기적인 작업의 결과를 구할 때 사용합니다.
         * Future를 이용하면 멀티쓰레드 환경에서 처리된 어떤 데이터를 다른 쓰레드에 전달할 수 있습니다.
         * Future 내부적으로 Thread-Safe 하도록 구현되었기에, synchronized를 사용하지 않아도 됩니다.
         * 보통 ExecutorService를 통해 비동기적인 작업을 할 때 사용됩니다.
         */
        val future = Executors.newSingleThreadExecutor().submit<String> { "Hello World future" }
        val futureObservable = Observable.fromFuture(future)
        futureObservable.subscribe {
            Logger.e(it)
        }.dispose()

        /**
         * Publisher는 잠재적인 아이템 발행을 제공하는 생산자입니다.
         */
        val publisher = Publisher<String> {
            it.onNext("hello publisher")
            it.onComplete()
        }
        val publisherObservable = Observable.fromPublisher(publisher)
        publisherObservable.subscribe {
            Logger.e(it)
        }.dispose()
    }

    fun emptyNeverError() {
        Observable.empty<String>()
            .subscribeBy(
                onNext = { Logger.e("empty - onNext") },
                onError = { Logger.e("empty - onError") },
                onComplete = { Logger.e("empty - onComplete") }
            ).dispose()

        Observable.never<String>()
            .subscribeBy(
                onNext = { Logger.e("never - onNext") },
                onError = { Logger.e("never - onError") },
                onComplete = { Logger.e("never - onComplete") }
            ).dispose()

        Observable.error<String>(Throwable("Error"))
            .subscribeBy(
                onNext = { Logger.e("error - onNext") },
                onError = { Logger.e("error - onError") },
                onComplete = { Logger.e("error - onComplete") }
            ).dispose()
    }

    // 구독을 중기하기 전까지 무한 발행.
    fun interval() {
        val intervalObservable = Observable.interval(1, TimeUnit.SECONDS)
            .subscribe {
                Logger.e(it.toString())
            }
        Thread.sleep(5000)
        intervalObservable.dispose() // 아이템 발행 중단
    }

    fun range() {
        Observable.range(0, 5)
            .subscribe {
                Logger.e(it.toString())
            }.dispose()
    }

    // 특정 시간동안 지연시킨 뒤 아이템 발행하고 종료
    fun timer() {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribe {
                Logger.e("Timer 끝!")
            }

        Logger.e("시작!")
    }

    // defer 연산자는 observer가 구독할 때까지 observable 생성을 지연시킴.
    // subscribe 매서드를 사용해 구독을 요청해야지만 observable 아이템을 생성한다.
    fun defer() {
        val justSrc = Observable.just(
            System.currentTimeMillis()
        )
        val deferSrc = Observable.defer {
            Observable.just(System.currentTimeMillis())
        }

// 현재 시간 출력
        Logger.e("#1 now = ${System.currentTimeMillis()}")
        try {
            Thread.sleep(5000)
        } catch (ie: InterruptedException) {
            ie.printStackTrace()
        }
        Logger.e("#2 now = ${System.currentTimeMillis()}")

// just만을 사용하여 바로 아이템 발행 -> #1 now와 비슷
        justSrc.subscribe {
            Logger.e("#1 time = $it")
        }.dispose()

// defer를 사용하여 구독이 들어왔을 때 아이템 발행 -> #2 now와 비슷
        deferSrc.subscribe {
            Logger.e("#2 time = $it")
        }.dispose()
    }

    /**
     * Subscribe
     * 위에서 Observable 객체에서 발행하는 아이템을 받기 위해서 subscribe() 메서드를 사용하였습니다.
     * subscribe() 메서드는 Observer를 Observable에 연결하는 메서드로, Observable이 발행하는 아이템을 받고,
     * error 또는 complete 알림을 받기 위해서 사용합니다.
     * subscribe() 메서드는 파라미터에 따라서 다양하게 오버로딩되어 있습니다.
     *
     * subscribe(): Disposable
     *
     * subscribe(onNext: Consumer): Disposable
     *
     * subscribe(onNext: Consumer, onError: Consumer)
     *
     * subscribe(onNext: Consumer, onError: Consumer, onComplete: Action)
     *
     * subscribe(onNext: Consumer, onError: Consumer, onComplete: Action, container: DisposableContainer): Disposable
     *
     * subscribe(observer: Observer): Disposable
     *
     * 해당 메서드들의 공통점은 Disposable 객체를 반환하는 것입니다.
     * 유한한 아이템을 발행하는 Observable의 경우 onComplete() 호출로 안전하게 종료됩니다.
     * 하지만 무한하게 아이템을 발행하거나 오랫동안 실행되는 Observable의 경우에는 사용자의 액션 또는 안드로이드 컴포넌트의 생명주기와 관련해서
     * 이미 활성화되어 있다면 구독이 더는 필요하지 않을 수 있습니다.
     * 이럴 때, 메모리 누수 방지를 위해서 Disposable 클래스의 dispose() 메서드를 사용해 아이템 발행을 중단할 수 있습니다.
     *
     * interval 메서드는 무한하게 아이템을 발행하는데, Thread에서 3.5초 이후에 dispose를 호출하여 아이템 발행을 중지시키고 리소스가 폐기되는 코드입니다.
     *
     * 리소스가 이미 폐기되었는지 확인하는 데 Disposable.isDisposed() 메서드를 활용할 수 있습니다.
     *
     * onComplete()를 명시적으로 호출하거나 호출됨을 보장한다면 dispose()를 호출할 필요가 없습니다.
     *
     * 구독자(Observer)가 여러 곳에 있고, 이들을 폐기하려면 각각의 Disposable 객체에 대해서 dispose()를 호출해야 합니다. 이때, CompositeDisposable을 이용하면 이들을 한꺼번에 폐기할 수 있습니다.
     */

    fun disposable() {
        val observable = Observable.interval(1, TimeUnit.SECONDS)
        val disposable = observable.subscribe {
            Logger.e(it.toString())
        }.let {
            Thread().apply {
                try {
                    Thread.sleep(3500)
                } catch (ie: InterruptedException) {
                    ie.printStackTrace()
                }
                // 아이템 발행 해제
                it.dispose()
            }.start()
        }

        // 한꺼번에 리소스 해제 가능.

//    val observable = Observable.interval(1, TimeUnit.SECONDS)
//    // 1초에 한 번씩 아이템 발행(무한히)
//    val disposable = observable.subscribe {
//        Logger.e("Disposable1 : $it")
//    }
//    val disposable2 = observable.subscribe {
//        Logger.e("Disposable2 : $it")
//    }
//    val disposable3 = observable.subscribe {
//        Logger.e("Disposable3 : $it")
//    }
//    val compositeDisposable = CompositeDisposable()
//    compositeDisposable.addAll(disposable, disposable2, disposable3)
//
//    Thread().apply {
//        try {
//            Thread.sleep(3500)
//        } catch (ie: InterruptedException) {
//            ie.printStackTrace()
//        }
//        // 아이템 발행 해제
//        compositeDisposable.dispose()
//    }.start()
    }

    /**
     * ConnectableObservable은 Hot Observable을 구현할 수 있도록 도와주는 타입으로 아무 Observable 타입이나 publish 연산자를 이용하여 간단히 ConnectableObservable로 변환할 수 있습니다.
     * 다만 ConnectableObservable은 구독을 요청해도 Observable을 발행하지 않고,
     * connect() 연산자를 호출할 때 비로소 아이템을 발행하기 시작합니다.
     */

    fun connectableObservable() {
        val connectableObservable = Observable.interval(1, TimeUnit.SECONDS).publish()
// 1번 구독자 등록
        connectableObservable.connect()
        val connectableObserver1 = connectableObservable.subscribe {
            println("#1: $it")
        }
        Thread.sleep(3000)

// 2번 구독자 등록
        val connectableObserver2 = connectableObservable.subscribe {
            println("#2: $it")
        }

        Thread.sleep(3000)

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.addAll(connectableObserver1, connectableObserver2)
        compositeDisposable.dispose()
    }

    /**
     * autoConnect 연산자는 connect 연산자를 호출하지 않더라도, 구독 시에 즉각 아이템을 발행할 수 있도록 도와주는 연산자입니다.
     *
     * autoConnect 연산자의 매개 변수는 아이템을 발행하는 구독자 수로, 만약 2를 인자로 준다면, 구독자가 2개 이상 붙어야 아이템을 발행합니다.
     */

    fun autoConnect() {
        Thread(
            Runnable {
                val observable = Observable.interval(1, TimeUnit.SECONDS)
                    .publish()
                    .autoConnect(2)

                val connectableObserver1 = observable.subscribe {
                    Logger.e("A: $it")
                }

                Thread.sleep(3000)

                val connectableObserver2 = observable.subscribe {
                    Logger.e("B: $it")
                }

                Thread.sleep(3000)

                val compositeDisposable = CompositeDisposable()
                compositeDisposable.addAll(connectableObserver1, connectableObserver2)
                compositeDisposable.dispose()
            }
        ).start()
    }

}