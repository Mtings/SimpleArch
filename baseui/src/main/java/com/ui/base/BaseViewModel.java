package com.ui.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ui.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseViewModel extends AndroidViewModel {
    protected final CompositeDisposable subscription = new CompositeDisposable();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<? super T> onNext, Consumer<Throwable> onError, Action onComplete) {
        if (onComplete == null) {
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
        } else {
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete));
        }
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<? super T> onNext, Consumer<Throwable> onError) {
            subscription.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError));
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<? super T> onNext) {

        subscription.add(observable.subscribe(onNext));
    }

    public <T> void submitRequestCatchError(Observable<T> observable, Consumer<? super T> onNext) {

        subscription.add(observable.subscribe(onNext, LogUtil::print));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onDestroy();
    }

    public void onDestroy() {
        subscription.clear();
    }

    public void cancelTask() {
        subscription.clear();
    }

    public static String getStringValue(String s) {
        return s == null ? "" : s;
    }

    public String getString(Integer s) {
        return getApplication().getString(s);
    }

}
